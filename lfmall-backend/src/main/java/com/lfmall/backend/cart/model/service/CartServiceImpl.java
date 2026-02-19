package com.lfmall.backend.cart.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfmall.backend.cart.model.dto.CartDto;
import com.lfmall.backend.cart.model.mapper.CartMapper;
@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartMapper cartMapper;
	
	@Override
	public List<CartDto> getCartsByMemberId(Long memberId) {
		return cartMapper.selectCartsByMemberId(memberId);
	}



}
