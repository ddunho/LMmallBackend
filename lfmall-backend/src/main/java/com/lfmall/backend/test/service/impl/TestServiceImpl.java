package com.lfmall.backend.test.service.impl;

import org.springframework.stereotype.Service;

import com.lfmall.backend.test.dto.TestDto;
import com.lfmall.backend.test.mapper.TestMapper;
import com.lfmall.backend.test.service.TestService;

@Service
public class TestServiceImpl implements TestService{
	private final TestMapper testMapper;

    public TestServiceImpl(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    @Override
    public TestDto checkDb() {
        return testMapper.selectHealth();
    }
}
