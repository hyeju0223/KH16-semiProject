package com.muzic.aop;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice // 컨트롤러에 간섭하는 도구 // 프로젝트의 모든 컨트롤러(전역으로 설정)
public class EmptyStringControllerAdvice {

    //(+추가) 이 컨트롤러로 들어오는 empty String 은 이제부터 null이다
    @InitBinder // 컨트롤러가 미리 알아야 하는 내용을 알려주기 위한 메소드(전처리 메소드)
    public void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(데이터종류, 사용할도구);
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}