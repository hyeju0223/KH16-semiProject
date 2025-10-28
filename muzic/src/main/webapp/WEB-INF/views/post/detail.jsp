<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- 댓글 -->
<script type="text/javascript">
        $(function(){
            var param = new URLSearchParams(location.search);
            var postNo = param.get("postNo");

            loadList();

            function loadList() {
                $.ajax({
                    url : "rest/comments/list",
                    method : "post",
                    data : {
                        commentsTarget : postNo
                    },
                    success : function(response) {
                        $(".comments-list-area").empty();

                        for(var i = 0; i < response.length; i++) {
                            var comments = response[i];

                            var origin = $("#comments-view-template").text();
                            var html = $.parseHTML(origin);

                            $(html).find(".comments-area").text(comments.commentsWriter)

                            if(comments.writer == true) {
                                $(html).find(".comments-writer").append("<span >작성자</span>")
                            }

                            $(html).find(".comments-content").text(comments.commentsContent);

                            var wtime = moment(comments.commentsWtime).fromNow();
                            $(html).find(".comments-time").text(wtime);

                            $(html).find(".fa-edit").attr("data-pk", comments.commentsNo);
						    $(html).find(".fa-trash").attr("data-pk", comments.commentsNo);

                            if(comments.owner == false) {
                                $(html).find(".button-area").remove();
                            }

                            $(".comments-list-area").append(html);
                        }
                    }
               });
            }
        });
     </script>

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

<c:if test="${musicUserVO != null}">  <div class="music-info-box">
    <h3>첨부된 음악</h3>
    <p>
        <span style="font-weight: bold;">제목:</span> ${musicUserVO.musicTitle}  <span style="margin: 0 10px;">|</span>
        <span style="font-weight: bold;">아티스트:</span> ${musicUserVO.musicArtist} </p>
    <p>
        <span style="font-weight: bold;">앨범:</span> ${musicUserVO.musicAlbum} </p>
</div>
<hr>
</c:if>

<div class="cell">
		<span class="red">
			<i id="post-like" class="fa-regular fa-heart red"></i> 
			<span id="post-like-count">?</span>
		</span>
		
		<span>
			<i class="fa-solid fa-list"></i>
			<span>${commentsDto.postComments}</span>
		</span>  
</div>

<div class="cell">
	<div class="comments-list-area">댓글 영역</div>
</div>
	
	<div class="cell">
		<c:choose>
			<c:when test="${sessionScope.loginMemberId != null}">
				<div class="comments-write-area">
					<div class="cell">
						<textarea class="comments-input" rows="4" style="resize:none;" placeholder="댓글 내용">
						</textarea>
					</div>
					<div class="cell">
						<button type="button" class="comments-btn-wrute">등록하기</button>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="comments-write-area">
					<div class="cell">
						<textarea rows="4" style="resize:none;" placeholder="로그인 후 작성이 가능합니다" disabled></textarea>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
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

