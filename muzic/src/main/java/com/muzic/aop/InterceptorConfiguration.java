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
	@Autowired
	private PostReadInterceptor postReadInterceptor;
	@Autowired
	private BlackListInterceptor blackListInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) { //인터셉터 등록메소드
		//registry.addInterceptor(인터셉터객체).addPathPatterns(적용시킬주소);
		
				//블랙리스트 검사 (가장 먼저 실행)
				registry.addInterceptor(blackListInterceptor)
		                    .addPathPatterns("/**") // 모든 주소에 적용
		                    // 정적 자원과 에러 페이지는 제외해야 무한 루프나 오류가 발생하지 않음
		                    .excludePathPatterns("/css/**", "/js/**", "/images/**", "/error/**") 
							.order(1);

		        //미로그인 차단 인터셉터
				registry.addInterceptor(loginInterceptor)	
							.addPathPatterns("/mypage/**", "/post/**","/post/free/**", "/post/mbti/**", "/store/cart/**", "/admin/**", "/music/**", "/event/**")
							.excludePathPatterns("/post/free/list","/music/list","/music/detail","/rest/music/**","/music/search/**")
							.order(2);

				//조회수 중복 방지
				registry.addInterceptor(postReadInterceptor)
						.addPathPatterns("/post/detail")
						.order(3);
				
		        //관리자용 인터셉터
				registry.addInterceptor(adminInterceptor)
							.addPathPatterns("/admin/**")
							.order(4);
		        
		        //관리자 상세/수정/삭제 금지 인터셉터
				registry.addInterceptor(preventAdminInterceptor)
							.addPathPatterns("/admin/member/detail", "/admin/member/drop")
							.order(5);
				
		        //자기자신(수정/삭제), 관리자(삭제) 허용 인터셉터
				registry.addInterceptor(postOwnerInterceptor)
							.addPathPatterns("/post/edit", "/post/delete")
							.order(6);
			}
}
