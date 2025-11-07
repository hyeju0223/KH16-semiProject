<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/commons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-music.css">

<div class="admin-container">
    <div class="admin-title">🎧 음원 관리</div>

    <!-- 상태 필터 -->
    <div class="admin-status-tabs">
        <a href="?status=전체" class="tab ${status == '전체' ? 'active' : ''}">전체</a>
        <a href="?status=대기" class="tab ${status == '대기' ? 'active' : ''}">대기</a>
        <a href="?status=수정요청" class="tab ${status == '수정요청' ? 'active' : ''}">수정요청</a>
        <a href="?status=삭제요청" class="tab ${status == '삭제요청' ? 'active' : ''}">삭제요청</a>
        <a href="?status=승인" class="tab ${status == '승인' ? 'active' : ''}">승인</a>
        <a href="?status=반려" class="tab ${status == '반려' ? 'active' : ''}">반려</a>
    </div>

    <!-- 검색 영역 -->
    <form method="get" class="admin-search-box">
        <input type="hidden" name="status" value="${status}">
        <select name="column" class="admin-search-select">
            <option value="music_title" ${searchCondition.column == 'music_title' ? 'selected' : ''}>제목</option>
            <option value="music_artist" ${searchCondition.column == 'music_artist' ? 'selected' : ''}>가수</option>
            <option value="music_album" ${searchCondition.column == 'music_album' ? 'selected' : ''}>앨범</option>
            <option value="music_uploader" ${searchCondition.column == 'music_uploader' ? 'selected' : ''}>업로더</option>
        </select>
        <input type="text" name="keyword" class="admin-search-input" placeholder="검색어 입력" value="${searchCondition.keyword}">
        <button type="submit" class="admin-search-btn">검색</button>
    </form>

    <!-- 테이블 -->
    <table class="admin-table">
        <thead>
            <tr>
                <th>No</th>
                <th>제목</th>
                <th>가수</th>
                <th>업로더</th>
                <th>상태</th>
                <th>관리</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="music" items="${list}">
                <tr>
                    <td>${music.musicNo}</td>
                    <td>${music.musicTitle}</td>
                    <td>${music.musicArtist}</td>
                    <td>${music.musicUploader}</td>
                    <td><span class="status-badge status-${music.musicStatus}">${music.musicStatus}</span></td>
                    <td><a class="admin-btn btn-detail" href="${pageContext.request.contextPath}/admin/music/detail?musicNo=${music.musicNo}">관리</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty list}">
                <tr><td colspan="6">데이터가 없습니다.</td></tr>
            </c:if>
        </tbody>
    </table>

    <div class="admin-pagination">
        <c:if test="${!searchCondition.firstBlock}">
            <a href="?page=${searchCondition.prevPage}&status=${status}&column=${searchCondition.column}&keyword=${searchCondition.keyword}" class="page-btn">◀</a>
        </c:if>
        <c:forEach begin="${searchCondition.startPage}" end="${searchCondition.endPage}" var="p">
            <a href="?page=${p}&status=${status}&column=${searchCondition.column}&keyword=${searchCondition.keyword}" class="page-btn ${p == searchCondition.page ? 'active' : ''}">${p}</a>
        </c:forEach>
        <c:if test="${!searchCondition.lastBlock}">
            <a href="?page=${searchCondition.nextPage}&status=${status}&column=${searchCondition.column}&keyword=${searchCondition.keyword}" class="page-btn">▶</a>
        </c:if>
    </div>
</div>
