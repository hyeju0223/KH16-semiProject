package com.muzic.dto;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MusicFormDto {
	
	private String musicTitle;
    private String musicArtist;
    private String musicAlbum;
    private Set<String> musicGenreSet;
    private MultipartFile coverImage;
    private MultipartFile musicFile;
	
}
