package com.lfmall.backend.member.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

	Map<String, Object> checkUserLogin(Map<String, String> memberMap);

	void insertProfile(Map<String, String> memberMap);

}
