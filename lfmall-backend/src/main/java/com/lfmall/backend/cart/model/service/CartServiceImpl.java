package com.lfmall.backend.cart.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lfmall.backend.cart.model.dto.CartDto;
import com.lfmall.backend.cart.model.mapper.CartMapper;
@Service
public class CartServiceImpl implements CartService {
	@Autowired
    private CartMapper cartMapper;

    @Override
    public List<CartDto> getCartsByMemberId(Long memberId) {
        return cartMapper.selectCartsByMemberId(memberId);
    }

    @Override
    @Transactional
    public void addCart(Long memberId, Long stockId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("quantity는 1 이상이어야 합니다.");
        }

        Integer existQty = cartMapper.selectCartQuantity(memberId, stockId);

        if (existQty == null) {
            cartMapper.insertCart(memberId, stockId, quantity);
        } else {
            // 같은 stock_id가 이미 담겨있으면 수량 누적
            int newQty = existQty + quantity;
            cartMapper.updateCartQuantityByMemberAndStock(memberId, stockId, newQty);
        }
    }

    @Override
    @Transactional
    public void changeQuantity(Long cartId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("quantity는 1 이상이어야 합니다.");
        }
        int updated = cartMapper.updateCartQuantityById(cartId, quantity);
        if (updated == 0) {
            throw new IllegalStateException("수량 변경 실패: cart_id가 유효하지 않음");
        }
    }

    @Override
    @Transactional
    public void updateStock(Long cartId, Long newStockId, Integer quantity, Long memberId) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("quantity는 1 이상이어야 합니다.");
        }

        // (선택) 같은 newStockId가 이미 장바구니에 있으면 merge 처리하는 게 더 좋음
        // 지금은 단순 변경만.

        int updated = cartMapper.updateCartStockAndQuantity(cartId, memberId, newStockId, quantity);
        if (updated == 0) {
            throw new IllegalStateException("옵션 변경 실패: cart_id가 없거나 member 불일치");
        }
    }

    @Override
    @Transactional
    public void deleteCarts(List<Long> cartIds, Long memberId) {
        if (cartIds == null || cartIds.isEmpty()) return;
        cartMapper.deleteCartItems(memberId, cartIds);
    }
}