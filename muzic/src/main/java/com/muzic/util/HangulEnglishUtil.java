package com.muzic.util;

import io.github.crizin.KoreanUtils;

public class HangulEnglishUtil {

    private HangulEnglishUtil() {}

    // 영어 -> 완성형한글
    public static String toHangul(String englishTyped) {
        if (englishTyped == null || englishTyped.isBlank()) return englishTyped;
        return KoreanUtils.convertEnglishTypedToKorean(englishTyped);
    }

    // 한글 -> 영어 사용 별로없음
    public static String toEnglish(String hangulTyped) {
        if (hangulTyped == null || hangulTyped.isBlank()) return hangulTyped;
        return KoreanUtils.convertKoreanTypedToEnglish(hangulTyped);
    }
    
    
    
}
