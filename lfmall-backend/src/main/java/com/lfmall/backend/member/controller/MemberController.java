package com.lfmall.backend.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lfmall.backend.member.service.MemberService;

@RequestMapping("/api")
@RestController //리액트에서 json응답용
public class MemberController {
	
	@Autowired
	private MemberService memberservice;
	
	//로그인
	@PostMapping("/member/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> memberMap) {
		
		

		// 이메일이 비밀번호 빈값으로 넘겨받았을 경우 
		if(memberMap == null || memberMap.isEmpty()) {
//	        return ResponseEntity.badRequest().body(Map.of("message", "잘못된 요청"));
			return ResponseEntity.badRequest().body(Map.of(
		            "success", false,
		            "message", "잘못된 요청"
		        ));
	    }
		

		Map<String, String> convertedMap = new HashMap<>();
		convertedMap.put("member_login_id", memberMap.get("loginid"));
		convertedMap.put("member_password", memberMap.get("loginpw"));
		
		Map<String, Boolean> result = memberservice.checkUserLogin(convertedMap);

		// 정상적으로 로그인 했을경우 비밀번호 비교를 통해서 true 반환되었을경우에 session 데이터 담기
		if(result.get("loginCheck") == true ) {
			Map<String, Object> sessionMap = memberservice.saveSession(convertedMap);
			
			return ResponseEntity.ok(Map.of(
		            "success", true,
		            "message", "로그인 성공",
		            "member", sessionMap
		   ));
		}else {
			return ResponseEntity.status(401).body(Map.of("loginCheck", false));
		}
	}
	
	
	//회원가입
	 @PostMapping("/signup")
    public ResponseEntity<?> registerSubmit(@RequestBody Map<String, String> memberMap) { //userDto타입으로 값 받아옴
        // JSON 키 이름 = DTO 필드 이름 인 것에 자동으로 매핑됨

		 memberservice.insertProfile(memberMap);
        
		 return ResponseEntity.ok(Map.of(
		            "success", true,
		            "message", "로그인 성공"
		   ));
    }
    
	
	

} 
