package com.muzic.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @Builder @AllArgsConstructor
public class MusicDto {
	private int musicNo;               // music_no
    private String musicTitle;          // music_title
    private String musicTitleChosung;   // music_title_chosung
    private String musicArtist;         // music_artist
    private String musicArtistChosung;  // music_artist_chosung
    private String musicTitleSearch;	// music_title_search
    private String musicArtistSearch;	// music_artist_search
    private String musicAlbum;          // music_album
    private String musicUploader;       // music_uploader
    private int musicPlay;          // music_play
    private int musicLike;          // music_like
    private Timestamp musicUtime;       // music_utime
    private Timestamp musicEtime;		// music_etime
    private String musicStatus;		//music_status
}

