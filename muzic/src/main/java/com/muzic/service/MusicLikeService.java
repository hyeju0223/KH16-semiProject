package com.muzic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muzic.dao.MemberDao;
import com.muzic.dao.MusicDao;
import com.muzic.dao.MusicLikeDao;
import com.muzic.dto.MemberDto;
import com.muzic.dto.MusicDto;
import com.muzic.dto.MusicLikeVO;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;

@Service
public class MusicLikeService {

	@Autowired
	private MemberDao memberDao;
	
    @Autowired
    private MusicLikeDao musicLikeDao;

    @Autowired
    private MusicDao musicDao;

     //좋아요 상태 조회
    public MusicLikeVO checkLikeStatus(String memberId, int musicNo) {
    	MemberDto memberDto = memberDao.selectOne(memberId);
    	if(memberDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다.");
    	MusicDto musicDto = musicDao.selectOne(musicNo);
    	if(musicDto == null) throw new TargetNotFoundException("존재하지 않는 음원입니다.");
    	
        boolean isLiked = musicLikeDao.isLiked(memberId, musicNo);
        int likeCount = musicDto.getMusicLike();

        return MusicLikeVO.builder()
                .like(isLiked)
                .likeCount(likeCount)
                .build();
    }

     // 좋아요 토글 (insert/delete + count 동기화)
    @Transactional
    public MusicLikeVO toggleLike(String memberId, int musicNo) {
    	
    	MemberDto memberDto = memberDao.selectOne(memberId);
    	if(memberDto == null) throw new NeedPermissionException("회원만 좋아요를 누를 수 있습니다.");
    	
    	MusicDto musicDto = musicDao.selectOne(musicNo);
        if(musicDto == null) throw new TargetNotFoundException("존재하지 않는 음원입니다");
        
        MusicLikeVO musicLikeVO = new MusicLikeVO();

        // 이미 좋아요 눌렀으면 취소
        if (musicLikeDao.isLiked(memberId, musicNo)) {
            musicLikeDao.delete(memberId, musicNo);
            musicLikeVO.setLike(false);
        } else { // 안 눌렀으면 등록
            musicLikeDao.insert(musicNo, memberId, "Y");
            musicLikeVO.setLike(true);
        }

        // 좋아요 수 갱신 및 최신값 반영
        musicDao.updateLikeCount(musicNo);
        musicLikeVO.setLikeCount(musicDao.selectOne(musicNo).getMusicLike());

        return musicLikeVO;
    }
}