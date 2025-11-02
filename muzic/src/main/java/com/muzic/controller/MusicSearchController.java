package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.muzic.condition.SearchCondition;
import com.muzic.service.MusicSearchService;
import com.muzic.vo.MusicSearchVO;

@Controller
@RequestMapping("/music/search")
public class MusicSearchController {

	@Autowired
	private MusicSearchService musicSearchService;

	@GetMapping("/list")
	public String list(@ModelAttribute SearchCondition searchCondition, Model model) {

		// 우선순위 설정
		List<String> columnPriority = List.of("music_title", "music_artist", "music_album");

		List<MusicSearchVO> musicUserVO = List.of(); // 검색 실행시 초기화용 리스트

		String selectedColumn = searchCondition.getColumn();

		if (selectedColumn != null) {
			musicUserVO = musicSearchService.searchByColumnOnly(searchCondition); // 결과가 없으면 그냥 null 반환
		} else {
			// 우선순위 탐색
			for (String column : columnPriority) {
				searchCondition.setColumn(column); // 순회하면서 컬럼 변경
				List<MusicSearchVO> tempList = musicSearchService.search(searchCondition);
				if (!tempList.isEmpty()) {
					musicUserVO = tempList;
					selectedColumn = column;
					break; // 결과 있으면 바로 중단
				}
			}
		}
		// JSP 전달
		model.addAttribute("searchCondition", searchCondition);
		model.addAttribute("musicUserVO", musicUserVO);
		model.addAttribute("selectedColumn", selectedColumn); // 직관적으로 반환
		model.addAttribute("pageURL", "/music/search/list");
		
		return "/WEB-INF/views/music/search/list.jsp";
	}
}