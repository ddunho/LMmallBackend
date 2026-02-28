package com.lfmall.backend.user.sevice;

import java.util.Map;

public interface UserService {
	
	void insertProfile(Map<String, Object> map);
	Map<String, Boolean> selectUserData(Map<String, Object> map);
	Map<String, Boolean> checkUserLogin(Map<String, Object> map);
	Map<String, Object> saveSession(Map<String, Object> map);

}
