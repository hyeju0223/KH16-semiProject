package com.muzic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muzic.condition.SearchCondition;
import com.muzic.dao.MusicDao;
import com.muzic.dao.MusicViewDao;
import com.muzic.domain.MemberRole;
import com.muzic.domain.MusicStatus;
import com.muzic.dto.MusicDto;
import com.muzic.dto.MusicFormDto;
import com.muzic.error.AlreadyRequestedException;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.NotOwnerException;
import com.muzic.error.OperationFailedException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.vo.MusicUserVO;


@Service
public class MusicService {
	
	@Autowired
	private MusicDao musicDao;
	
	@Autowired
	private MusicViewDao musicViewDao;
	
	@Autowired
	private MusicHelperService musicHelperService;
	
	private static final Set<String> ALLOWED_SORT = 
			Set.of("latest", "like", "play", "");
	
	private static final Set<String> ALLOWED_COLUMN = 
			Set.of("music_album", "music_title_search", "music_artist_search");
	
	// 음원 등록
    @Transactional
    public int registerMusic(MusicFormDto musicFormDto, String memberId, String memberRole) 
    		throws IOException { // int 반환 이유는 등록 후 혹시 방금 음원 등록한 글로 갈 수 있으니 등록한 음원 번호 반환(아마 관리자)
    	
    	musicHelperService.validateGenres(musicFormDto.getMusicGenreSet());
    	musicHelperService.validateMusicFile(musicFormDto.getMusicFile());

        boolean isAdmin = memberRole.equals(MemberRole.ADMIN.getRoleName());
        String musicStatus = isAdmin ? MusicStatus.APPROVED.getStatusName() : MusicStatus.PENDING.getStatusName();
        String comment = isAdmin ? "등록 승인완료" : "등록 승인대기";
        int musicNo = musicDao.sequence();

        MusicDto musicDto = musicHelperService.buildMusicDto(musicFormDto, musicNo, memberId, musicStatus);
        musicDao.insert(musicDto);

        musicHelperService.saveAttachments(musicFormDto, musicNo);
        // 장르 매핑 INSERT (music_genre_map 테이블)
        musicDao.insertMusicGenres(musicNo, new ArrayList<>(musicFormDto.getMusicGenreSet()));
        musicHelperService.insertHistory(musicNo, musicStatus, memberId, comment, isAdmin);

        return musicNo;
    }
    
    // 음원 삭제
    @Transactional
    public void deleteMusic(String memberRole, int musicNo) {
    	if(!memberRole.equals(MemberRole.ADMIN.getRoleName()))
    		throw new NeedPermissionException("음원 삭제는 관리자만 가능합니다.");
    	MusicDto musicDto = musicDao.selectOne(musicNo);
    	if(musicDto == null) throw new TargetNotFoundException("존재하지 않는 음원 입니다.");
    	musicHelperService.deleteAttachments(musicNo);
    	musicDao.delete(musicNo);
    }
    
    // 음원 삭제요청(사용자)
    public void requestDeleteMusic(String memberId, int musicNo) {
    	MusicDto musicDto = musicDao.selectOne(musicNo);
    	if (musicDto == null)
    	    throw new TargetNotFoundException("존재하지 않는 음원입니다.");
    	if(!musicDto.getMusicUploader().equals(memberId))
    		throw new NotOwnerException("자신의 음원만 삭제요청을 할 수 있습니다.");
    	if(musicDto.getMusicStatus().equals(MusicStatus.DELETE_REQUEST.getStatusName()))
    		throw new AlreadyRequestedException("이미 삭제요청된 음원입니다.");
    	musicDao.updateMusicStatus(musicNo, MusicStatus.DELETE_REQUEST.getStatusName());
    	musicHelperService.insertHistory(musicNo, MusicStatus.DELETE_REQUEST.getStatusName(), memberId, "삭제 요청", false);
    }
    
    // 음원 수정
    @Transactional
    public void updateMusic(MusicFormDto musicFormDto, String memberId, String memberRole, int musicNo) 
    		throws IOException {
    	MusicDto originMusicDto = musicDao.selectOne(musicNo);
    	if(originMusicDto == null)
    		throw new TargetNotFoundException("존재하지 않는 음원입니다.");
    	
    	boolean isAdmin = memberRole.equals(MemberRole.ADMIN.getRoleName());
        String musicStatus = isAdmin ? MusicStatus.APPROVED.getStatusName() : MusicStatus.PENDING.getStatusName();
        String comment = isAdmin ? "수정 승인완료" : "수정 승인대기";
        
    	if(!isAdmin && !originMusicDto.getMusicUploader().equals(memberId))
    		throw new NeedPermissionException("음원을 수정 할 권한이 없습니다.");
    	musicHelperService.validateGenres(musicFormDto.getMusicGenreSet());
    	
    	MusicDto musicDto = musicHelperService.buildMusicDto(musicFormDto, musicNo, memberId, musicStatus);
    	if(!musicDao.update(musicDto))
    	 throw new OperationFailedException("음원 정보 수정에 실패했습니다.");
    	
    	
    	musicHelperService.changeAttachments(musicFormDto, musicNo);
    	musicDao.deleteMusicGenres(musicNo);
        musicDao.insertMusicGenres(musicNo, new ArrayList<>(musicFormDto.getMusicGenreSet()));
        musicHelperService.insertHistory(musicNo, musicStatus, memberId, comment, isAdmin);
    }
    
    // 음원 목록
    public List<MusicUserVO> findUserMusicList(SearchCondition searchCondition){
    	String sort = searchCondition.getSortType();
		
    	if (sort == null) sort = "latest";

		if(searchCondition.isList() && ALLOWED_SORT.contains(sort)) {
			switch(sort) {
			case "latest" :
				return musicViewDao.selectPagingByLatest(searchCondition);
			case "like" : 
				return musicViewDao.selectPagingByLike(searchCondition);
			case "play" :
				return musicViewDao.selectPagingByPlay(searchCondition);
			case "title" :
				return musicViewDao.selectPagingByTitle(searchCondition);
			default :
				return musicViewDao.selectPagingByLatest(searchCondition);
			}
		} 
		return List.of();
    }
    
    public MusicDto selectOneMusicDto(int musicNo) {
    	MusicDto musicDto = musicDao.selectOne(musicNo);
    	if(musicDto == null) throw new TargetNotFoundException("존재하지 않는 음원입니다.");
    	return musicDto;
    }
}
    
