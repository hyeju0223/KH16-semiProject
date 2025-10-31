package com.muzic.restcontroller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.condition.SearchCondition;
import com.muzic.service.MusicSearchService;
import com.muzic.vo.MusicSearchVO;

@RestController
@RequestMapping("/music/search")
public class MusicSearchRestController {

    @Autowired
    private MusicSearchService musicSearchService;

    // 컬럼별로 preview 결과 (제목 / 가수 / 앨범) 각각 반환
    // 미리보기당 최대 3개 결과 반환
    @GetMapping("/preview")
    public Map<String, List<MusicSearchVO>> preview(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "accuracy") String sort) {
    	
    	// 입력순서 유지하기 위해서 링크드해시맵 사용
        Map<String, List<MusicSearchVO>> result = new LinkedHashMap<>();

        // 검색 대상 컬럼
        List<String> columns = List.of("music_title", "music_artist", "music_album");

        // 각 컬럼별로 3개씩 검색
        for (String column : columns) {
            SearchCondition condition = new SearchCondition();
            condition.setKeyword(keyword);
            condition.setSortType(sort);
            condition.setColumn(column);
            condition.setPage(1);
            condition.setSize(3); // 미리보기용

            List<MusicSearchVO> list = musicSearchService.searchByColumnOnly(condition);
            // 결과가 존재하는 컬럼만 Map에 추가
            if (list != null && !list.isEmpty()) {
                result.put(column, list);
            }
        }
        return result;
    }
}