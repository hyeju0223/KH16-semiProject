package com.muzic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muzic.dao.MusicDao;
import com.muzic.dao.MusicLikeDao;
import com.muzic.dto.MusicLikeVO;

@Service
public class MusicLikeService {

    @Autowired
    private MusicLikeDao musicLikeDao;

    @Autowired
    private MusicDao musicDao;

     //좋아요 상태 조회
    public MusicLikeVO checkLikeStatus(String memberId, int musicNo) {
        boolean isLiked = musicLikeDao.isLiked(memberId, musicNo);
        int likeCount = musicDao.selectOne(musicNo).getMusicLike();

        return MusicLikeVO.builder()
                .like(isLiked)
                .likeCount(likeCount)
                .build();
    }

     // 좋아요 토글 (insert/delete + count 동기화)
    @Transactional
    public MusicLikeVO toggleLike(String memberId, int musicNo) {
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