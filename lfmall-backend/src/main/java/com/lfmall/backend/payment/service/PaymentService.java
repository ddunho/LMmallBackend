package com.lfmall.backend.payment.service;

import java.util.Map;

public interface PaymentService {

	void processTotalOrder(Map<String, Object> orderData);

	Map<String, Object> confirmTossPayment(Map<String, Object> requestData) throws Exception;

}
