package com.lfmall.backend.test.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.lfmall.backend.test.dto.TestDto;

@Mapper
public interface TestMapper {
	TestDto selectHealth();
}
