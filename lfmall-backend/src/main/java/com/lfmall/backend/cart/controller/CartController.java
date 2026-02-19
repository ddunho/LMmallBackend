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

import com.lfmall.backend.cart.model.dto.CartDto;
import com.lfmall.backend.cart.model.service.CartService;

//import lombok.RequiredArgsConstructor;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
    private CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<Object> getCartsByMemberId(@RequestBody Map<String, Object> body) {

        // 1️⃣ 요청 body에서 member_id 추출
        Long memberId = Long.valueOf(body.get("member_id").toString());

        // 2️⃣ 서비스 호출
        List<CartDto> cartList = cartService.getCartsByMemberId(memberId);

        // 3️⃣ 응답 형식 맞춰서 반환
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", cartList);

        return ResponseEntity.ok(response);
    }
}