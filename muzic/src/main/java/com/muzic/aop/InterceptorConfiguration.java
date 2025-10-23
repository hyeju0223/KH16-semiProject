package com.muzic.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class InterceptorConfiguration implements WebMvcConfigurer {

	@Autowired
	private LoginInterceptor loginInterceptor; //미로그인 차단 인터셉터

	
	@Override
	public void addInterceptors(InterceptorRegistry registry) { //인터셉터 등록메소드
		//registry.addInterceptor(인터셉터객체).addPathPatterns(적용시킬주소);
		
		//미로그인
		registry.addInterceptor(loginInterceptor)	
					.addPathPatterns("/mypage/**");
		
	}
	
}
