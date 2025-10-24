package com.muzic.domain;

public enum AttachmentCategory {
    PROFILE("profile"),
    MUSIC("music"),
    GOODS("goods"),
    POST("post"),
    COVER("cover");

    private final String category;

    AttachmentCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return category;
    }
}