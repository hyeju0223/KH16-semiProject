package com.muzic.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

	@Autowired
	private LoginInterceptor loginInterceptor; //미로그인 차단 인터셉터
	@Autowired
	private AdminInterceptor adminInterceptor;
	@Autowired
	private PreventAdminInterceptor preventAdminInterceptor;
	@Autowired
	private PostOwnerInterceptor postOwnerInterceptor;

	
	@Override
	public void addInterceptors(InterceptorRegistry registry) { //인터셉터 등록메소드
		//registry.addInterceptor(인터셉터객체).addPathPatterns(적용시킬주소);
		
		//미로그인
		registry.addInterceptor(loginInterceptor)	
					.addPathPatterns("/mypage/**", "/post/**","/post/free/**", "/post/mbti/**", "/store/cart/**", "/admin/**")
					.excludePathPatterns("/post/free/list")
					.order(1);
		
//		관리자, 게시판 관련 일단 주석처리
//		//관리자용 인터셉터
//		registry.addInterceptor(adminInterceptor)
//					.addPathPatterns("/admin/**")
//					.order(2);
//		//관리자 상세/수정/삭제 금지 인터셉터
//		registry.addInterceptor(preventAdminInterceptor)
//					.addPathPatterns("/admin/member/detail", "/admin/member/drop")
//					.order(3);
//		
//		//자기자신(수정/삭제), 관리자(삭제) 허용 인터셉터
//		registry.addInterceptor(postOwnerInterceptor)
//					.addPathPatterns("/post/edit", "/post/delete")
//					.order(4);
	}
}
