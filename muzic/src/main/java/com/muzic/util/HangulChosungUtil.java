package com.muzic.util;

public class HangulChosungUtil {

	private HangulChosungUtil() {} // 인스턴스화 방지
	
	private static final char[] CHOSUNG = {
	        'ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ','ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'
	    };	// 상수 필드로 초성 저장

	// 문자열에서 한글과 공백만 초성 처리하는 유틸
    public static String getChosung(String input) {
    	if (input == null || input.isEmpty()) {
            return "";
    	}
    	
    	StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            
           if (c >= '가' && c <= '힣') { // 한글일 경우 추가
                int index = (c - 0xAC00) / (21 * 28);
                sb.append(CHOSUNG[index]);
            } else if (c == ' ') {
                sb.append(c); // 공백은 그대로 추가
            }
        }
        
        return sb.toString();
    }
	
    // 특수문자 및 영어 등 한글이 아니더라도 한글은 초성변환하고 나머지는 저장하는 유틸
	public static String getSearch(String input) {
	    if (input == null || input.isEmpty()) {
	        return "";
	    }

	    StringBuilder sb = new StringBuilder();
	    for (char c : input.toCharArray()) {
	        if (c >= '가' && c <= '힣') { // 한글이면 초성
	            int index = (c - 0xAC00) / (21 * 28);
	            sb.append(CHOSUNG[index]);
	        } else if (c == ' ') {
	            continue;
	        }	
	        else { // 한글이 아니면 그대로
	            sb.append(c);
	        }
	    }

	    return sb.toString().toLowerCase().replaceAll("\\s+", ""); // 공백 2차 방어
	}
	
//	// 한글여부 판별
//	public static boolean isChosung(String keyword) {
//	    //ㄱ~ ㅎ이 섞여있는지
//	    for (char c : keyword.toCharArray()) {
//	        if (c >= 0x3131 && c <= 0x314E) {
//	            return true;
//	        }
//	    }
//	    return false;
//	}
	
}