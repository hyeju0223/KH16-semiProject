package com.muzic.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MusicDto {

	private int musicNo;
	private String musicTitle;
	private String musicTitleChosung;
	private String musicArtist;
	private String musicArtistChosung;
	private String musicAlbum;
//	private String musicGenre;
	private String musicUploader;
	private Integer musicPlay;
	private Integer musicLike;
	private Timestamp musicUtime;
}
