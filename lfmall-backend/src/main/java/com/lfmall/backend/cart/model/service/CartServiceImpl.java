package com.lfmall.backend.cart.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lfmall.backend.cart.model.mapper.CartMapper;
import com.lfmall.backend.product.mapper.ProductMapper;
@Service
public class CartServiceImpl implements CartService {
	@Autowired
    private CartMapper cartMapper;

	@Autowired
	private ProductMapper productMapper;
    @Override
    public List<Map<String, Object>> getCartsByMemberId(Long memberId) {
        List<Map<String, Object>> cartList = cartMapper.selectCartsByMemberId(memberId);
        List<Integer> productIds = getProductIds(cartList);

        attachOtherOptionsByProductIds(cartList, productIds);
    	/*for(Map<String, Object> cart : cartList) { // 가능한 다른 옵션들(otherOptions)을 다시 넣는 역할.
        // productId 꺼내기
        Integer productId = ((Number) cart.get("productId")).intValue();

        // 옵션 조회
        List<Map<String, Object>> otherOptions = productService.getProductOptions(productId);
        
        //Map에 추가
        cart.put("otherOptions", otherOptions);
    }*/
        //return cartMapper.selectCartsByMemberId(memberId);
        return cartList;
    }
    
    @Override
    public int getCartCountByMemberId(Long memberId) {
        return cartMapper.selectCartCountByMemberId(memberId);
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
    
    /************************************************************************/
    
    private List<Integer> getProductIds(List<Map<String, Object>> cartList) {
        Set<Integer> set = new HashSet<>();

        for (Map<String, Object> cart : cartList) {
            Integer productId = (Integer)cart.get("productId");
            if (productId != null) set.add(productId);
        }

        return new ArrayList<>(set);
    }
    /***
     *CartList에 고를수 있는 옵션들을 따로 삽입하는 함수. 
     */
    private void attachOtherOptionsByProductIds(
            List<Map<String, Object>> cartList,
            List<Integer> productIds
    ) {
    	
    	
        
        if (cartList == null || cartList.isEmpty()) return;

        // productIds가 비어있으면 otherOptions를 빈 배열로 세팅
        if (productIds == null || productIds.isEmpty()) {
            for (Map<String, Object> cart : cartList) {
                cart.put("otherOptions", Collections.emptyList());
            }
            return;
        }

        // 1) IN 한방 조회
        List<Map<String, Object>> optionRows = cartMapper.selectOptionsByProductIds(productIds);

        // 2) productId -> options 그룹핑
        Map<Integer, List<Map<String, Object>>> optionsByProductId = new HashMap<>();
        if (optionRows != null) {
            for (Map<String, Object> row : optionRows) {
                Integer pid = (Integer) row.get("productId"); // SQL에서 "productId"로 내려온 값
                if (pid == null) continue;

                optionsByProductId.computeIfAbsent(pid, k -> new ArrayList<>()).add(row);
            }
        }

        // 3) cartList에 매칭해서 otherOptions 주입
        for (Map<String, Object> cart : cartList) {
            Integer pid = (Integer) cart.get("productId");
            List<Map<String, Object>> otherOptions =
                    (pid == null)
                            ? Collections.emptyList()
                            : optionsByProductId.getOrDefault(pid, Collections.emptyList());

            cart.put("otherOptions", otherOptions);
        }
    }
}
