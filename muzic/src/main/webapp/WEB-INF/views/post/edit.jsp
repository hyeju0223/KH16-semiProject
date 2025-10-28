<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.js"></script>
<link rel="stylesheet" type="text/css" href="/summernote/custom-summernote.css">  
<script src="/summernote/custom-summernote.js"></script>

<script>

</script>

    <div class="cell center">
        <h1>게시글 수정</h1>
    </div>

    <form action="edit" method="post">
        <input type="hidden" name="postNo" value="${postDto.postNo}">
        
        <c:if test="${sessionScope.loginMemberRole == '관리자'}">
            <div class="cell right">
                <input type="checkbox" name="postNotice" value="Y" 
                        ${postDto.postNotice == 'Y' ? 'checked' : ''}> 
                <span>공지사항</span>
            </div>
        </c:if>

        <div class="cell">
            <label>제목 <span class="red">*</span></label>
            <input type="text" name="postTitle" value="${postDto.postTitle}" required class="field w-100">
        </div>
        
	<textarea name="postContent" class="summernote-editor">${postDto.postContent}</textarea>
	<br><br>
        
	<div class="cell">
        <label>음악 선택</label>
        <select name="postMusic" class="field w-100">
            <option value="">선택 안함</option>
            <c:forEach var="music" items="${musicList}">
    			<option value="${music.musicNo}">${music.musicTitle} - ${music.musicArtist}</option>
			</c:forEach>
        </select>
    </div>

        <div class="cell right">
            <c:choose>
                <c:when test="${not empty postDto.postMbti}">
                    <a href="${pageContext.request.contextPath}/post/mbti/list" class="btn btn-neutral">취소/목록</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/post/free/list" class="btn btn-neutral">취소/목록</a>
                </c:otherwise>
            </c:choose>
            <button class="btn btn-positive">수정하기</button>
        </div>
    </form>
