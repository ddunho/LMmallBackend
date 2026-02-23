package com.lfmall.backend.cart.model.service;

import java.util.List;

import com.lfmall.backend.cart.model.dto.CartDto;
import com.lfmall.backend.cart.model.dto.CartItemDto;

public interface CartService {
    List<CartDto> getCartsByMemberId(Long memberId);

    // addCart는 최소 member_id, stock_id, cart_quantity 필요
    void addCart(Long memberId, Long stockId, Integer quantity);

    void changeQuantity(Long cartId, Integer quantity);

    // 옵션 변경 = stock_id 변경
    void updateStock(Long cartId, Long newStockId, Integer quantity, Long memberId);

    void deleteCarts(List<Long> cartIds, Long memberId);
}