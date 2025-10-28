<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <title>음원 목록</title>
    <style>
        body { font-family: 'Pretendard', sans-serif; margin: 30px; background-color: #f9f9f9; color: #333; }
        h1 { text-align: center; margin-bottom: 25px; }

        /* 정렬 바 */
        .sort-bar {
            text-align: right;
            margin-bottom: 15px;
            font-size: 15px;
        }
        .sort-link {
            color: #555;
            text-decoration: none;
            margin-left: 15px;
            font-weight: 500;
        }
        .sort-link:hover {
            color: #000;
        }
        .sort-active {
            color: #000;
            font-weight: 700;
            border-bottom: 2px solid #000;
            padding-bottom: 3px;
        }

        table { width: 100%; border-collapse: collapse; background-color: white; }
        th, td { padding: 12px 10px; border-bottom: 1px solid #ddd; text-align: center; }
        th { background-color: #f0f0f0; }
        tr:hover { background-color: #f7f7f7; }

        .btn { display: inline-block; padding: 6px 12px; border-radius: 6px; text-decoration: none; color: white; }
        .btn-view { background-color: #3498db; }
        .btn-play { background-color: #2ecc71; }
        .btn-like { background-color: #e74c3c; }
        .empty { text-align: center; padding: 40px 0; color: #888; }
    </style>
</head>
<body>

<h1>음원 목록</h1>

<!-- 🔽 정렬 탭 -->
<div class="sort-bar">
    <span>정렬: </span>
    <a href="/music/list?sortType=latest"
       class="sort-link ${searchCondition.sortType == 'latest' or empty searchCondition.sortType ? 'sort-active' : ''}">
        최신순
    </a>
    |
    <a href="/music/list?sortType=like"
       class="sort-link ${searchCondition.sortType == 'like' ? 'sort-active' : ''}">
        좋아요순
    </a>
    |
    <a href="/music/list?sortType=play"
       class="sort-link ${searchCondition.sortType == 'play' ? 'sort-active' : ''}">
        재생순
    </a>
</div>

<!-- 🔽 목록 -->
<c:choose>
    <c:when test="${empty musicUserVO}">
        <div class="empty">등록된 음원이 없습니다.</div>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>번호</th>
                    <th>앨범</th>
                    <th>제목</th>
                    <th>아티스트</th>
                    <th>재생수</th>
                    <th>좋아요</th>
                    <th>등록일</th>
                    <th>관리</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="music" items="${musicUserVO}">
                    <tr>
                        <td>${music.musicNo}</td>
                        <td>${music.musicAlbum}</td>
                        <td>${music.musicTitle}</td>
                        <td>${music.musicArtist}</td>
                        <td>${music.musicPlay}</td>
                        <td>${music.musicLike}</td>
                        <td><fmt:formatDate value="${music.musicUtime}" pattern="yyyy-MM-dd"/></td>
                        <td>
                            <a href="/music/detail?musicNo=${music.musicNo}" class="btn btn-view">보기</a>
                            <a href="/music/play?musicNo=${music.musicNo}" class="btn btn-play">재생</a>
                            <a href="/music/like?musicNo=${music.musicNo}" class="btn btn-like">좋아요</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

</body>
</html>