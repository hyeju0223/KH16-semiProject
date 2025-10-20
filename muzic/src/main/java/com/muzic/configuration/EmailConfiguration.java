package com.muzic.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//설정파일을 이용하면 내가자주 쓰는 도구를 등록할수있음.
@Configuration
public class EmailConfiguration {
	 	//스프링에서는 서버가 시작되면 @Bean을 자동으로 등록하도록 설계되어 있다
		@Bean
		
		public JavaMailSenderImpl sender() {
			
			//메일 발송 도구 생성
			JavaMailSenderImpl sender=new JavaMailSenderImpl();
			
		
			
			//서비스 제공자 정보 설정
			sender.setHost("smtp.gmail.com");//이용할 업체의 호스트번호
			sender.setPort(587);//이용할 업체의 포트번호
			sender.setUsername("dlalstn85451569"); //이용할 업체의 사용자 계정이름
			sender.setPassword("crop myer bylh htmq");
			
			Properties props=new Properties();
			props.setProperty("mail.smtp.auth","true");//이메일 발송에 인증을 사용(무조건 true)
			props.setProperty("mail.smtp.debug", "true");//이메일 발송과정을 자세하게 출력(오류 해결용)
			props.setProperty("mail.smtp.starttls.enable", "true");//StarTTLS 사용(보안용 통신방식)
			props.setProperty("mail.smtp.ssl.protocols","TLSv1.2");//TLS방식의 버전선택
			props.setProperty("mail.smtp.ssl.trust","smtp.gmail.com");//신뢰할수있는인증서발급자지정
			sender.setJavaMailProperties(props);
			
			return sender;
		}
}
