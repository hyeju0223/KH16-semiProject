package com.muzic.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.MusicDao;
import com.muzic.dto.MusicDto;
import com.muzic.util.HangulChosungUtils;


@Service
public class MusicService {
	
	@Autowired
	private MusicDao musicDao;
	
	@Autowired
    private AttachmentService attachmentService;

    @Transactional
    public int registerMusic(MusicDto musicDto, List<MultipartFile> attaches, List<String> musicGenres, 
    		String memberNickname, String memberRole) 
    		throws IOException { // int 반환 이유는 등록 후 혹시 방금 음원 등록한 글로 갈 수 있으니 등록한 음원 번호 반환
        
    	String musicStatus;
    	
    	musicStatus = memberRole.equals("관리자") ? "APPROVED" : "PENDING"; 
    		
        int musicNo = musicDao.sequence();
        
        String titleChosung = HangulChosungUtils.getChosung(musicDto.getMusicTitle());
        String titleSearch = HangulChosungUtils.getSearch(musicDto.getMusicTitle());
        String artistChosung = HangulChosungUtils.getChosung(musicDto.getMusicArtist());
        String artistSearch = HangulChosungUtils.getSearch(musicDto.getMusicArtist());
        
        musicDto.setMusicNo(musicNo);
        musicDto.setMusicTitleChosung(titleChosung);
        musicDto.setMusicTitleSearch(titleSearch);
        musicDto.setMusicArtistChosung(artistChosung);
        musicDto.setMusicArtistSearch(artistSearch);
        musicDto.setMusicUploader(memberNickname);
        musicDto.setMusicStatus(musicStatus);
        musicDao.insert(musicDto); 
        
        // 프로필 이미지 저장 (첫 번째 파일)
        MultipartFile coverImage = attaches.get(0);
        
        if (!coverImage.isEmpty()) {
            attachmentService.save(coverImage, musicNo, "cover"); 
             // category를 'music' 대신 'cover'로 분리.
        }
        
        // 음악 파일 저장 (두 번째 파일)
        MultipartFile musicFile = attaches.get(1);
        
        if (!musicFile.isEmpty()) {
             attachmentService.save(musicFile, musicNo, "music");
        }
        
        // 장르 매핑 INSERT (music_genre_map 테이블)
         musicDao.insertGenreMap(musicNo, musicGenres);

        return musicNo;
    }
    
}
