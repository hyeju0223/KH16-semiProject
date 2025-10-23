package com.muzic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.MusicDao;
import com.muzic.dao.MusicStatusHistoryDao;
import com.muzic.dto.MusicDto;
import com.muzic.dto.MusicFormDto;
import com.muzic.dto.MusicStatusHistoryDto;
import com.muzic.util.HangulChosungUtils;


@Service
public class MusicService {
	
	@Autowired
	private MusicDao musicDao;
	
	@Autowired
    private AttachmentService attachmentService;
	
	@Autowired
	private MusicStatusHistoryDao musicStatusHistoryDao;

    @Transactional
    public int registerMusic(MusicFormDto musicFormDto, String memberId, String memberRole) 
    		throws IOException { // int 반환 이유는 등록 후 혹시 방금 음원 등록한 글로 갈 수 있으니 등록한 음원 번호 반환(아마 관리자)
        
		List<String> musicGenres = new ArrayList<>(musicFormDto.getMusicGenreSet());
		
		boolean isAdmin = memberRole.equals("관리자");
		String musicStatus = isAdmin ? "APPROVED" : "PENDING";
    		
        int musicNo = musicDao.sequence();
        
        String titleChosung = HangulChosungUtils.getChosung(musicFormDto.getMusicTitle());
        String titleSearch = HangulChosungUtils.getSearch(musicFormDto.getMusicTitle());
        String artistChosung = HangulChosungUtils.getChosung(musicFormDto.getMusicArtist());
        String artistSearch = HangulChosungUtils.getSearch(musicFormDto.getMusicArtist());
        
        MusicDto musicDto = 
        		MusicDto.builder()
        		.musicNo(musicNo)
        		.musicTitle(musicFormDto.getMusicTitle())
        		.musicTitleChosung(titleChosung)
        		.musicArtist(musicFormDto.getMusicArtist())
        		.musicArtistChosung(artistChosung)
        		.musicTitleSearch(titleSearch)
        		.musicArtistSearch(artistSearch)
		        .musicAlbum(musicFormDto.getMusicAlbum())
		        .musicUploader(memberId)
		        .musicStatus(musicStatus)
		        .build();

        musicDao.insert(musicDto); 
        
        // 파일이 null일 경우 앞에 null 조건으로 확인을 해야 null이 들어왔을 때 false를 유도해서 바로 조건문을 false로 마감시킬 수 있음. 
        // 만약 isEmpty를 먼저 체크하면 null일 발생했을 경우 조건계산이 아닌 nullpointer예외가 발생함.
        // 프로필 이미지 저장 (첫 번째 파일)
        
        MultipartFile inputCover = musicFormDto.getCoverImage();
        if (inputCover != null && !inputCover.isEmpty()) {
            attachmentService.save(inputCover, "cover", musicNo);
             // category를 'music' 대신 'cover'로 분리.
        }
        
       MultipartFile inputMusic = musicFormDto.getMusicFile();
        // 음악 파일 저장 (두 번째 파일)
        if (inputMusic != null && !inputMusic.isEmpty()) {
             attachmentService.save(inputMusic, "music", musicNo);
        }
        
        // 장르 매핑 INSERT (music_genre_map 테이블)
        musicDao.insertMusicGenres(musicNo, musicGenres);
         
         // history INSERT
        String adminId = isAdmin ? memberId : null;
        String historyComment = isAdmin ? "승인완료" : "등록신청"; 
        MusicStatusHistoryDto musicStatusHistoryDto = 
        		MusicStatusHistoryDto
        		.builder()
        		.mshMusicNo(musicNo)
        		.mshMusicStatus(musicStatus)
        		.mshComment(historyComment)
        		.mshAdminId(adminId)
        		.build();
        
        musicStatusHistoryDao.insert(musicStatusHistoryDto);
        
        return musicNo;
    }
    
}
