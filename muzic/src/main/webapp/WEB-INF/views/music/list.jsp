<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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

	<div class="mz-title mb-20">Muzic Library</div>
	<div class="mz-sub mb-30">K-뮤직 감성 플레이리스트를 만나보세요 🎧</div>
	<div class="page-header-line"></div>

	<!-- Sort bar -->
	<div class="music-sort-bar mb-20">
		<a href="/music/list?sortType=latest"
			class="${param.sortType=='latest' ? 'on' : ''}">최신순</a> <a
			href="/music/list?sortType=like"
			class="${param.sortType=='like' ? 'on' : ''}">좋아요순</a> <a
			href="/music/list?sortType=play"
			class="${param.sortType=='play' ? 'on' : ''}">재생순</a> <a
			href="/music/list?sortType=title"
			class="${param.sortType=='title' ? 'on' : ''}">가나다순</a>
	</div>

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
						onclick="event.stopPropagation(); location.href='/music/detail?musicNo=${m.musicNo}'"> <!-- 이벤트가 부모에게 전파 x -->
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
			<i class="fa-solid fa-magnifying-glass "></i> 총 음원 수:
			${searchCondition.allData}개
		</div>
	</c:if>

</div>

<div>
	<jsp:include page="/WEB-INF/views/template/pagination.jsp"></jsp:include>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
<script src="/js/music/like.js"></script>
