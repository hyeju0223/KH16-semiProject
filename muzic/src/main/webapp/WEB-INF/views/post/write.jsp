<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/post.css">
<!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> -->


<jsp:include page="/WEB-INF/views/template/header-1.jsp" />
<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/summernote/custom-summernote.css">  
<script src="${pageContext.request.contextPath}/summernote/custom-summernote.js"></script>

<style>

</style>

<script>

</script>

<form autocomplete="off" action="${pageContext.request.contextPath}/post/write" method="post">

<input type="hidden" name="postType" value="${empty postDto.postMbti ? 'free' : 'mbti'}">

<c:if test="${not empty postDto.postMbti}">
    <input type="hidden" name="postMbti" value="${postDto.postMbti}">
</c:if>

<div class="container w-1000">
    <div class="cell center">
        <h1>게시글 작성</h1>
    </div>            
    <div class="cell center">
        <p>타인을 향한 욕설 및 비난 적발 시 사이트 이용에 제한이 생깁니다😎</p>
    </div>
    
    <c:if test="${sessionScope.loginMemberRole == '관리자'}">
    	<div class="cell right">
        	<input type="checkbox" name="postNotice" value="Y">
        	<span>공지사항으로 등록</span>
    	</div>
    </c:if>
    
    <div class="cell">
        <label>제목 <span class="red">*</span></label>
        <input type="text" name="postTitle" required class="field" style="width: 100%">
    </div>

	<div class="cell">
        <label>내용 <span class="red">*</span></label>
        <textarea name="postContent" class="summernote-editor"></textarea>
    </div>

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
        <!-- 목록보기/취소 버튼 경로 동적 설정 -->
        <c:choose>
            <c:when test="${not empty postDto.postMbti}">
                 <!-- MBTI 게시판 글쓰기일 경우 MBTI 목록으로 돌아감 -->
                <a href="${pageContext.request.contextPath}/post/mbti/list" class="btn btn-neutral">목록보기</a>
            </c:when>
            <c:otherwise>
                <!-- 자유게시판 글쓰기일 경우 자유 게시판 목록으로 돌아감 -->
                <a href="${pageContext.request.contextPath}/post/free/list" class="btn btn-neutral">목록보기</a>
            </c:otherwise>
        </c:choose>
        <button class="btn btn-positive">등록하기</button>
    </div>
</div>
</form>

<jsp:include page="/WEB-INF/views/template/footer.jsp" />