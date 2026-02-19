package com.lfmall.backend.cart.model.service;

import java.util.List;

import com.lfmall.backend.cart.model.dto.CartDto;
import com.lfmall.backend.cart.model.dto.CartItemDto;

public interface CartService {

	List<CartDto> getCartsByMemberId(Long memberId);

 
}