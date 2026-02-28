package com.lfmall.backend.member.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfmall.backend.member.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberMapper memberMapper;
	
	public Map<String, Boolean> checkUserLogin(Map<String, String> memberMap) {
		Map<String, Boolean> checkedResult = new HashMap<>();
		Map<String , Object> test = memberMapper.checkUserLogin(memberMap);
		
		if (test == null){
			checkedResult.put("loginCheck", false);
			return checkedResult; 
		}
		
		String savedPassword = (String) test.get("member_password");//DB에서 아이디에 맞는 member_password 가져옴
		
		boolean checkPassword = memberMap.get("member_password").equals(savedPassword) ; //비번 맞는지 체크
		
		checkedResult.put("loginCheck", checkPassword);
		
		return checkedResult;
	}

	@Override
	public Map<String, Object> saveSession(Map<String, String> memberMap) {
		Map<String , Object> sessionUserLogin = memberMapper.checkUserLogin(memberMap);
		
		Map<String, Object> sessionData = new HashMap<>();
	    sessionData.put("nickname", sessionUserLogin.get("member_name")); // 프론트 member?.nickname과 매칭
	    sessionData.put("memberId", sessionUserLogin.get("member_id"));
		
		return sessionData;
	}

	@Override
	public void insertProfile(Map<String, String> memberMap) {
		// TODO Auto-generated method stub
	    Map<String, String> convertedMap = new HashMap<>();
	    convertedMap.put("member_login_id", memberMap.get("loginid"));
	    convertedMap.put("member_password", memberMap.get("loginpw"));
	    convertedMap.put("member_email", memberMap.get("email"));
	    convertedMap.put("member_name", memberMap.get("name"));
	    convertedMap.put("member_phone", memberMap.get("phone"));
	    convertedMap.put("member_birth", memberMap.get("birth"));
	    convertedMap.put("member_address", memberMap.get("address"));
	    convertedMap.put("member_address_detail", memberMap.get("addressDetail"));
	    convertedMap.put("member_address_number", memberMap.get("addressNumber"));

	    memberMapper.insertProfile(convertedMap);

	}

}
