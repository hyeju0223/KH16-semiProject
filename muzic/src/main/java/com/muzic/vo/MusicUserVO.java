package com.muzic.vo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder 
public class MusicUserVO {

    private int musicNo;
    private String musicTitle;
    private String musicArtist;
    private String musicAlbum;
    private String uploaderNickname;
    private int musicPlay;
    private int musicLike;
    private Timestamp musicUtime;
    @Builder.Default
    private Set<String> musicGenres = new HashSet<>();
    private int coverAttachmentNo;
    private int musicFileAttachmentNo;
    
}
