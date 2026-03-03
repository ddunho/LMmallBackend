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

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<Object> getCartsByMemberId(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long memberId = getLoginMemberId(session);
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
    public ResponseEntity<Object> getCartCount(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long memberId = getLoginMemberId(session);
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
    public ResponseEntity<Object> addCart(@RequestBody List<Map<String, Object>> selectedOption, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            for (Map<String, Object> item : selectedOption) {
                Long memberId = getLoginMemberId(session);
                Long stockId = toLong(item.get("productId"));
                Integer qty = toInt(item.get("quantity"));
                cartService.addCart(memberId, stockId, qty);
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
    public ResponseEntity<Object> changeQuantity(@RequestBody Map<String, Object> body, HttpSession session) {
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
    public ResponseEntity<Object> updateOption(@RequestBody Map<String, Object> body, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long cartId = Long.valueOf(body.get("cart_id").toString());
            Long memberId = Long.valueOf(body.get("member_id").toString());
            Long newStockId = Long.valueOf(body.get("stock_id").toString());
            Integer quantity = Integer.valueOf(body.get("quantity").toString());

            cartService.updateStock(cartId, newStockId, quantity, memberId);
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
    public ResponseEntity<Object> deleteCarts(@RequestBody Map<String, Object> body, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long memberId = getLoginMemberId(session);

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

    private long getLoginMemberId(HttpSession session) {
        Object attr = session.getAttribute("userLogin");
        if (attr == null) {
            throw new IllegalStateException("로그인이 필요합니다. (세션 없음)");
        }

        if (!(attr instanceof Map<?, ?> sessionMap)) {
            throw new ClassCastException("session userLogin 타입이 Map이 아닙니다.");
        }

        Object memberId = sessionMap.get("memberId");
        if (memberId instanceof Long id) {
            return id;
        }

        throw new IllegalStateException("현재 login 중인 memberId 정보가 없습니다.");
    }
}
