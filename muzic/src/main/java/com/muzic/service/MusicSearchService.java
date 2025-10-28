package com.muzic.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muzic.condition.SearchCondition;
import com.muzic.dao.MusicSearchDao;
import com.muzic.util.HangulChosungUtil;
import com.muzic.util.HangulEnglishUtil;
import com.muzic.vo.MusicUserVO;

@Service
public class MusicSearchService {
    
	@Autowired
    private MusicSearchDao musicSearchDao;
	
	@Autowired
	private MusicService musicService;
	
	private static final Set<String> ALLOWED_SORT = 
			Set.of("latest", "like", "play", "");
	
	private static final Set<String> ALLOWED_COLUMN = 
			Set.of("music_title", "music_artist", 
				    "music_title_search", "music_artist_search", 
				    "music_title_chosung", "music_artist_chosung");
	
//	public List<MusicUserVO> search(SearchCondition searchCondition) {
//		searchCondition.get
//	    return findMusicByCondition(searchCondition, false);
//	}
//
//	public List<MusicUserVO> searchByChosung(SearchCondition searchCondition) {
//	    if (!searchCondition.isChosung()) return findMusicByCondition(searchCondition, false);
//	    return findMusicByCondition(searchCondition, true);
//	}
	

	private List<MusicUserVO> findMusicByCondition(SearchCondition searchCondition) {
	    if (!searchCondition.isSearch())
	        return musicService.findUserMusicList(searchCondition);
	    if (!ALLOWED_COLUMN.contains(searchCondition.getColumn()))
	        return List.of();
	    
	    boolean chosung = isChosng(searchCondition);
	    String originKeyword = searchCondition.getKeyword();
	    
	    if(chosung) searchCondition.setKeyword(HangulChosungUtil.getSearch(originKeyword));
	    
	    List<MusicUserVO> list;
	    
	    if (searchCondition.getSortType() == null || !ALLOWED_SORT.contains(searchCondition.getSortType()))
	        list = chosung ? musicSearchDao.searchByChosungAccuracy(searchCondition)
	                       : musicSearchDao.searchByAccuracy(searchCondition);
	    switch (searchCondition.getSortType()) {
	        case "like":
	           list = chosung 
	           		? musicSearchDao.searchByChosungLike(searchCondition)
	                : musicSearchDao.searchByLike(searchCondition);
	           break;
	        case "play":
	        	list = chosung 
	        		? musicSearchDao.searchByChosungPlay(searchCondition)
	                : musicSearchDao.searchByPlay(searchCondition);
	        	break;
	        case "latest":
	        	list = chosung 
	        		? musicSearchDao.searchByChosungLatest(searchCondition)
	                : musicSearchDao.searchByLatest(searchCondition);
	        	break;
	        default:
	            list = chosung 
	            	? musicSearchDao.searchByChosungAccuracy(searchCondition)
	                : musicSearchDao.searchByAccuracy(searchCondition);
	            break;
	    }
	    if((list == null || list.isEmpty()) && !originKeyword.equals(searchCondition.getKeyword())) {
	    	String converted = HangulEnglishUtil.toHangul(originKeyword);
	        if (!converted.equals(originKeyword)) {
	            searchCondition.setKeyword(converted);
	            return findMusicByCondition(searchCondition);
	        }
	    }
	    	return list;
	}
	
	 private boolean isChosng(SearchCondition searchCondition) {
		return HangulChosungUtil.isChosung(searchCondition.getKeyword()); 
	 }
	 
}
	
//    public List<MusicUserVO> search(SearchCondition searchCondition) {
//    	if(!searchCondition.isSearch())
//    		return musicService.findUserMusicList(searchCondition);
//    	if(!ALLOWED_COLUMN.contains(searchCondition.getColumn()))
//    		return List.of();
//        if(searchCondition.getSortType() == null || !ALLOWED_SORT.contains(searchCondition.getSortType()))
//        	return musicSearchDao.searchByAccuracy(searchCondition);
//
//        switch (searchCondition.getSortType()) {
//            case "like":
//                return musicSearchDao.searchByLike(searchCondition);
//            case "play":
//                return musicSearchDao.searchByPlay(searchCondition);
//            case "latest":
//                return musicSearchDao.searchByLatest(searchCondition);
//            default:
//                return musicSearchDao.searchByAccuracy(searchCondition);
//        }
//    }
//    
//    public List<MusicUserVO> searchByChosung(SearchCondition searchCondition) {
//    	if(!searchCondition.isChosung())
//    		return search(searchCondition);
//    	if(!ALLOWED_COLUMN.contains(searchCondition.getColumn()))
//    		return List.of();
//        if(searchCondition.getSortType() == null || !ALLOWED_SORT.contains(searchCondition.getSortType()))
//        	return musicSearchDao.searchByChosungAccuracy(searchCondition);
//
//        switch (searchCondition.getSortType()) {
//            case "like":
//                return musicSearchDao.searchByChosungLike(searchCondition);
//            case "play":
//                return musicSearchDao.searchByChosungPlay(searchCondition);
//            case "latest":
//                return musicSearchDao.searchByChosungLatest(searchCondition);
//            default:
//                return musicSearchDao.searchByChosungAccuracy(searchCondition);
//        }
//    }
