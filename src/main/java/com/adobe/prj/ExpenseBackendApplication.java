package com.adobe.prj;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.adobe.prj.util.JwtUtil;

@SpringBootApplication
public class ExpenseBackendApplication {

	public static void main(String[] args) {
		JwtUtil.setStartDate(new Date());
		SpringApplication.run(ExpenseBackendApplication.class, args);
	}

}
