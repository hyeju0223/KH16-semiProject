package com.muzic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muzic.dao.MusicDao;
import com.muzic.dao.MusicStatusHistoryDao;
import com.muzic.domain.MusicStatus;
import com.muzic.dto.MusicStatusHistoryDto;

@Service
public class AdminMusicService {

    @Autowired
    private MusicDao musicDao;

    @Autowired
    private MusicStatusHistoryDao historyDao;

    private void insertHistory(int musicNo, String status, String comment, String adminId) {
        MusicStatusHistoryDto dto = MusicStatusHistoryDto.builder()
                .mshMusicNo(musicNo)
                .mshMusicStatus(status)
                .mshComment(comment)
                .mshAdminId(adminId)
                .build();
        historyDao.insert(dto);
    }

    @Transactional
    public boolean approve(int musicNo, String adminId, String comment) {
        boolean success = musicDao.updateMusicStatus(musicNo, MusicStatus.APPROVED.getStatusName());
        if(success) insertHistory(musicNo, MusicStatus.APPROVED.getStatusName(), comment, adminId);
        return success;
    }

    @Transactional
    public boolean reject(int musicNo, String adminId, String comment) {
        boolean success = musicDao.updateMusicStatus(musicNo, MusicStatus.REJECTED.getStatusName());
        if(success) insertHistory(musicNo, MusicStatus.REJECTED.getStatusName(), comment, adminId);
        return success;
    }

    @Transactional
    public boolean approveDelete(int musicNo, String adminId, String comment) {
        boolean success = musicDao.updateMusicStatus(musicNo, MusicStatus.DELETE_REQUEST.getStatusName());
        if(success) insertHistory(musicNo, MusicStatus.DELETE_REQUEST.getStatusName(), comment, adminId);
        return success;
    }

    @Transactional
    public boolean approveUpdate(int musicNo, String adminId, String comment) {
        boolean success = musicDao.updateMusicStatus(musicNo, MusicStatus.EDIT_REQUEST.getStatusName());
        if(success) insertHistory(musicNo, MusicStatus.EDIT_REQUEST.getStatusName(), comment, adminId);
        return success;
    }
}
