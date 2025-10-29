package com.muzic.util;

public class SearchUtil {

    private SearchUtil() {} // 인스턴스화 방지

    // 원본 컬럼 허용 문자로만 이루어졌는지 판별(DB와 동일한 정규표현식)
    public static boolean isOriginalColumnInput(String input) {
        if (input == null || input.isBlank()) return false;
        String pattern = "^[가-힣0-9a-zA-Z ()!?.,#&*+=@\\$%'\"\\^:;/|<>₩~-]+$";
        return input.matches(pattern);
    }

    // 마지막 글자가 초성인지 판별
    public static boolean endsWithChosung(String input) {
        if (input == null || input.isEmpty()) return false;
        char last = input.charAt(input.length() - 1);
        return (last >= 0x3131 && last <= 0x314E);
    }
    
    // 한글(초성 포함) 문자가 전혀 없는지 판별
    public static boolean isWithoutHangul(String input) {
        if (input == null || input.isBlank()) return false;
        // 한글 완성형(가-힣) + 자모(ㄱ-ㅎ, ㅏ-ㅣ)가 하나라도 있으면 false
        return !input.matches(".*[가-힣ㄱ-ㅎㅏ-ㅣ].*");
    }
    
    // 자모 섞여있는지 판별
    public static boolean containsJamo(String input) {
        if (input == null) return false;
        for (char c : input.toCharArray()) {
            if ((c >= 'ㄱ' && c <= 'ㅎ') || (c >= 'ㅏ' && c <= 'ㅣ')) {
                return true; // 자음 또는 모음 포함
            }
        }
        return false;
    }
}