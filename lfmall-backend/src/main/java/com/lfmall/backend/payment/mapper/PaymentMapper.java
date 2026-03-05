package com.lfmall.backend.payment.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
	public void subtractStock(Map<String, Object> item);

	public void insertOrderItem(Map<String, Object> item);

	public void saveOrderInfo(Map<String, Object> orderData);

	public void savePaymentInfo(Map<String, Object> paymentData);

	public void deletePaidCartItems(Map<String, Object> data);
}
