<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/css/commons.css">
<link rel="stylesheet" href="/css/music/music-commons.css">
<link rel="stylesheet" href="/css/music/list.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<div class="container w-1100 mt-40">
	
	<jsp:include page="/WEB-INF/views/music/search-bar.jsp" ></jsp:include>
	
	<div class="mz-title mb-20">Muzic Library</div>
	<div class="mz-sub mb-30">K-뮤직 감성 플레이리스트를 만나보세요 🎧</div>
	<div class="page-header-line"></div>

	<!-- Sort bar -->
	<div class="music-sort-bar mb-20">
		<a href="/music/list?sortType=latest" class="${param.sortType=='latest' ? 'on' : ''}">최신순</a>
		<a href="/music/list?sortType=like"   class="${param.sortType=='like' ? 'on' : ''}">좋아요순</a>
		<a href="/music/list?sortType=play"   class="${param.sortType=='play' ? 'on' : ''}">조회순</a>
	</div>

	<!-- Grid -->
	<div class="music-grid">
		<c:forEach var="m" items="${musicUserVO}">
			<div class="music-card" onclick="location.href='/music/detail?musicNo=${m.musicNo}'">

				<div class="cover-wrap">
					<img class="cover-img"
						src="/music/file?attachmentNo=${m.coverAttachmentNo}"
						onerror="this.src='/images/error/no-image.png'">

					<button class="play-btn"
						onclick="event.stopPropagation(); location.href='/music/detail?musicNo=${m.musicNo}'">
						<i class="fa-solid fa-play"></i>
					</button>
				</div>

				<div class="title">${m.musicTitle}</div>
				<div class="artist">${m.musicArtist}</div>

				<div class="music-stats">
					<div class="stat like-area" onclick="toggleLike(this, event)">
						<i class="fa-regular fa-heart"></i> ${m.musicLike}
					</div>

					<div class="stat">
						<i class="fa-solid fa-headphones"></i> ${m.musicPlay}
					</div>
				</div>

			</div>
		</c:forEach>
	</div>
</div>
<script src="/js/music/list.js"></script>