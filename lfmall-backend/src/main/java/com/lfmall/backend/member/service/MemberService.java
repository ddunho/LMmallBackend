package com.lfmall.backend.member.service;

import java.util.Map;

public interface MemberService {
	Map<String, Boolean> checkUserLogin(Map<String, String> memberMap);

	Map<String, Object> saveSession(Map<String, String> memberMap);

	void insertProfile(Map<String, String> memberMap);
}
