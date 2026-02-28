package com.lfmall.backend.user.sevice;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lfmall.backend.user.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private final PasswordEncoder passwordEncoder;

	
	// 생성자 주입
    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


	@Override
	public void insertProfile(Map<String, Object> userMap) {
		
		// 비밀번호 암호화
		String pwdEncoder = passwordEncoder.encode(userDto.getUserPassword());
		userDto.setUserPassword(pwdEncoder);
		
		userDao.insertProfile(userDto);
		
	}


	@Override
	public Map<String, Boolean> selectUserData(Map<String, Object> userMap) {
		
		Map<String, Boolean> booleanMap = new HashMap<>();
	    
	    if(userDto.getUserName() != null && !userDto.getUserName().isEmpty()) {
	        int nameCount = userDao.checkUserName(userDto.getUserName());
	        booleanMap.put("usernameExists", nameCount > 0);
	    } else {
	        booleanMap.put("usernameExists", false);
	    }
	    
	    if(userDto.getUserEmail() != null && !userDto.getUserEmail().isEmpty()) {
	        int emailCount = userDao.checkUserEmail(userDto.getUserEmail());
	        booleanMap.put("emailExists", emailCount > 0);
	    } else {
	        booleanMap.put("emailExists", false);
	    }
	    
	    return booleanMap;
	}


	@Override
	public Map<String, Boolean> checkUserLogin(Map<String, Object> userMap) {
		Map<String, Boolean> checkedResult = new HashMap<>();
		Map<String , Object> test = userDao.checkUserLogin(userDto);
		
		if (test == null){
			checkedResult.put("loginCheck", false);
			return checkedResult; 
		}
		
		String savedPassword = (String) test.get("USER_PASSWORD");
		
		System.out.println("savedPassword"+savedPassword);
		
		boolean checkPassword = passwordEncoder.matches(userDto.getUserPassword(), savedPassword); //받아온 pw와 DB에 저장된 암호화 Pw를 비교하는 se3curity문
		
		System.out.println("savedPassword 통과"+savedPassword);
		
		checkedResult.put("loginCheck", checkPassword);
		
		return checkedResult;	
	}
	
	public Map<String, Object> saveSession(Map<String, Object> userMap) {
		Map<String, Object> checkedResult = new HashMap<>();
		Map<String , Object> sessionUserDto = userDao.checkUserLogin(userDto);
		
		return sessionUserDto;
	}
}
