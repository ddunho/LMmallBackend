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
@RequestMapping("/api/order")
public class OrderController {
	

    // TODO: 서비스 주입 (나중에 OrderService 만들어서 여기 넣기)
    // private final OrderService orderService;
	
	
/**
 * 주문 결제가 완료된 후에 DB에 결제기록을 저장하는 함수.
 * @param requestData.order_id
 * @param requestData.member_id
 * @param requestData.orderInfo
 * @param requestData.orderCompleteItems
 * 
 * */
	 @PostMapping("/complete")
    public Map<String, Object> completeOrder(
            @RequestBody Map<String, Object> requestData
    ) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1️⃣ 값 추출
            String orderId = requestData.get("order_id").toString();
            Long memberId = Long.valueOf(requestData.get("member_id").toString());

            Map<String, Object> orderInfo =
                    (Map<String, Object>) requestData.get("orderinfo");

            List<Map<String, Object>> orderCompleteItems =
                    (List<Map<String, Object>>) requestData.get("orderCompleteItems");

            // 2️⃣ (추후) 서비스 호출
            // orderService.saveOrder(orderId, memberId, orderInfo, orderCompleteItems);

            System.out.println("orderId = " + orderId);
            System.out.println("memberId = " + memberId);
            System.out.println("orderInfo = " + orderInfo);
            System.out.println("items = " + orderCompleteItems);

            // 3️⃣ 성공 응답
            response.put("success", true);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "주문 저장 실패");
        }

        return response;
    }
}
