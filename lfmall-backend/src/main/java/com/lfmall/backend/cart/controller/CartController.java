package com.lfmall.backend.cart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lfmall.backend.cart.model.dto.CartDto;
import com.lfmall.backend.cart.model.service.CartService;

import jakarta.servlet.http.HttpSession;

//import lombok.RequiredArgsConstructor;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
    private CartService cartService;
	 /** @from : CartItems.jsx
     * member_id로 장바구니 목록 조회
     * body: { member_id? }
     */
    @PostMapping("/carts")
    public ResponseEntity<Object> getCartsByMemberId( HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
        	Long memberId = getLoginMemberId(session);

        	
            //Long memberId = Long.valueOf(body.get("member_id").toString());
            List<Map<String, Object>> cartList = cartService.getCartsByMemberId(memberId);

            response.put("success", true);
            response.put("data", cartList);
            
        } catch (ClassCastException e) { // "loginMemberId" 의 타입이 잘못 저장된 경우
        	e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 조회 실패 : 로그인 세션정보가 올바르지 않습니다.");
          
        } catch(IllegalStateException e) {
        	e.printStackTrace();
        	response.put("success", false);
            response.put("message", "장바구니 조회 실패 : 로그인이 필요합니다.");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 조회 실패");
            
        }
        
        return ResponseEntity.ok(response);
    }

    /** 장바구니 담기
     * ✅ cart 테이블 구조 기준: member_id + stock_id + quantity 필요
     * body: { member_id, stock_id, quantity }
     */
    @PostMapping("/addcart")
    public ResponseEntity<Object> addCart(	@RequestBody List<Map<String, Object>> selectedOption
    										, HttpSession session ) {
        Map<String, Object> response = new HashMap<>();
        try {
        	/*
            Long memberId = Long.valueOf(body.get("member_id").toString());
            Long stockId = Long.valueOf(body.get("stock_id").toString());

            // quantity 없으면 기본 1
            Integer quantity = body.get("quantity") == null ? 1 : Integer.valueOf(body.get("quantity").toString());*/
            for (Map<String, Object> item : selectedOption) {
            	Long memberId = getLoginMemberId(session);
            	//Long memberId = toLong(item.get("member_id"));   
                Long stockId  = toLong(item.get("productId"));    
                Long optionId  = toLong(item.get("optionId"));    
                Integer qty    = toInt(item.get("quantity"));     
                cartService.addCart(memberId, stockId, qty);

            }
            response.put("success", true);
            
        } catch (ClassCastException e) { // "loginMemberId" 의 타입이 잘못 저장된 경우
        	e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 담기 실패 : 로그인 세션정보가 올바르지 않습니다.");
          
        } catch(IllegalStateException e) {
        	e.printStackTrace();
        	response.put("success", false);
            response.put("message", "장바구니 담기 실패 : 로그인이 필요합니다.");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 담기 실패");
            
        }
        return ResponseEntity.ok(response);
    }

    /** 수량 변경
     * body: { cart_id, quantity }
     */
    @PostMapping("/chquantity")
    public ResponseEntity<Object> changeQuantity(	@RequestBody Map<String, Object> body
    												, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long cartId = Long.valueOf(body.get("cart_id").toString());
            Integer quantity = Integer.valueOf(body.get("quantity").toString());

            cartService.changeQuantity(cartId, quantity);

            response.put("success", true);
        } catch (ClassCastException e) { // "loginMemberId" 의 타입이 잘못 저장된 경우
        	e.printStackTrace();
            response.put("success", false);
            response.put("message", "수량 변경 실패 : 로그인 세션정보가 올바르지 않습니다.");
          
        } catch(IllegalStateException e) {
        	e.printStackTrace();
        	response.put("success", false);
            response.put("message", "수량 변경 실패 : 로그인이 필요합니다.");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "수량 변경 실패");
            
        }
        return ResponseEntity.ok(response);
    }

    /** 옵션 + 수량 수정
     * ✅ 스키마 기준으로 "옵션 변경" = stock_id 변경
     * body: { cart_id, member_id, stock_id, quantity }
     */
    @PostMapping("/update-option")
    public ResponseEntity<Object> updateOption(	@RequestBody Map<String, Object> body
												, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long cartId = Long.valueOf(body.get("cart_id").toString());
            Long memberId = Long.valueOf(body.get("member_id").toString());
            Long newStockId = Long.valueOf(body.get("stock_id").toString());
            Integer quantity = Integer.valueOf(body.get("quantity").toString());

            cartService.updateStock(cartId, newStockId, quantity, memberId);

            response.put("success", true);
            
        } catch (ClassCastException e) { // "loginMemberId" 의 타입이 잘못 저장된 경우
        	e.printStackTrace();
            response.put("success", false);
            response.put("message", "옵션 변경 실패 : 로그인 세션정보가 올바르지 않습니다.");
          
        } catch(IllegalStateException e) {
        	e.printStackTrace();
        	response.put("success", false);
            response.put("message", "옵션 변경 실패 : 로그인이 필요합니다.");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "옵션 변경 실패");
            
        }
        return ResponseEntity.ok(response);
    }

    /** 선택 삭제
     * body: { member_id, cart_ids: [..] }
     */
    @PostMapping("/delete")
    public ResponseEntity<Object> deleteCarts(	@RequestBody Map<String, Object> body
												, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long memberId = Long.valueOf(body.get("member_id").toString());

            // JSON 배열을 그대로 List로 받음
            // type 오류 생길수 있으므로 주의할것.
            List<Object> rawIds = (List<Object>) body.get("cart_ids");

            // Object -> Long 변환
            List<Long> cartIds = rawIds.stream()
                    .map(x -> Long.valueOf(x.toString()))
                    .toList();

            cartService.deleteCarts(cartIds, memberId);

            response.put("success", true);
        } catch (ClassCastException e) { // "loginMemberId" 의 타입이 잘못 저장된 경우
        	e.printStackTrace();
            response.put("success", false);
            response.put("message", "삭제 실패 : 로그인 세션정보가 올바르지 않습니다.");
          
        } catch(IllegalStateException e) {
        	e.printStackTrace();
        	response.put("success", false);
            response.put("message", "삭제 실패 : 로그인이 필요합니다.");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "삭제 실패");
            
        }
        return ResponseEntity.ok(response);
    }
    
    
    /*******************************************/
    
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
    
    //***********************************************/
    private long getLoginMemberId(HttpSession session) {

        Object attr = session.getAttribute("loginMemberId");

        if (attr == null) {
            throw new IllegalStateException("로그인이 필요합니다. (세션 없음)");
        }

        if (!(attr instanceof Long)) {
            throw new ClassCastException("loginMemberId 타입이 Long이 아닙니다.");
        }

        return (Long) attr;
    }
}