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
</script>

<div class="music-detail-page">

	<jsp:include page="/WEB-INF/views/music/search-bar.jsp"></jsp:include>

	<div class="music-detail-card" data-music-no="${musicUserVO.musicNo}">
		<div class="music-cover-wrap">
			<img src="/music/file?attachmentNo=${musicUserVO.coverAttachmentNo}"
				class="music-cover-img">

			<div class="music-play-btn">
				<i class="fa-regular fa-circle-play"></i>
			</div>
		</div>

		<h2 class="music-title">${musicUserVO.musicTitle}</h2>
		<p class="music-artist">${musicUserVO.musicArtist}</p>

		<p class="music-meta">Album: ${musicUserVO.musicAlbum}</p>
		<p class="music-meta">Uploaded by: ${musicUserVO.uploaderNickname}</p>

		<!-- 🎼 Genres -->
		<div class="music-genres">
			<c:forEach var="g" items="${musicUserVO.musicGenres}">
				<span class="genre-tag">#${g}</span>
			</c:forEach>
		</div>

		<div class="music-stats">

			<!-- ❤️ Like -->
			<span
				class="stat-item like-area detail-like
    				${sessionScope.loginMemberId == null ? 'disabled-like' : ''}"
				data-music-no="${musicUserVO.musicNo}"> <i
				class="fa-regular fa-heart like-btn"></i> <span class="like-count">${musicUserVO.musicLike}</span>
			</span>
			<!-- 🎧 Plays -->
			<span class="stat-item"> <i class="fa-solid fa-headphones"></i>
				<span>${musicUserVO.musicPlay}</span>
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
	</div>
</div>

<script src="/js/music/like.js"></script>
<script src="/js/music/detail.js"></script>

