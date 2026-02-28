package com.lfmall.backend.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lfmall.backend.user.sevice.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@RequestMapping("/")
@Controller
public class UserController{
	
	@Autowired
	private UserService userService;

	//로그인 화면
	@GetMapping("/")
	public String login() {
		
		return "login/login";
	}
	
	@PostMapping("/checkLogin")
	public ResponseEntity<?> login(@RequestBody UserDto userDto,  HttpSession session) {
		System.out.println(userDto);
		// 이메일이 비밀번호 빈값으로 넘겨받았을 경우 
		if(userDto == null) {
			return null;
		}
		

		
		Map<String, Boolean> result = userService.checkUserLogin(userDto);

		// 정상적으로 로그인 했을경우 비밀번호 비교를 통해서 true 반환되었을경우에 session 데이터를 담아야해요
		if(result.get("loginCheck") == true ) { // ?? 
			Map<String, Object> sessionDto = userService.saveSession(userDto);
			session.setAttribute("userLogin", sessionDto);
			// result.put("success", true);
		}else {
			return null;
		}
		
		return ResponseEntity.ok(result);
	}
	
	//로그아웃
	@PostMapping("/main/logout")
	public ResponseEntity<Void> logoutSession(HttpServletRequest request){
		HttpSession session = request.getSession(false); // 세션이 존재하지 않을 경우, null 반환(새로 생성 x)
		
		if(session != null) {
			session.invalidate();//세션 존재할 경우 세션 삭제
		}
		
		return ResponseEntity.ok().build();
		
	}
	
	//회원가입 화면 도출용
	@GetMapping("/register")
	public String register() {
		
		return "login/membership";
	}

	//회원가입 정보 저장용
    @PostMapping("/register")
    public ResponseEntity<?> registerSubmit(@RequestBody UserDto userDto) { //userDto타입으로 값 받아옴
        // JSON 키 이름 = DTO 필드 이름 인 것에 자동으로 매핑됨

    	userService.insertProfile(userDto);
        
        return ResponseEntity.ok(userDto); //서버 성공여부 반환 (반환 시 userDto 데이터 같이 반환)
    }
    
    //회원가입 유효성 검사용 api
    @PostMapping("/register/check")
    public ResponseEntity<?> registerCheck(@RequestBody UserDto userDto){
    	Map<String, Boolean> result = userService.selectUserData(userDto);

    	
    	return ResponseEntity.ok(result);
    }
}
