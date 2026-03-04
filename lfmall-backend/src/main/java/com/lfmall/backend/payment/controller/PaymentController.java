package com.lfmall.backend.payment.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lfmall.backend.payment.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/api/order")
@RestController
public class PaymentController {
	@Autowired
	private PaymentService paymentService;

	/* 주문 데이터 가져오는 건 화면단의 sessionStorage에서 처리. */
	
	//결제 승인 요청용 - confirm 함수와 매칭할것임.
    @PostMapping("")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> requestData) {
    	try {
            Map<String, Object> result = paymentService.confirmTossPayment(requestData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // 주문 데이터 저장
    @PostMapping("/complete")
    public ResponseEntity<?> completeOrder(@RequestBody Map<String, Object> orderData) {
        try {
        	//서비스 메서드 통합 호출용
        	paymentService.processTotalOrder(orderData);
        	
            return ResponseEntity.ok(Map.of("success", true));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
