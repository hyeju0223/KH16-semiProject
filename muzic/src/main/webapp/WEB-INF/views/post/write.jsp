<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/trix/1.3.1/trix.min.css">
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/trix/1.3.1/trix.min.js"></script>

<!-- Trix의 기본 첨부 기능 비활성화 (보안 및 DB 문제 방지) -->
<script>
    // Trix 에디터의 파일 첨부 기능을 비활성화합니다.
    Trix.config.attachments.preview.caption = {
        name: false,
        size: false
    };
    Trix.config.attachments.preview.captionText = false;

    document.addEventListener("trix-file-accept", function(event) {
        event.preventDefault();
        // 사용자에게 파일 첨부가 허용되지 않음을 알릴 수 있습니다.
        // console.log("파일 첨부는 현재 허용되지 않습니다."); 
    });
</script>

<form autocomplete="off" action="${pageContext.request.contextPath}/post/write" method="post">



<div class="container w-800">
    <div class="cell">
        <h1>게시글 작성</h1>
    </div>            
    <div class="cell">
        <p>인격 어쩌구.... 말 이쁘게 쓰기🎅🎅<p>
    </div>
    
    <c:if test="${sessionScope.loginMemberRole == '관리자'}">
    	<div class="cell right">
        	<input type="checkbox" name="postNotice" value="Y">
        	<span>공지사항으로 등록</span>
    	</div>
    </c:if>
    
    <div class="cell">
        <label>제목 <span class="red">*</span></label>
        <input type="text" name="postTitle" required class="field w-100">
    </div>
<!--     <div class="cell"> -->
<!--         <label>내용 <span class="red">*</span></label> -->
<!--         <textarea name="postContent"></textarea> -->
<!--     </div> -->
	<div class="cell">
        <label>내용 <span class="red">*</span></label>
        <!-- Trix 에디터가 실제 내용(HTML)을 저장할 숨겨진 input 필드. name 속성이 중요합니다. -->
        <input id="postContentInput" type="hidden" name="postContent">
        
        <!-- trix-editor 태그를 사용하여 에디터를 표시하고, input 필드와 연결합니다. -->
        <trix-editor input="postContentInput" class="trix-content"></trix-editor>
    </div>
    <!-- Trix Editor 적용 끝 -->


    <div class="cell">
        <label>음악 선택</label>
        <select name="postMusic" class="field w-100">
            <option value="">선택 안함</option>
            <c:forEach var="music" items="${musicList}">
    			<option value="${music.musicNo}">${music.musicTitle} - ${music.musicArtist}</option>
			</c:forEach>
        </select>
    </div>
    
<%--     <c:choose> --%>
<%--     	<c:when test="${not empty post.postMbti}"> --%>
<%--        		<a href="/post/mbti/list?mbti=${sessionScope.loginMemberMbti}" class="btn btn-neutral">목록으로</a> --%>
<%--     	</c:when> --%>
<%--     	<c:otherwise> --%>
<!--        		<a href="/post/free/list" class="btn btn-neutral">목록으로</a> -->
<%--     	</c:otherwise> --%>
<%-- 	</c:choose> --%>

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

        <button class="btn btn-positive">등록하기</button>
    </div>
</div>
</form>