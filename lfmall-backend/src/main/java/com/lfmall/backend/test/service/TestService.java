package com.lfmall.backend.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfmall.backend.test.dto.TestDto;
import com.lfmall.backend.test.mapper.TestMapper;

public interface TestService {
   /**
    * TEST 
    * 
    * @author 홍길동
    * @since 2025-02-10
    */
	TestDto checkDb();
}
