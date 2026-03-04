package com.lfmall.backend.payment.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private PaymentMapper paymentMapper;

	@Override
	@Transactional(rollbackFor = Exception.class) //하나라도 실패하면 취소
	public void processTotalOrder(Map<String, Object> orderData) {	
		//1. 주문 정보 저장: 주소, 주문자명 등
		this.saveOrderInfo(orderData);
		
		List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("orderCompleteItems");
	        for(Map<String, Object> item : items) {
			//2. 주문품목 별 상세항목 저장
			this.insertOrderItem(item);
			//3. 주문품목 별 재고 감소
			this.subtractStock(item);
        }
		
		//장바구니 비우기 - 이건 분류상 카트 매퍼에서 처리해야할듯
//		if("cart".equals(orderData.get("purchase_type"))) {
//            this.clearCart(orderData.get("member_id"));
//        }
	}

	//재고 감소
	private void subtractStock(Map<String, Object> item) {
		paymentMapper.subtractStock(item);// product 매퍼에서 해당 아이템 id, 옵션에 해당하는 product_discount 주문 개수만큼 감소
	}

	//품목 별 상세항목 저장
	private void insertOrderItem(Map<String, Object> item) {
		paymentMapper.insertOrderItem(item);	
	}

	//주문자 정보 저장
	private void saveOrderInfo(Map<String, Object> orderData) {	
		paymentMapper.saveOrderInfo(orderData);
	}

	@Override
	public Map<String, Object> confirmTossPayment(Map<String, Object> requestData) throws Exception {
	    
	    String paymentKey = (String) requestData.get("payment_key");
	    String orderId    = (String) requestData.get("order_id");
	    int amount        = ((Number) requestData.get("total_amount")).intValue();
	    int memberId      = ((Number) requestData.get("member_id")).intValue();

	    // 1. Toss API 승인 요청
	    Map<String, Object> tossResponse = callTossConfirmApi(paymentKey, orderId, amount);

	    // 2. payment 테이블 저장
	    Map<String, Object> paymentData = new HashMap<>();
	    paymentData.put("payment_key",  paymentKey);
	    paymentData.put("order_id",orderId);
	    paymentData.put("payment_amount",amount);
	    paymentData.put("payment_status",tossResponse.get("status"));        // "DONE"
	    paymentData.put("payment_approved_at",tossResponse.get("approvedAt"));    // Toss 승인시각
	    paymentMapper.savePaymentInfo(paymentData);

	    return Map.of("success", true);
	}

	// Toss 승인 API 호출
	private Map<String, Object> callTossConfirmApi(
	        String paymentKey, String orderId, int amount) throws Exception {

	    String secretKey  = "test_sk_6bJXmgo28e1KOdNMvp4Y8LAnGKWx"; 
	    String encodedKey = Base64.getEncoder()
	            .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

	    // 요청 body
	    Map<String, Object> body = new HashMap<>();
	    body.put("paymentKey", paymentKey);
	    body.put("orderId",    orderId);
	    body.put("amount",     amount);

	    // Toss API 호출
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	        .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
	        .header("Authorization",  "Basic " + encodedKey)
	        .header("Content-Type",   "application/json")
	        .POST(HttpRequest.BodyPublishers.ofString(
	            new ObjectMapper().writeValueAsString(body)
	        ))
	        .build();

	    HttpResponse<String> response = client.send(request,
	            HttpResponse.BodyHandlers.ofString());

	    if (response.statusCode() != 200) {
	        Map<String, Object> errorBody = new ObjectMapper()
	                .readValue(response.body(), Map.class);
	        throw new Exception("Toss 승인 실패: " + errorBody.get("message"));
	    }

	    return new ObjectMapper().readValue(response.body(), Map.class);
	}
	
	//장바구니에서 상품 삭제
//	private void clearCart(Map<String, Object> orderData) {	
//		paymentMapper.deleteCart(orderData);// 장바구니 mapper에서 멤버의 해당 아이템 정보 삭제
//	}

}
