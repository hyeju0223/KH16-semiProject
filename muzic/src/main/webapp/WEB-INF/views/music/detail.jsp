<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/css/commons.css">
<link rel="stylesheet" href="/css/music/music-commons.css">
<link rel="stylesheet" href="/css/music/detail.css">
<link rel="stylesheet" href="/css/music/like.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<script>
	window.isLogin = "${sessionScope.loginMemberId != null ? 'true' : 'false'}";

	$(document).ready(function() {

		var musicNo = $(".music-detail-card").data("music-no");
		var audio = document.getElementById("music-player");

		var playedOnce = false; // 페이지 진입마다 초기화

		audio.addEventListener("play", function() {

			if (playedOnce) return;
			playedOnce = true;

			$.ajax({
			    url: "/rest/music/play",
			    method: "POST",
			    data: { musicNo: musicNo },
			    success: function (isFirstPlay) {

			        // 서버에서 true 준 경우만 +1
			        if (isFirstPlay == true) { // 문자열 true 도 허용
			            var $cnt = $(".play-count");
			            var cur = parseInt($cnt.text(), 10) || 0;
			            $cnt.text(cur + 1);
			        }
			    }
			});
		});
	});
	function requestDelete() {
	    if(confirm("정말 삭제 요청하시겠습니까?")) {
	        document.getElementById('deleteForm').submit();
	    }
	}
</script>
<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
<div class="music-detail-page">

	<jsp:include page="/WEB-INF/views/music/template/search-bar.jsp"></jsp:include>

	<div class="music-detail-card" data-music-no="${musicUserVO.musicNo}">
		<div class="music-cover-wrap">
			<img src="/music/file?attachmentNo=${musicUserVO.coverAttachmentNo}"
			onerror="this.onerror=null; this.src='/images/error/music-no-image.png';"
				class="music-cover-img">

			<div class="music-play-btn">
				<i class="fa-regular fa-circle-play"></i>
			</div>
		</div>

		<h2 class="music-title">${musicUserVO.musicTitle}</h2>
		<p class="music-artist">${musicUserVO.musicArtist}</p>

		<p class="music-meta">Album: ${musicUserVO.musicAlbum}</p>
		<p class="music-meta">Uploaded by: ${musicUserVO.uploaderNickname}</p>

		<!-- 장르 -->
		<div class="music-genres">
			<c:forEach var="g" items="${musicUserVO.musicGenres}">
				<span class="genre-tag">#${g}</span>
			</c:forEach>
		</div>

		<div class="music-stats">

			<!-- 좋아요 -->
			<span
				class="stat-item like-area detail-like
    				${sessionScope.loginMemberId == null ? 'disabled-like' : ''}"
				data-music-no="${musicUserVO.musicNo}"> <i
				class="fa-regular fa-heart like-btn"></i> <span class="like-count">${musicUserVO.musicLike}</span>
			</span>
			<!-- 재생수 -->
			<span class="stat-item"> <i class="fa-solid fa-headphones"></i>
				<span class="play-count">${musicUserVO.musicPlay}</span>
			</span>

		</div>

		<audio id="music-player" preload="metadata">
			<source
				src="/music/file?attachmentNo=${musicUserVO.musicFileAttachmentNo}">
		</audio>

		<div class="player-ui">
			<span id="current-time">00:00</span> <input type="range"
				id="seek-bar" value="0" min="0" max="100"> <span
				id="total-time">--:--</span>
		</div>
		<!-- 수정 / 삭제 버튼 -->
		<c:if
			test="${sessionScope.loginMemberId != null 
        && sessionScope.loginMemberId == uploaderId}">
			<div class="music-owner-actions mt-30">
				<button class="mz-btn-edit me-50"
					onclick="location.href='/music/edit?musicNo=${musicUserVO.musicNo}'">
					✏️ 수정하기</button>
				<button class="mz-btn-delete" 
					onclick="requestDelete()">🚫 삭제요청</button>
			</div>
			 <!-- POST 요청 폼 -->
	        <form id="deleteForm" action="/music/delete-request" method="post" style="display:none;">
	            <input type="hidden" name="musicNo" value="${musicUserVO.musicNo}">
	        </form>
		</c:if>
	</div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
<script src="/js/music/like.js"></script>
<script src="/js/music/detail.js"></script>

