package com.muzic.domain;

public enum MusicStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("반려"),
    DELETE_REQUEST("삭제요청");

    private final String status;

    MusicStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return status;
    }
}