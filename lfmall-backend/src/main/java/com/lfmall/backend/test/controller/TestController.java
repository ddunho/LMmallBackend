package com.lfmall.backend.test.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lfmall.backend.test.dto.TestDto;
import com.lfmall.backend.test.service.TestService;


@RestController
public class TestController {
	private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/api/health/db")
    public TestDto dbHealthCheck() {
        return testService.checkDb();
    }
}
