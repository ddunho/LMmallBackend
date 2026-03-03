package com.lfmall.backend.cart.model.service;

import java.util.List;
import java.util.Map;

import com.lfmall.backend.cart.model.dto.CartDto;
import com.lfmall.backend.cart.model.dto.CartItemDto;

public interface CartService {
    List<Map<String, Object>> getCartsByMemberId(Long memberId);

    int getCartCountByMemberId(Long memberId);

    // addCart??理쒖냼 member_id, stock_id, cart_quantity ?꾩슂
    void addCart(Long memberId, Long stockId, Integer quantity);

    void changeQuantity(Long cartId, Integer quantity);

    // ?듭뀡 蹂寃?= stock_id 蹂寃?
    void updateStock(Long cartId, Long newStockId, Integer quantity, Long memberId);

    void deleteCarts(List<Long> cartIds, Long memberId);
}
