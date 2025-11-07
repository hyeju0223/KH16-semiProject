<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <title>음원 상세 관리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/commons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-music.css">
    
<div class="admin-container">
    <div class="admin-title">🎧 음원 상세 관리</div>

    <!-- 돌아가기 버튼 -->
    <a href="${pageContext.request.contextPath}/admin/music/list" class="admin-btn btn-detail" style="margin-bottom:14px;display:inline-block;">← 목록으로</a>

    <!-- 상세 카드 -->
    <div class="admin-detail-card">
        <div class="detail-row">
            <div class="detail-label">음원번호</div>
            <div class="detail-value">${musicDto.musicNo}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">제목</div>
            <div class="detail-value">${musicDto.musicTitle}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">가수</div>
            <div class="detail-value">${musicDto.musicArtist}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">앨범</div>
            <div class="detail-value">${musicDto.musicAlbum != null ? musicDto.musicAlbum : '-'} </div>
        </div>
        <div class="detail-row">
            <div class="detail-label">업로더</div>
            <div class="detail-value">${musicDto.musicUploader}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">상태</div>
            <div class="detail-value">
                <span class="status-badge status-${musicDto.musicStatus}">${musicDto.musicStatus}</span>
            </div>
        </div>
        <div class="detail-row">
            <div class="detail-label">등록일</div>
            <div class="detail-value">${musicDto.musicUtime}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">수정일</div>
            <div class="detail-value">${musicDto.musicEtime != null ? musicDto.musicEtime : '-'} </div>
        </div>
    </div>

    <!-- Comment form -->
    <form method="post" action="${pageContext.request.contextPath}/admin/music/approve" class="admin-action-box">
        <input type="hidden" name="musicNo" value="${musicDto.musicNo}">
        <textarea name="comment" class="admin-textarea" placeholder="승인 사유를 입력하세요(선택)"></textarea>
        <button class="admin-btn btn-success" type="submit">✅ 승인</button>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/admin/music/reject" class="admin-action-box">
        <input type="hidden" name="musicNo" value="${musicDto.musicNo}">
        <textarea name="comment" class="admin-textarea" placeholder="반려 사유를 입력하세요(필수)"></textarea>
        <button class="admin-btn btn-reject" type="submit">❌ 반려</button>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/admin/music/approve-edit" class="admin-action-box">
        <input type="hidden" name="musicNo" value="${musicDto.musicNo}">
        <textarea name="comment" class="admin-textarea" placeholder="수정 요청 승인 코멘트(선택)"></textarea>
        <button class="admin-btn btn-info" type="submit">✏️ 수정요청 승인</button>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/admin/music/approve-delete" class="admin-action-box">
        <input type="hidden" name="musicNo" value="${musicDto.musicNo}">
        <textarea name="comment" class="admin-textarea" placeholder="삭제 요청 승인 코멘트(선택)"></textarea>
        <button class="admin-btn btn-danger" type="submit">🗑 삭제요청 승인</button>
    </form>
</div>
