package com.muzic.domain;

public enum MemberRole {
    ADMIN("관리자"),
    USER("일반회원"),
    SUSPENDED("정지회원");

    private final String roleName;

    MemberRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}