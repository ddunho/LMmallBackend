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
		saveOrderInfo(orderData);

		List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("orderCompleteItems");
		if (items == null || items.isEmpty()) {
			return;
		}

		List<Long> paidCartIds = new ArrayList<>();
		for (Map<String, Object> item : items) {
			item.put("order_id", orderData.get("order_id"));
			insertOrderItem(item);
			subtractStock(item);

			Object cartIdObj = item.get("cartId");
			if (cartIdObj instanceof Number cartId && cartId.longValue() > 0) {
				paidCartIds.add(cartId.longValue());
			}
		}

		if (!paidCartIds.isEmpty()) {
			Map<String, Object> deletePayload = new HashMap<>();
			deletePayload.put("member_id", orderData.get("member_id"));
			deletePayload.put("cartIds", paidCartIds);
			paymentMapper.deletePaidCartItems(deletePayload);
		}
	}

	private void subtractStock(Map<String, Object> item) {
		paymentMapper.subtractStock(item);
	}

	private void insertOrderItem(Map<String, Object> item) {
		paymentMapper.insertOrderItem(item);
	}

	private void saveOrderInfo(Map<String, Object> orderData) {
		paymentMapper.saveOrderInfo(orderData);
	}

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

	private Map<String, Object> callTossConfirmApi(String paymentKey, String orderId, int amount) throws Exception {
		String secretKey = "test_sk_6bJXmgo28e1KOdNMvp4Y8LAnGKWx";
		String encodedKey = Base64.getEncoder()
				.encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

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
