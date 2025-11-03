package com.muzic.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.MusicGenreDao;
import com.muzic.dao.MusicStatusHistoryDao;
import com.muzic.domain.AttachmentCategory;
import com.muzic.dto.MusicDto;
import com.muzic.dto.MusicFormDto;
import com.muzic.dto.MusicStatusHistoryDto;
import com.muzic.error.InvalidContentException;
import com.muzic.util.HangulChosungUtil;
import com.muzic.vo.MusicSearchVO;

@Service
public class MusicHelperService {
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private MusicGenreDao musicGenreDao;
	
	@Autowired
	private MusicStatusHistoryDao musicStatusHistoryDao;
	
    // 선택 음원장르 검증 
    public void validateGenres(Set<String> genres) {
        if (genres == null || genres.isEmpty())
            throw new InvalidContentException("장르는 최소 1개 선택해야 합니다.");
        if (!musicGenreDao.areAllGenresValid(genres))
            throw new InvalidContentException("유효하지 않은 장르입니다.");
    }

    // 음악파일 등록 여부 검증
    public void validateMusicFile(MultipartFile musicFile) {
        if (musicFile == null || musicFile.isEmpty())
            throw new InvalidContentException("음악파일은 필수로 첨부되어야 합니다.");
    }
    
    // 음원 객체 빌드
    public MusicDto buildMusicDto(MusicFormDto musicFormDto, int musicNo, String memberId, String musicStatus) {
        return MusicDto.builder()
                .musicNo(musicNo)
                .musicTitle(musicFormDto.getMusicTitle())
                .musicTitleChosung(HangulChosungUtil.getChosung(musicFormDto.getMusicTitle()))
                .musicArtist(musicFormDto.getMusicArtist())
                .musicArtistChosung(HangulChosungUtil.getChosung(musicFormDto.getMusicArtist()))
                .musicTitleSearch(HangulChosungUtil.getSearch(musicFormDto.getMusicTitle()))
                .musicArtistSearch(HangulChosungUtil.getSearch(musicFormDto.getMusicArtist()))
                .musicAlbum(musicFormDto.getMusicAlbum())
                .musicUploader(memberId)
                .musicStatus(musicStatus)
                .musicAlbumChosung(HangulChosungUtil.getChosung(musicFormDto.getMusicAlbum()))
                .musicAlbumSearch(HangulChosungUtil.getSearch(musicFormDto.getMusicAlbum()))
                .build();
    }
    
    // 음원 이미지, 음원 파일 저장
    public void saveAttachments(MusicFormDto musicFormDto, int musicNo) throws IOException {
        MultipartFile coverImage = musicFormDto.getCoverImage();
        // 파일이 null일 경우 앞에 null 조건으로 확인을 해야 null이 들어왔을 때 false를 유도해서 바로 조건문을 false로 마감시킬 수 있음. 
        // 만약 isEmpty를 먼저 체크하면 null일 발생했을 경우 조건계산이 아닌 nullpointer예외가 발생함.
        // 프로필 이미지 저장 (첫번째 파일)
        if (coverImage != null && !coverImage.isEmpty()) {
        	// 파일 카테고리 "cover"로 분리
            attachmentService.save(coverImage, AttachmentCategory.COVER.getCategoryName(), musicNo);
        }
        // 음원 파일 저장 (두번째 파일
        MultipartFile musicFile = musicFormDto.getMusicFile();
        if (musicFile != null && !musicFile.isEmpty()) {
            attachmentService.save(musicFile, AttachmentCategory.MUSIC.getCategoryName(), musicNo);
        }
    }
    
    // 음악등록 히스토리 저장
    public void insertHistory(int musicNo, String musicStatus, String memberId, String comment, boolean isAdmin) {
        MusicStatusHistoryDto history = MusicStatusHistoryDto.builder()
                .mshMusicNo(musicNo)
                .mshMusicStatus(musicStatus)
                .mshComment(comment)
                .mshAdminId(isAdmin ? memberId : null)
                .build();
        musicStatusHistoryDao.insert(history);
    }
	
    // 음원 이미지, 음원 파일 삭제
    public void deleteAttachments(int musicNo) {
        int coverImageNo =
                attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.COVER.getCategoryName());
        int musicFileNo =
                attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.MUSIC.getCategoryName());
        if (coverImageNo > 0) attachmentService.delete(coverImageNo);
        if (musicFileNo > 0) attachmentService.delete(musicFileNo);
    }
    
    // 음원 이미지, 음원 파일 수정
    public void changeAttachments(MusicFormDto musicFormDto, int musicNo) throws IOException {
    	int beforeCoverImageNo = attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.COVER.getCategoryName());
    	int beforeMusicFileNo = attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.MUSIC.getCategoryName());
    	if(musicFormDto.getCoverImage() != null && musicFormDto.getMusicFile() != null) {
        	deleteAttachments(musicNo);
    	} else if(musicFormDto.getCoverImage() != null && musicFormDto.getMusicFile() == null) {
    		attachmentService.delete(beforeCoverImageNo);
    	} else if(musicFormDto.getCoverImage() == null && musicFormDto.getMusicFile() != null) {
    		attachmentService.delete(beforeMusicFileNo);
    	}
    	saveAttachments(musicFormDto, musicNo);
    }
    
    // 승인 음원 View에서 직접 파일 정보 가져오기 위해 세팅
    public void setMusicAttachmentNo(List<MusicSearchVO> list) {
        for (MusicSearchVO vo : list) {
            int coverNo = attachmentService.getAttachmentNoByParent(vo.getMusicNo(), "COVER");
            int musicFileNo = attachmentService.getAttachmentNoByParent(vo.getMusicNo(), "MUSIC");
            vo.setCoverAttachmentNo(coverNo);
            vo.setMusicFileAttachmentNo(musicFileNo);
        }
    }
}
