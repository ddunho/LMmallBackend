package com.lfmall.backend.cart.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lfmall.backend.cart.model.service.CartService;
import com.lfmall.backend.product.service.ProductService;


@RestController
@RequestMapping("/api/cart")

public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductService productService;
    @PostMapping("/carts")
    public ResponseEntity<Object> getCartsByMemberId(
    		@RequestBody Map<String, Object> memberData /*HttpSession session*/) { //수정(쿠키): 쿠키를 통해 memberId 전송
        Map<String, Object> response = new HashMap<>();
        try {
//            Long memberId = getLoginMemberId(session);
        	Long memberId = ((Number) memberData.get("memberId")).longValue(); // 수정(쿠키): 프론트에서 memberId 가져옴
            List<Map<String, Object>> cartList = cartService.getCartsByMemberId(memberId);
            response.put("success", true);
            response.put("data", cartList);
        } catch (ClassCastException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 조회 실패: 세션 정보 타입 오류");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 조회 실패: 로그인 필요");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 조회 실패");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/count")
	public ResponseEntity<Object> getCartCount(
			@RequestBody Map<String, Object> memberData /* HttpSession session */) { //수정(쿠키)
        Map<String, Object> response = new HashMap<>();
        try {     	
//            Long memberId = getLoginMemberId(session);
        	Long memberId = ((Number) memberData.get("memberId")).longValue(); // 수정(쿠키): 프론트에서 memberId 가져옴
            int count = cartService.getCartCountByMemberId(memberId);
            response.put("success", true);
            response.put("count", count);
        } catch (ClassCastException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "쇼핑백 개수 조회 실패: 세션 정보 타입 오류");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "쇼핑백 개수 조회 실패: 로그인 필요");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "쇼핑백 개수 조회 실패");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/addcart")
    public ResponseEntity<Object> addCart(@RequestBody Map<String, Object> body/* HttpSession session */) {
    	//수정(쿠키): body기능-프론트에서 보낸 selectedOption과 member 쿠키 데이터 전부 가져옴
        Map<String, Object> response = new HashMap<>();
        try {
        	Long memberId = toLong(body.get("memberId")); //수정(쿠키): 프론트의 멤버아이디 받아옴
            List<Map<String, Object>> selectedOption = 
                (List<Map<String, Object>>) body.get("selectedOption");
            
            for (Map<String, Object> item : selectedOption) {
//              Long memberId = getLoginMemberId(session);
                Long stockId = toLong(item.get("productId"));
                Long optionId = toLong(item.get("option_id"));
                //System.out.println("option_id : " + optionId);
                Integer qty = toInt(item.get("quantity"));
                cartService.addCart(memberId, stockId, optionId, qty);
            }
            response.put("success", true);
        } catch (ClassCastException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 담기 실패: 세션 정보 타입 오류");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 담기 실패: 로그인 필요");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 담기 실패");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chquantity")
    public ResponseEntity<Object> changeQuantity(@RequestBody Map<String, Object> body /* HttpSession session */) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long cartId = Long.valueOf(body.get("cart_id").toString());
            Integer quantity = Integer.valueOf(body.get("quantity").toString());

            cartService.changeQuantity(cartId, quantity);
            response.put("success", true);
        } catch (ClassCastException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "수량 변경 실패: 세션 정보 타입 오류");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "수량 변경 실패: 로그인 필요");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "수량 변경 실패");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-option")
    public ResponseEntity<Object> updateOption(@RequestBody Map<String, Object> body /* HttpSession session */) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long cartId = Long.valueOf(body.get("cart_id").toString());
            //Long memberId = Long.valueOf(body.get("member_id").toString());
            Long memberId = getLoginMemberId(session);
            Long optionId = Long.valueOf(body.get("option_id").toString());
            Integer quantity = Integer.valueOf(body.get("quantity").toString());

            //cartService.updateStock(cartId, newStockId, quantity, memberId);
            cartService.updateOptionByCartAndMember(cartId, memberId, quantity, optionId);
            response.put("success", true);
        } catch (ClassCastException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "옵션 변경 실패: 세션 정보 타입 오류");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "옵션 변경 실패: 로그인 필요");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "옵션 변경 실패");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteCarts(@RequestBody Map<String, Object> body/* HttpSession session */) {
        Map<String, Object> response = new HashMap<>();
        try {
//          Long memberId = getLoginMemberId(session);
        	Long memberId = ((Number) body.get("memberId")).longValue(); //수정(쿠키): body에서 멤버아이디 받아오기
        	
            List<Object> rawIds = (List<Object>) body.get("cart_ids");
            List<Long> cartIds = rawIds.stream().map(x -> Long.valueOf(x.toString())).toList();

            cartService.deleteCarts(cartIds, memberId);
            response.put("success", true);
        } catch (ClassCastException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "삭제 실패: 세션 정보 타입 오류");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "삭제 실패: 로그인 필요");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "삭제 실패");
        }
        return ResponseEntity.ok(response);
    }

    private Long toLong(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.longValue();
        return Long.valueOf(v.toString());
    }

    private Integer toInt(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        return Integer.valueOf(v.toString());
    }

//    private long getLoginMemberId(HttpSession session) {
//        Object attr = session.getAttribute("userLogin");
//        if (attr == null) {
//            throw new IllegalStateException("로그인이 필요합니다. (세션 없음)");
//        }
//
//        if (!(attr instanceof Map<?, ?> sessionMap)) {
//            throw new ClassCastException("session userLogin 타입이 Map이 아닙니다.");
//        }
//
//        Object memberId = sessionMap.get("memberId");
//        if (memberId instanceof Long id) {
//            return id;
//        }
//
//        throw new IllegalStateException("현재 login 중인 memberId 정보가 없습니다.");
//    }
}
