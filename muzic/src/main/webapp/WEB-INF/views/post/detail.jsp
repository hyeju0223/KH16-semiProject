<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div>
    <c:choose>
        <c:when test="${memberDto == null}">
            탈퇴한사용자
        </c:when>
        <c:otherwise>
            <a href="/member/detail?memberId=${memberDto.memberId}">
                ${memberDto.memberNickname}
            </a>  
            (${memberDto.memberRole})
        </c:otherwise>
    </c:choose>
</div>

<div>
	<fmt:formatDate value="${postDto.postWtime}" pattern="yyyy-MM-dd HH:mm"/> 
	조회수 ${postDto.postRead}
</div>
<hr>
<div style="min-height: 200px">
	${postDto.postContent}
</div>
<hr>

<c:if test="${musicDto != null}">  <div class="music-info-box">
    <h3>첨부된 음악</h3>
    <p>
        <span style="font-weight: bold;">제목:</span> ${musicDto.musicTitle}  <span style="margin: 0 10px;">|</span>
        <span style="font-weight: bold;">아티스트:</span> ${musicDto.musicArtist} </p>
    <p>
        <span style="font-weight: bold;">앨범:</span> ${musicDto.musicAlbum} </p>
</div>
<hr>
</c:if>

<div>
	좋아요 
	<i id="post-like" class="fa-regular fa-heart red"></i> 
	<span id="post-like-count">?</span>  
	댓글 ${postDto.postLike}
</div>


<div>
	<a href="write">글쓰기</a> 
	<c:if test="${sessionScope.loginMemberId != null}">
	<c:choose>
		<c:when test="${sessionScope.loginMemberId == postDto.postWriter}">
			<a href="edit?postNo=${postDto.postNo}">수정</a> 
			<a href="delete?postNo=${postDto.postNo}">삭제</a>
		</c:when>
		<c:when test="${sessionScope.loginMemberRole == '관리자'}">
			<a href="delete?postNo=${postDto.postNo}">삭제</a>
		</c:when>
	</c:choose>
	</c:if>
	
	<c:choose>
    <c:when test="${not empty postDto.postMbti}">
        <!-- MBTI 게시판 글이면 MBTI 목록으로 -->
        <a href="${pageContext.request.contextPath}/post/mbti/list?mbti=${postDto.postMbti}" class="btn btn-neutral">목록으로</a>
    </c:when>
    <c:otherwise>
        <!-- 자유게시판 글이면 자유게시판 목록으로 -->
        <a href="${pageContext.request.contextPath}/post/free/list" class="btn btn-neutral">목록으로</a>
    </c:otherwise>
</c:choose>

</div>

