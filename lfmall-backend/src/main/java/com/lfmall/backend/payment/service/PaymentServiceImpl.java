package com.lfmall.backend.payment.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfmall.backend.payment.mapper.PaymentMapper;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentMapper paymentMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void processTotalOrder(Map<String, Object> orderData) {
		saveOrderInfo(orderData); // 상품 정보 저장(주문 아이디 생성)
		
		List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("orderCompleteItems");
		
		//주문 완료 상품 빈값일 경우 리턴
		if (items == null || items.isEmpty()) {
			return;
		}

		//카트 주문 리스트 별
		List<Long> paidCartIds = new ArrayList<>();
		for (Map<String, Object> item : items) {
			item.put("order_id", orderData.get("order_id")); //저장한 상품 아이디 주가
			insertOrderItem(item);  //상품 상세정보 저장
			subtractStock(item); // 재고 감소

			Object cartIdObj = item.get("cartId");
			if (cartIdObj instanceof Number cartId && cartId.longValue() > 0) { //장바구니에서 주문한 경우
				paidCartIds.add(cartId.longValue());// 장바구니 삭제 명단
			}
		}

		if (!paidCartIds.isEmpty()) {
			Map<String, Object> deletePayload = new HashMap<>();
			deletePayload.put("member_id", orderData.get("member_id"));
			deletePayload.put("cartIds", paidCartIds);
			paymentMapper.deletePaidCartItems(deletePayload);
		}
	}

	//주문 상품 재고 감소
	private void subtractStock(Map<String, Object> item) {
		paymentMapper.subtractStock(item);
	}

	//주문 상품 별 상세 저장
	private void insertOrderItem(Map<String, Object> item) {
		paymentMapper.insertOrderItem(item);
	}

	//즈믄 장버 자징
	private void saveOrderInfo(Map<String, Object> orderData) {
		paymentMapper.saveOrderInfo(orderData);
	}

	//토스 결제 승인(승인 시 DB 데이터 변경되게끔)
	@Override
	public Map<String, Object> confirmTossPayment(Map<String, Object> requestData) throws Exception {
		String paymentKey = (String) requestData.get("payment_key");
		String orderId = (String) requestData.get("order_id");
		int amount = ((Number) requestData.get("total_amount")).intValue();

		Map<String, Object> tossResponse = callTossConfirmApi(paymentKey, orderId, amount);

		Map<String, Object> paymentData = new HashMap<>();
		paymentData.put("payment_key", paymentKey);
		paymentData.put("order_id", orderId);
		paymentData.put("payment_amount", amount);
		paymentData.put("payment_status", tossResponse.get("status"));
		paymentData.put("payment_approved_at", toTimestamp((String) tossResponse.get("approvedAt")));
		paymentMapper.savePaymentInfo(paymentData);

		return Map.of("success", true);
	}

	//토스 api 호출
	private Map<String, Object> callTossConfirmApi(String paymentKey, String orderId, int amount) throws Exception {
		String secretKey = "test_sk_6bJXmgo28e1KOdNMvp4Y8LAnGKWx"; //토스 api 시크릿 키 - 후에 .env로 관리할 것
		String encodedKey = Base64.getEncoder()
				.encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

		//토스 api에 넘겨주는 필수 정보
		Map<String, Object> body = new HashMap<>();
		body.put("paymentKey", paymentKey);
		body.put("orderId", orderId);
		body.put("amount", amount);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
				.header("Authorization", "Basic " + encodedKey)
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(body)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			Map<String, Object> errorBody = new ObjectMapper().readValue(response.body(), Map.class);
			throw new Exception("Toss confirm failed: " + errorBody.get("message"));
		}

		return new ObjectMapper().readValue(response.body(), Map.class);
	}

	//
	private Timestamp toTimestamp(String approvedAt) {
		if (approvedAt == null || approvedAt.isBlank()) {
			return new Timestamp(System.currentTimeMillis());
		}
		try {
			return Timestamp.from(OffsetDateTime.parse(approvedAt).toInstant());
		} catch (Exception ignore) {
			return new Timestamp(System.currentTimeMillis());
		}
	}
}
