package com.muzic.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muzic.condition.SearchCondition;
import com.muzic.dao.MusicSearchDao;
import com.muzic.util.HangulChosungUtil;
import com.muzic.util.HangulEnglishUtil;
import com.muzic.util.SearchUtil;
import com.muzic.vo.MusicSearchVO;

@Service
public class MusicSearchService {

    @Autowired
    private MusicSearchDao musicSearchDao;
    
    @Autowired
    private MusicHelperService musicHelperService;
    
    // 허용 정렬값
    private static final Set<String> ALLOWED_SORT =
            Set.of("latest", "like", "play", "accuracy");

    // 기본 컬럼 목록 (원본)
    private static final List<String> BASE_COLUMNS =
            List.of("music_title", "music_artist", "music_album");

    // 공통 전처리 (검색어, 정렬, 초성/자모 판별)
    private boolean prepareCondition(SearchCondition searchCondition) { // 논리값으로 반환 false면 검색안함(검색어가 비어있을 경우에만 false)
        String keyword = searchCondition.getKeyword();
        if (keyword == null) return false;

        // 1. 기본 정리: 공백 제거 + 소문자화
        keyword = keyword.replaceAll("\\s+", "").toLowerCase();
        if (keyword.isEmpty()) return false;
        searchCondition.setKeyword(keyword);

        // 2. 정렬 기준 검증 및 기본값 보정
        String sortType = searchCondition.getSortType();
        if (sortType == null || !ALLOWED_SORT.contains(sortType))
            searchCondition.setSortType("accuracy");

        // 3. 원본 컬럼 형태인지 확인 (DB와 동일한 정규식 기반)
        boolean isBaseColumn = SearchUtil.isOriginalColumnInput(keyword);

        // 4. 마지막 글자가 초성일 경우 보정 (예: "감ㅅ" → "감")
        if (!isBaseColumn && SearchUtil.endsWithChosung(keyword)) { // 원본컬럼 검색이 아니며 마지막 글자가 초성일 경우
            String trimmedLast = keyword.substring(0, keyword.length() - 1); // 마지막 글자를 자르고 저장
            if (SearchUtil.isOriginalColumnInput(trimmedLast)) { // 자른 글자가 원본컬럼 검색어가 맞다면
                keyword = trimmedLast;
                searchCondition.setKeyword(keyword); // 키워드를 컨디션에 저장 
                isBaseColumn = true; // 보정된 결과로 재평가(원본 컬럼 검색어가 맞다)
            }
        }

        // 5. 자모 포함 여부 판별 후 저장 (_search 컬럼 판단용)
        // 유효한 검색어가 아니면서 자모가 포함되어있다면 false(완성형 한글에는 자모가 무조건 있으므로 검색어가 원본컬럼의 검색어인지 판별도 필요)
        boolean hasJamo = !isBaseColumn && SearchUtil.containsJamo(keyword); 
        searchCondition.setMixedJamo(hasJamo); // 판정 결과로 저장(자모여부)

        // 6. 반환: 유효한 검색어일 경우 true
        return true;
    }

    // 통합 검색 (메인 검색)
    public List<MusicSearchVO> search(SearchCondition searchCondition) {
        if (!prepareCondition(searchCondition)) return List.of(); // 검색 실행여부 확인

        String keyword = searchCondition.getKeyword();
        String sortType = searchCondition.getSortType();
        boolean isBaseColumn = SearchUtil.isOriginalColumnInput(keyword); // 원본컬럼 입력인가?
        boolean hasJamo = searchCondition.isMixedJamo(); // 완성형 한글이 아니면서 자모만 따로 있는지 포함 여부

        // 1차 입력 그대로 검색
        for (String baseColumn : BASE_COLUMNS) {
            String column = (!isBaseColumn || hasJamo) ? baseColumn + "_search" : baseColumn; // 자모가 있으면 _search 컬럼
            searchCondition.setColumn(column); // 컬럼 확정

            String originalKeyword = searchCondition.getKeyword(); // 원본 키워드 저장
            if (!isBaseColumn || hasJamo) { // 원본컬럼이 아니면서 자모가 있다면
                searchCondition.setKeyword(HangulChosungUtil.getSearch(originalKeyword)); // 초성 변환 검색어 세팅
            }

            List<MusicSearchVO> result = performSearch(searchCondition, sortType); // 검색 실행
            searchCondition.setKeyword(originalKeyword); // 검색어 원복(3개의 컬럼을 순회해야하므로 원복 필요) // 원본 검색어가 뭔지 fe 표시할때 필요
            if (!result.isEmpty()) {
            	musicHelperService.setMusicAttachmentNo(result);
            	searchCondition.setAllData(musicSearchDao.countMusicSearchResults(searchCondition));
            	return result; // 결과 있으면 바로 반환
            }
        }

        // 2차 영타 입력 시 한글로 변환 후 재검색
        if (SearchUtil.isWithoutHangul(keyword)) { // 한글이 전혀 없는 경우
            String converted = HangulEnglishUtil.toHangul(keyword).replaceAll("\\s+", ""); // 한글 변환 시도
            if (!converted.equals(keyword)) { // 한글 변환이 실제로 이뤄졌다면
                searchCondition.setKeyword(converted); // 변환된 한글로 키워드 세팅
                boolean convertedBase = SearchUtil.isOriginalColumnInput(converted); // 변환된 키워드가 원본컬럼 검색어인지(완성형 한글인지)
                boolean convertedJamo = !convertedBase && SearchUtil.containsJamo(converted); // 완성형 한글이 아니면서 자모가 섞여있다면 true

                for (String baseColumn : BASE_COLUMNS) { // 탐색
                    String column = (!convertedBase || convertedJamo) ? baseColumn + "_search" : baseColumn; // 변환된 형태 기준 컬럼 선택
                    searchCondition.setColumn(column);

                    String originalKeyword = searchCondition.getKeyword(); // 원본 키워드 저장
                    if (!convertedBase || convertedJamo) { // 원본컬럼이 아니거나 자모가 섞여있가면(완성형 한글이 아닌 자음만 따로있다면)
                        searchCondition.setKeyword(HangulChosungUtil.getSearch(originalKeyword)); // 초성 변환
                    }

                    List<MusicSearchVO> result = performSearch(searchCondition, sortType);
                    searchCondition.setKeyword(originalKeyword); // 검색어 복원
                    if (!result.isEmpty()) { // 검색어 있을 때까지 컬럼 순회
                    	musicHelperService.setMusicAttachmentNo(result);
                    	searchCondition.setAllData(musicSearchDao.countMusicSearchResults(searchCondition));
                    	return result; // 결과 있으면 바로 반환
                    }
                }
            }
        }
        searchCondition.setAllData(0);
        return List.of(); // 여기까지 없다면 빈 리스트 반환
    }
    
    // 단일 컬럼 전용 검색 (미리보기 및 컬럼 지정 검색 실행)
    public List<MusicSearchVO> searchByColumnOnly(SearchCondition searchCondition) {
        if (!prepareCondition(searchCondition)) return List.of();
        if (!BASE_COLUMNS.contains(searchCondition.getColumn())) return List.of();
        	
        String keyword = searchCondition.getKeyword();
        String sortType = searchCondition.getSortType();
        boolean isBaseColumn = SearchUtil.isOriginalColumnInput(keyword);
        boolean hasJamo = searchCondition.isMixedJamo();

        // 단일 컬럼용 (컬럼 루프 없음)
        String column = searchCondition.getColumn();
        if (!isBaseColumn || hasJamo) { // 원본 컬럼이 아니거나 자모가 섞여있다면(searchCondition의 게터로 확인)
            column = column.endsWith("_search") ? column : column + "_search"; // 컬럼에 _search가 있을경우 유지 아닐경우 _search추가(중복 방지)
        }
        searchCondition.setColumn(column);

        String originalKeyword = searchCondition.getKeyword(); // 원본 키워드 저장
        if (!isBaseColumn || hasJamo || column.endsWith("_search")) { // 원본 컬럼 검색어가 아니거나! 완성형 한글이 아닌데 자모가 있거나! 컬럼명이 _search로 끝난다면!
            searchCondition.setKeyword(HangulChosungUtil.getSearch(originalKeyword)); // 초성 변환
        }

        List<MusicSearchVO> result = performSearch(searchCondition, sortType); // 탐색 순회 x(레스트 컨트롤러에서 순회 담당)
        searchCondition.setKeyword(originalKeyword); // 원본 키워드 복원
        if (!result.isEmpty()) {  // 빈 리스트가 아니라면 종료
        	musicHelperService.setMusicAttachmentNo(result);
        	searchCondition.setAllData(musicSearchDao.countMusicSearchResults(searchCondition));
        	return result;
        }

        // 2차 영타 → 한글 변환 재검색
        if (SearchUtil.isWithoutHangul(keyword)) { // 한글이 아예 없다면
            String converted = HangulEnglishUtil.toHangul(keyword).replaceAll("\\s+", ""); // 한글로 변환 시도 및 저장
            if (!converted.equals(keyword)) { // 한글로 변환됐다면
                searchCondition.setKeyword(converted); // 한글로 검색어 변경

                boolean convertedBase = SearchUtil.isOriginalColumnInput(converted); //변환된 한글이 원본 검색어라면(완성형 한글)
                column = searchCondition.getColumn(); // 검색 컬럼 저장 붙어있는 그대로 저장
                if (convertedBase && column.endsWith("_search")) { // 원본 컬럼 검색어이면서 _search로 끝난다면
                    column = column.replace("_search", ""); // _search를 지우고 원본 컬럼으로 변경
                } else if (!convertedBase && !column.endsWith("_search")) { // 원본컬럼 검색어가 아니면서 _search로 끝나지 않는다면
                    column = column + "_search"; // _search로 변경
                }
                searchCondition.setColumn(column); // 조회 컬럼 세팅

                if (!convertedBase || column.endsWith("_search")) { // 원본 컬럼 검색어가 아니거나 _search로 끝난다면
                    searchCondition.setKeyword(HangulChosungUtil.getSearch(converted)); // 한글로 변환 시도 된 검색어를 search 검색어로 변환
                }

                result = performSearch(searchCondition, sortType); // 검색 시작
                searchCondition.setKeyword(converted); // 변환된 검색어로 searchCondition 세팅(해당 코드 없어도 무관)

                if (!result.isEmpty()) { // 조회결과가 있다면 리스트 반환
                	musicHelperService.setMusicAttachmentNo(result);
                	searchCondition.setAllData(musicSearchDao.countMusicSearchResults(searchCondition));
                	return result;
                } 
            }
        }
        searchCondition.setAllData(0);
        return List.of(); // 없다면 빈 리스트 반환
    }


    // 실제 검색 수행 메소드 호출 (컬럼 기반 초성 여부 판별)
    private List<MusicSearchVO> performSearch(SearchCondition searchCondition, String sortType) {
        boolean isSearchColumn = searchCondition.getColumn().endsWith("_search"); // _search 컬럼이면 초성검색
        List<MusicSearchVO> result = runSearch(searchCondition, isSearchColumn, sortType);
        return (result == null) ? List.of() : result;
    }

    // DAO 호출 분기에 따른 검색 실행
    private List<MusicSearchVO> runSearch(SearchCondition searchCondition, boolean isSearchColumn, String sortType) {
        switch (sortType) {
            case "like":
                return isSearchColumn
                        ? musicSearchDao.searchByChosungLike(searchCondition)
                        : musicSearchDao.searchByLike(searchCondition);
            case "play":
                return isSearchColumn
                        ? musicSearchDao.searchByChosungPlay(searchCondition)
                        : musicSearchDao.searchByPlay(searchCondition);
            case "latest":
                return isSearchColumn
                        ? musicSearchDao.searchByChosungLatest(searchCondition)
                        : musicSearchDao.searchByLatest(searchCondition);
            case "accuracy":
            default:
                return isSearchColumn
                        ? musicSearchDao.searchByChosungAccuracy(searchCondition)
                        : musicSearchDao.searchByAccuracy(searchCondition);
        }
    }
}