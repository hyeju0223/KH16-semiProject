package com.muzic.domain;

public enum AttachmentCategory {
    PROFILE("profile"),
    MUSIC("music"),
    GOODS("goods"),
    POST("post"),
    COVER("cover");

    private final String value;

    AttachmentCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}