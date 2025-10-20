package com.muzic.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MusicDto {
	private int musicNo;               // music_no
    private String musicTitle;          // music_title
    private String musicTitleChosung;   // music_title_chosung
    private String musicArtist;         // music_artist
    private String musicArtistChosung;  // music_artist_chosung
    private String musicAlbum;          // music_album
    private String musicGenre;          // music_genre
    private String musicUploader;       // music_uploader
    private Integer musicPlay;          // music_play
    private Integer musicLike;          // music_like
    private Timestamp musicUtime;       // music_utime
}
