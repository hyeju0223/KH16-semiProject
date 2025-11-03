<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/template/header-1.jsp" />

<!DOCTYPE html>
<html lang="ko">
<title>게시글</title>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">

<!-- jquery cdn -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<!-- momentjs CDN-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.30.1/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.30.1/locale/ko.min.js"></script>

<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.js"></script>
<link rel="stylesheet" type="text/css" href="/summernote/custom-summernote.css">  
<script src="/summernote/custom-summernote.js"></script>
<link rel="stylesheet" type="text/css" href="/css/post.css">


<script type="text/javascript">
	$(function(){
		var params = new URLSearchParams(location.search);
		var postNo = params.get("postNo");
		
		// 좋아요 상태 확인
		$.ajax({
			url:"/rest/post/check?postNo="+postNo,
			method: "get",
			success: function(response) {
				if(response.like) {
					$("#post-like").removeClass("fa-solid fa-regular").addClass("fa-solid");
					$("#post-like-count").text(response.count)
				}
				else {
					$("#post-like").removeClass("fa-solid fa-regular").addClass("fa-regular");
					$("#post-like-count").text(response.count);
				}
			}
		});
	});
</script>

<c:if test="${sessionScope.loginMemberId != null}">
	<script type="text/javascript">
		$(function(){
			$("#post-like").on("click", function(){
				var params = new URLSearchParams(location.search);
				var postNo = params.get("postNo");
				
				$.ajax({
					url: "/rest/post/action?postNo="+postNo,
					method: "get",
					success: function(response) {
						if(response.like) {
							$("#post-like").removeClass("fa-solid fa-regular").addClass("fa-solid");
							$("#post-like-count").text(response.count)
						}
						else {
							$("#post-like").removeClass("fa-solid fa-regular").addClass("fa-regular");
							$("#post-like-count").text(response.count);
						}
					}
				});
			});
		});
	</script>
</c:if>

<!-- 댓글 -->
<script type="text/javascript">
        $(function(){
            var param = new URLSearchParams(location.search);
            var postNo = param.get("postNo");

            //loadList - 클릭하면 호출
            function loadList() {
                $.ajax({
                    url : "/rest/comments/list",
                    method : "get",
                    data : {
                    	commentPost : postNo
                    },
                    success : function(response) {
                        $(".comments-list-area").empty();

//                         for(var i = 0; i < response.length; i++) {
//                             var comments = response[i];

//                             var origin = $("#comments-view-template").text();
//                             var html = $.parseHTML(origin);

//                             $(html).find(".comments-writer").text(comments.commentWriter)

//                             if(comments.writer == true) {
//                                 $(html).find(".comments-writer").append("<span >${sessionScope.loginMemberNickname}</span>")
//                             }

//                             $(html).find(".comments-content").text(comments.commentContent);

//                             var wtime = moment(comments.commentWtime).fromNow();
//                             $(html).find(".comments-time").text(wtime);

//                             $(html).find(".comment-like-icon").attr("data-comment-no", comments.commentNo);
//                             $(html).find(".comment-like-count").text(comments.commentLike);
                            
//                             $(html).find(".fa-pen-to-square").attr("data-pk", comments.commentNo);
// 						    $(html).find(".fa-eraser").attr("data-pk", comments.commentNo);

//                             if(comments.owner == false) {
//                                 $(html).find(".button-area").remove();
//                             }

//                             $(".comments-list-area").append(html);

						for(var i = 0; i < response.length; i++) {
                            var comments = response[i];

                            var origin = $("#comments-view-template").text();
                            var html = $.parseHTML(origin);

                            $(html).find(".comments-writer").text(comments.commentWriter)

//                             if(comments.writer == true) {
//                                 $(html).find(".comments-writer").append("<span >${sessionScope.loginMemberNickname}</span>")
//                             }

                            $(html).find(".comments-content").text(comments.commentContent);

                            var wtime = moment(comments.commentWtime).fromNow();
                            $(html).find(".comments-time").text(wtime);

                            $(html).find(".comment-like-icon").attr("data-comment-no", comments.commentNo);
                            $(html).find(".comment-like-count").text(comments.commentLike);
                            
                            $(html).find(".fa-pen-to-square").attr("data-pk", comments.commentNo);
						    $(html).find(".fa-eraser").attr("data-pk", comments.commentNo);

                            if(comments.owner == false) {
                                $(html).find(".button-area").remove();
                            }

                            $(".comments-list-area").append(html);
                        }
                    }
               });
            }
            
			// 댓글 좋아요/취소 액션
            $(".comments-list-area").on("click", ".comment-like-icon", function(){
				var $icon = $(this);
				// 로그인 여부는 서버에서 체크하므로 클라이언트에서는 바로 요청
				var commentNo = $icon.data("comment-no");
                
				if (!commentNo) {
                    console.error("댓글 번호가 누락되었습니다.");
                    return;
                }
				
				$.ajax({
					url: "/rest/comments/action",
					method: "post",
					data: { commentNo: commentNo },
					success: function(response) {
						// response.like (boolean)와 response.count (int)를 사용
						var $count = $icon.next(".comment-like-count");
                        
						$count.text(response.count);

						if(response.like) {
							$icon.removeClass("fa-regular").addClass("fa-solid");
						}
						else {
							$icon.removeClass("fa-solid").addClass("fa-regular");
						}
						},
					error: function(xhr) {
						if(xhr.status === 403) { 
							alert("로그인 후 이용 가능합니다.");
						}
						else {
							console.error("좋아요 처리 실패:", xhr.responseText);
						}
					}
				});
			});
            
            //댓글 삭제
            $(".comments-list-area").on("click", ".fa-eraser", function(){
            	var choice = window.confirm("댓글을 삭제하시겠습니까?");
            	if(choice == false) return;
            	
            	var commentNo = $(this).data("pk");
            	
            	if (!commentNo) {
                    console.error("댓글 번호가 누락되었습니다.");
                    return;
                }
            	
            	$.ajax({
            		url: "/rest/comments/delete",
            		method: "post",
            		data: { commentNo : commentNo },
            		success: function(response) {
            			loadList();
            		}
            	});
            });
            
            //댓글 수정
            $(".comments-list-area").on("click", ".fa-pen-to-square", function(){
            	$(".comments-list-area").find(".comments-edit-area").remove();
                $(".comments-list-area").find(".comments-area").show();
            	
            	var origin = $("#comments-edit-template").text();
            	var html = $.parseHTML(origin);
            	
            	var commentNo = $(this).data("commentNo");
            	if (!commentNo) {
                    console.error("댓글 번호가 누락되었습니다.");
                    return;
                }
            	
            	var commentContent = $(this).closest(".comments-area").find(".comments-content").text().trim();
            	
            	$(html).find(".fa-check").attr("data-pk", commentNo);
            	$(html).find(".comments-editor").val(commentContent);
            	
            	$(".comments-area").show();
            	$(".comments-edit-area").remove();
            	$(this).closest(".comments-area").hide().after(html);
            });
            
            //댓글 수정 취소
            $(".comments-list-area").on("click", ".fa-xmark", function(){
            	$(this).closest(".comments-edit-area").prev(".comments-area").show();
            	$(this).closest(".comments-edit-area").remove();
            });
            //댓글 수정 완료
            $(".comments-list-area").on("click", ".fa-check", function(){
            	var commentNo = $(this).data("commentNo");
            	var commentContent = $(this).closest(".comments-edit-area").find(".comments-editor").val();
            	
            	$.ajax({
            		url: "/rest/comments/edit",
            		method: "post",
            		data: {
            			commentNo : commentNo,
            			commentContent : commentContent
            		},
            		success: function(response) {
            			loadList();
            		}
            	});
            });
            
            // 댓글 아이콘 클릭 이벤트 (토글 및 목록 로드)
            $("#post-comment").on("click", function() {
                // 댓글 섹션 토글
                $("#comments-section").slideToggle(200, function() {
                    // 댓글 영역이 열릴 때만 목록을 로드하고 아이콘 색상 변경
                    if ($(this).is(':visible')) {
                        loadList(); // 댓글 목록 로드
                        $("#post-comment").addClass("comment-icon-active").removeClass("comment-icon-inactive");
                    } else {
                        // 닫힐 때는 색상 원래대로
                        $("#post-comment").removeClass("comment-icon-active").addClass("comment-icon-inactive");
                    }
                });
            });


            $(".comments-btn-write").on("click", function(){
            	var content = $(".comments-input").val();
                
                // postNo 유효성 검사 추가!
                if (!postNo || postNo.length == 0) {
                    console.error("게시글 번호(postNo)가 유효하지 않습니다.");
                    alert("게시글 정보가 없어 댓글을 등록할 수 없습니다.");
                    return;
                }
                
                if(content.length == 0) {
                    alert("댓글 입력");
                    return;
                }
            	
            	$.ajax({
            		url:"/rest/comments/write",
            		method:"post",
            		data: { 
            		    commentPost : postNo, 
            		    commentContent : content.trim() 
            		},
            		success: function(response) {
//             			console.log(response);
						loadList();
						$(".comments-input").val("");
            		},
            		error: function(xhr, status, error) {
            			console.error("댓글 작성 실패:" , status, error);
            			alert("댓글 작성 실패 (서버오류)");
            			},
            		});
            	});
            });
     </script>

<style>
	/* 새로운 댓글 아이콘 색상 클래스 추가 */
	.comment-icon-inactive {
		color: #333; /* 기본 색상 (어두운 회색) */
	}
	.comment-icon-active {
		color: #f1c40f; /* 활성화 색상 (노란색) */
	}
	#post-comment {
		cursor: pointer; /* 클릭 가능한 모양 */
	}
	
	/* 기존 댓글 스타일 */
	.comments-wrapper {
		display: flex;
	}
	.comments-body-wrapper {
		flex-grow: 1;
		padding:10px;
	}
	.comments-writer {
		margin-top: 0;
		margin-bottom: 0;
	}
	.button-wrapper {
		text-align: right;
	}
	.comments-time {
		margin-top: 10px;
		color: gray;
	}
	.comments-edit-wrapper {
		padding:10px;
	}
	
	/* 좋아요 아이콘 색상 (빨간색 유지) */
    #post-like {
        cursor: pointer;
    }
    
    .comment-like-icon {
        cursor: pointer;
        color: red; /* 댓글 좋아요 아이콘 색상 */
        margin-right: 5px;
    }
</style>

<script type="text/template" id="comments-view-template">
	<div class="comments-area">
		<div class="comments-body-area">
			<h3 class="comments-writer"></h3>
			<pre class="comments-content">내용</pre>

			<!-- 댓글 좋아요 영역 -->
            <div class="comments-meta-area">
                <span style="color:red; margin-right: 15px;">
                    <!-- data-comment-no는 JS에서 동적으로 설정됩니다. -->
                    <i class="fa-regular fa-heart comment-like-icon"></i> 
                    <span class="comment-like-count">0</span>
                </span>
            </div>

			<div class="comments-time">yyyy-MM-dd</div>
			<div class="button-area">
				<i class="fa-regular fa-pen-to-square"></i>
				<i class="fa-solid fa-eraser"></i>
			</div>
		</div>
	</div>
</script>

<script type="text/template" id="comments-edit-template">
	<div class="comments-edit-area">
		<textarea class="comments-editor" row="4" style="resize:none;"></textarea>
		<div class="button-area">
			<i class="fa-solid fa-xmark"></i>
			<i class="fa-solid fa-check"></i>
		</div>
	</div>
</script>

<div class="w-1000">
	
	<div class="cell mt-30">
	    <c:choose>
	        <c:when test="${memberDto == null}">
	            탈퇴한사용자
	        </c:when>
	        <c:otherwise>
	            <a href="mypage/profile?memberId=${memberDto.memberId}">
	                ${memberDto.memberNickname}
	            </a>  
	            (${memberDto.memberRole})
	        </c:otherwise>
	    </c:choose>
	</div>
	
	<div class="cell">
	    <h1>${postDto.postTitle}</h1>
	</div>
	
	<div class="cell right">
	    <fmt:formatDate value="${postDto.postWtime}" pattern="yyyy-MM-dd HH:mm"/> 
	    조회수 ${postDto.postRead}
	</div>
	
	<hr>
	
	<!-- 게시글 내용 -->
	<div class="table-border" style="padding: 20px; min-height: 200px;">
	    ${postDto.postContent}
	</div>
	
	<hr>
	
	<!-- 첨부 음악 -->
	<c:if test="${musicUserVO != null}">
	    <c:url var="detailLink" value="/music/detail">
	        <c:param name="musicNo" value="${musicUserVO.musicNo}" />
	    </c:url>
	
	    <div class="table-border" style="padding: 15px; margin-top: 10px;">
	        <h3>첨부된 음악</h3>
	        <p>
	            <span style="font-weight: bold;">제목:</span> ${musicUserVO.musicTitle}  <span style="margin: 0 10px;">|</span>
	            <span style="font-weight: bold;">아티스트:</span> ${musicUserVO.musicArtist} <span style="margin: 0 10px;">|</span>
	            <span style="font-weight: bold;">앨범:</span> ${musicUserVO.musicAlbum} <span style="margin: 0 10px;">|</span>
	        </p>
	        <div class="cell right">
	            <a href="${detailLink}" class="btn btn-positive">음악 들으러 가기 &gt;&gt;&gt;</a>
	        </div>
	    </div>
	    <hr>
	</c:if>
	
	<!-- 좋아요 / 댓글 -->
	<div class="cell">
	    <span class="like-area" style="color:red;">
	        <i id="post-like" class="fa-regular fa-heart"></i> 
	        <span id="post-like-count">?</span>
	    </span>
	    
	    <span class="comment-area">
	        <i id="post-comment" class="fa-solid fa-comments comment-icon-inactive"></i>
	        <span>${commentsDto.postComments}</span>
	    </span>
	</div>
	
	<!-- 댓글 섹션 -->
	<div id="comments-section" style="display: none;">
	    <div class="cell">
	        <div class="comments-list-area">댓글</div>
	    </div>
	
	    <div class="cell">
	        <c:choose>
	            <c:when test="${sessionScope.loginMemberId != null}">
	                <div class="comments-write-area">
	                    <div class="cell">
	                        <textarea class="comments-input" placeholder="댓글 내용" name="commentContent"></textarea>
	                    </div>
	                    <div class="cell">
	                        <button type="button" class="btn btn-positive comments-btn-write">등록하기</button>
	                    </div>
	                </div>
	            </c:when>
	            <c:otherwise>
	                <div class="comments-write-area">
	                    <div class="cell">
	                        <textarea rows="4" style="resize:none;" placeholder="로그인 후 작성이 가능합니다" disabled name="commentContent"></textarea>
	                    </div>
	                </div>
	            </c:otherwise>
	        </c:choose>
	    </div>
	</div>
	
	<!-- 하단 버튼 -->
	<div class="cell" style="margin-top: 20px;">
	    <a href="write" class="btn btn-positive">글쓰기</a>
	    <c:if test="${sessionScope.loginMemberId != null}">
	        <c:choose>
	            <c:when test="${sessionScope.loginMemberId == postDto.postWriter}">
	                <a href="edit?postNo=${postDto.postNo}" class="btn btn-neutral">수정</a>
	                <a href="delete?postNo=${postDto.postNo}" class="btn btn-neutral">삭제</a>
	            </c:when>
	            <c:when test="${sessionScope.loginMemberRole == '관리자'}">
	                <a href="delete?postNo=${postDto.postNo}" class="btn btn-neutral">삭제</a>
	            </c:when>
	        </c:choose>
	    </c:if>
	
	    <c:choose>
	        <c:when test="${not empty postDto.postMbti}">
	            <a href="${pageContext.request.contextPath}/post/mbti/list?mbti=${postDto.postMbti}" class="btn btn-neutral">목록으로</a>
	        </c:when>
	        <c:otherwise>
	            <a href="${pageContext.request.contextPath}/post/free/list" class="btn btn-neutral">목록으로</a>
	        </c:otherwise>
	    </c:choose>
	</div>
</div>

<jsp:include page="/WEB-INF/views/template/footer.jsp" />