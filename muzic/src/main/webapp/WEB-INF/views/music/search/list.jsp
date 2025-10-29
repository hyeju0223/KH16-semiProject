<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


    <link rel="stylesheet" href="/css/common.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="/js/music-search.js"></script>
    <style>
        body { font-family: 'Pretendard', sans-serif; background-color: #fafafa; margin: 30px; }
        h2 { margin-bottom: 20px; }
        .result-container { background: white; border-radius: 10px; padding: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .result-item { border-bottom: 1px solid #eee; padding: 12px 0; }
        .result-item:last-child { border-bottom: none; }
        .meta { color: #666; font-size: 0.9em; }
        .pagination { margin-top: 20px; text-align: center; }
        .pagination a { margin: 0 4px; text-decoration: none; color: #333; }
        .pagination a.active { font-weight: bold; color: #007BFF; }
        .sort-bar { margin-bottom: 15px; }
        .sort-bar select { padding: 6px; border-radius: 6px; }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/views/template/music-search-bar.jsp" />

<div class="result-container">

    <h2>
        <c:choose>
            <c:when test="${selectedColumn eq 'music_title'}">🎵 제목으로 검색된 결과</c:when>
            <c:when test="${selectedColumn eq 'music_artist'}">🎤 가수명으로 검색된 결과</c:when>
            <c:when test="${selectedColumn eq 'music_album'}">💿 앨범명으로 검색된 결과</c:when>
            <c:otherwise>🔍 검색 결과</c:otherwise>
        </c:choose>
    </h2>

    <div class="sort-bar">
        <form action="/music/search/list" method="get">
            <input type="hidden" name="keyword" value="${searchCondition.keyword}">
            <select name="sortType" onchange="this.form.submit()">
                <option value="accuracy" ${searchCondition.sortType eq 'accuracy' ? 'selected' : ''}>정확도순</option>
                <option value="latest" ${searchCondition.sortType eq 'latest' ? 'selected' : ''}>최신순</option>
                <option value="like" ${searchCondition.sortType eq 'like' ? 'selected' : ''}>좋아요순</option>
                <option value="play" ${searchCondition.sortType eq 'play' ? 'selected' : ''}>조회순</option>
            </select>
        </form>
    </div>

    <c:if test="${empty musicUserVO}">
        <p style="text-align:center; color:#666;">검색 결과가 없습니다.</p>
    </c:if>

    <c:forEach var="music" items="${musicUserVO}">
        <div class="result-item">
            <strong>${music.musicTitle}</strong>
            <div class="meta">
                가수: ${music.musicArtist} / 앨범: ${music.musicAlbum} <br>
                ❤️ 좋아요: ${music.musicLike} / ▶ 재생: ${music.musicPlay}
            </div>
        </div>
    </c:forEach>

    <div class="pagination">
        <c:forEach var="i" begin="${searchCondition.startPage}" end="${searchCondition.endPage}">
            <a href="/music/search/list?page=${i}&${searchCondition.params}"
               class="${i eq searchCondition.page ? 'active' : ''}">
                ${i}
            </a>
        </c:forEach>
    </div>

</div>

</body>
</html>