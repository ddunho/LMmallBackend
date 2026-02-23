package com.lfmall.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lfmall.backend.product.mapper")
public class LfmallBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LfmallBackendApplication.class, args);
	}

}
