package com.lfmall.backend.cart.controller.devonly;

import java.io.Serializable;
//import java.net.http.HttpHeaders;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;




/**
 * 
 * ********************* 경고! ******************
 * 
 * dev환경용 가짜 로그인을 위한 컨트롤러. 
 * 
 * 임시로 session에 로그인 유저정보를 강제로 주입해서, 마치 로그인이 된것처럼 Spring단을 속이는 "개발 전용 Controller"임
 * 
 * 절대 Profile 어노테이션( @Profile({"local", "dev", "!prod"}) ) 을 수정하지 말것.
 * 
 * 로그인 기능이 개발완료 되면 본 파일(TempDevLoginController.java) 자체를 Delete하는걸 권장함.
 * 
 * 
 * 
 * */
@Profile({"local", "dev", "!prod"}) // ✅ 운영에서는 절대 활성화 안 되게 ( 경고 : 절대 수정금지 @Profile({"local", "dev", "!prod"}) )
@RestController // return 되는 값을 문자열 format으로 Http body에 그대로 써버림 
@RequestMapping("/dev")
public class TempDevLoginController {

    // ✅ 세션에 저장할 최소 로그인 정보(필요한 것만)
    public record LoginMember(Long memberId) implements Serializable {}
    /**
     * 	강제 로그인 함수.
     * 
     * ResponseEntity<Void> -> 응답 Body의 형식이 Void(= 아예 Body가 없다)이다.
     * */
    @GetMapping("/forceLogin")
    public ResponseEntity<Void> forceLogin(	@RequestParam(value = "memberId", defaultValue = "1") Long memberId,
            								HttpSession session) {
    	
    	
        if (!memberId.equals(1L) && !memberId.equals(2L)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        session.setAttribute("loginMemberId", memberId);
        /*return Map.of("success", true, "loginMemberId", session.getAttribute("loginMemberId"));*/
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/app/menu/0")
                .build(); // 리다이렉트.
        
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {
        Object login = session.getAttribute("loginMemberId");
        return Map.of("success", true, "loginMemberId", login);
    }

    @PostMapping("/forceLogout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        return Map.of("success", true);
    }
}