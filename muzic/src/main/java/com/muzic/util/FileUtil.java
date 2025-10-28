package com.muzic.util;

import com.muzic.error.InvalidContentException;

public class FileUtil {
	
	private FileUtil() {};

    public static String getCleanFileName(String originalName) {
        if (originalName == null || originalName.trim().isEmpty()) throw new InvalidContentException("원본 파일 이름은 null이거나 비어있을 수 없습니다.");
        
        // 1. 앞뒤 공백 제거
        String cleanName = originalName.trim();
        
        // 2. 파일 시스템 경로 구분자(\, /)를 밑줄(_)로 치환하여 경로 조작을 방지
        cleanName = cleanName.replaceAll("[\\\\/]", "_");
        
        return cleanName;
    }
}