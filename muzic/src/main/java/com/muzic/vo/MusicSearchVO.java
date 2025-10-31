package com.muzic.vo;

import java.sql.Timestamp;
import java.util.Set;
import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MusicSearchVO {

    private int musicNo;               
    private String musicTitle;         
    private String musicArtist;        
    private String musicAlbum;         
    private String uploaderNickname;  
    private int musicPlay;            
    private int musicLike;             
    private Timestamp musicUtime;     
    private Timestamp musicEtime;      
    private String musicStatus;        
    private String adminComment;       
    @Builder.Default
    private Set<String> musicGenres = new HashSet<>();
    private int coverAttachmentNo;
    private int musicFileAttachmentNo;
    
    
}