package com.lfmall.backend.user.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserDao {

	int insertProfile(Map<String, Object> map);
    int checkUserName(String userName);
    int checkUserEmail(String userEmail);
    Map<String, Object> checkUserLogin(Map<String, Object> map);

}
