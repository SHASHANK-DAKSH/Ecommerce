package com.example.MainProject;

import com.example.MainProject.security.JwtGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainProjectApplicationTests {
	@Autowired
	JwtGenerator jwtGenerator;

	@Test
	void contextLoads() {
	}

	@Test
	void validate() {
		String token=jwtGenerator.generateTokenByEmail("nitnitn");
		System.out.println(token);
		System.out.println(jwtGenerator.getUserNameFromJwt(token));
		System.out.println(jwtGenerator.validateToken(token));
	}



}
