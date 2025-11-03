<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<link rel="stylesheet" href="/css/commons.css">
<link rel="stylesheet" href="/css/music/music-commons.css">
<link rel="stylesheet" href="/css/music/list.css">
<link rel="stylesheet" href="/css/music/like.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<script>
	var isLogin = "${sessionScope.loginMemberId != null ? 'true' : 'false'}";
</script>
<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
<div class="container w-1100 mt-40">

	<jsp:include page="/WEB-INF/views/music/template/search-bar.jsp"></jsp:include>
	<div class="mz-title mb-20">검색 결과</div>
	<div class="mz-sub mb-30">"${param.keyword}" 검색 결과입니다 🎧</div>
	<div class="page-header-line"></div>

	<div class="mz-search-tabs mb-20">
		<a
			href="/music/search/list?keyword=${param.keyword}&column=music_title"
			class="${param.column=='music_title' ? 'on' : ''}">곡</a> <a
			href="/music/search/list?keyword=${param.keyword}&column=music_artist"
			class="${param.column=='music_artist' ? 'on' : ''}">아티스트</a> <a
			href="/music/search/list?keyword=${param.keyword}&column=music_album"
			class="${param.column=='music_album' ? 'on' : ''}">앨범</a>
	</div>
	<!-- Sort bar -->
	<div class="music-sort-bar mb-20">
		<a
			href="/music/search/list?sortType=accuracy&keyword=${param.keyword}${param.column != null ? '&column='.concat(param.column) : ''}"
			class="${param.sortType=='accuracy' ? 'on' : ''}">관련도순</a> <a
			href="/music/search/list?sortType=like&keyword=${param.keyword}${param.column != null ? '&column='.concat(param.column) : ''}"
			class="${param.sortType=='like' ? 'on' : ''}">좋아요순</a> <a
			href="/music/search/list?sortType=play&keyword=${param.keyword}${param.column != null ? '&column='.concat(param.column) : ''}"
			class="${param.sortType=='play' ? 'on' : ''}">재생순</a>
	</div>
	<c:if test="${empty musicUserVO}">
		<c:if test="${empty musicUserVO}">
			<c:choose>
				<c:when test="${fn:contains(selectedColumn, 'title')}">
					<div
						style="text-align: center; margin-top: 40px; font-size: 18px; color: #888;">
						곡으로 검색한 음원이 없습니다 😢</div>
				</c:when>
				<c:when test="${fn:contains(selectedColumn, 'artist')}">
					<div
						style="text-align: center; margin-top: 40px; font-size: 18px; color: #888;">
						가수로 검색한 음원이 없습니다 😢</div>
				</c:when>
				<c:when test="${fn:contains(selectedColumn, 'album')}">
					<div
						style="text-align: center; margin-top: 40px; font-size: 18px; color: #888;">
						앨범으로 검색한 음원이 없습니다 😢</div>
				</c:when>
				<c:otherwise>
					<div
						style="text-align: center; margin-top: 40px; font-size: 18px; color: #888;">
						음원 결과가 없습니다 😢</div>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:if>
	<!-- Grid -->
	<div class="music-grid">
		<c:forEach var="m" items="${musicUserVO}">
			<div class="music-card" data-music-no="${m.musicNo}"
				onclick="location.href='/music/detail?musicNo=${m.musicNo}'">

				<div class="cover-wrap">
					<img class="cover-img"
						src="/music/file?attachmentNo=${m.coverAttachmentNo}"
						onerror="this.src='/images/error/music-no-image.png'">

					<button class="play-btn"
						onclick="event.stopPropagation(); location.href='/music/detail?musicNo=${m.musicNo}'">
						<i class="fa-solid fa-play"></i>
					</button>
				</div>

				<div class="title">${m.musicTitle}</div>
				<div class="artist">${m.musicArtist}</div>

				<div class="music-stats">

					<!-- 좋아요 버튼 -->
					<span
						class="stat like-area list-like 
                            ${sessionScope.loginMemberId == null ? 'disabled-like' : ''}"
						data-music-no="${m.musicNo}"> <i
						class="fa-regular fa-heart like-btn"></i> <span class="like-count">${m.musicLike}</span>
					</span> <span class="stat"> <i class="fa-solid fa-headphones"></i>
						${m.musicPlay}
					</span>
				</div>

			</div>
		</c:forEach>
	</div>
	<c:if test="${searchCondition.allData > 0}">
		<div class="mz-search-count mt-30 me-20">
			<i class="fa-solid fa-magnifying-glass "></i> 검색결과:
			${searchCondition.allData}개
		</div>
	</c:if>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/music/template/pagination.jsp"></jsp:include>

<script src="/js/music/like.js"></script>
