package com.muzic.util;

import com.muzic.error.NotKoreanException;

public class HangulChosungUtils {

	private HangulChosungUtils() {} // 인스턴스화 방지
	
	private static final char[] CHOSUNG = {
	        'ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ','ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'
	    };	// 상수 필드로 초성 저장

	public static String getChosungOrKeep(String input) {
	    if (input == null || input.isEmpty()) {
	        return "";
	    }

	    StringBuilder sb = new StringBuilder();
	    for (char c : input.toCharArray()) {
	        if (c >= '가' && c <= '힣') { // 한글이면 초성
	            int index = (c - 0xAC00) / (21 * 28);
	            sb.append(CHOSUNG[index]);
	        } else { // 한글이 아니면 그대로
	            sb.append(c);
	        }
	    }

	    return sb.toString();
	}
	
		// 한글'만' 초성 처리하는 유틸
//	    public static String getChosung(String input) {
//	    	if (input == null || input.isEmpty()) {
//	            throw new NotKoreanException("입력값이 비어있습니다.");
//	    	}	// 입력값이 없을 때의 예외처리
//	    	
//	    	StringBuilder sb = new StringBuilder();
//	        for (char c : input.toCharArray()) { // 입력값을 
//	            if (c < '가' || c > '힣') {
//	                throw new NotKoreanException("한글만 입력 가능합니다: " + c);
//	            }
//	            int index = (c - 0xAC00) / (21 * 28);
//	            sb.append(CHOSUNG[index]);
//	        }
//	        
//	        return sb.toString();
//	    }
	    
}
