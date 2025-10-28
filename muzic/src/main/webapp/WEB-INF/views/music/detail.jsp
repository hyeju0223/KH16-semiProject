<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>${musicDto.musicTitle} - 상세보기</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f8f9fa;
            color: #333;
        }
        .music-detail {
            width: 800px;
            margin: 50px auto;
            padding: 30px;
            border-radius: 12px;
            background-color: #fff;
            box-shadow: 0 0 12px rgba(0,0,0,0.1);
        }
        .music-header {
            display: flex;
            align-items: center;
            gap: 30px;
            margin-bottom: 30px;
        }
        .music-cover {
            width: 250px;
            height: 250px;
            border-radius: 10px;
            object-fit: cover;
            background-color: #eee;
        }
        .music-info h2 {
            margin: 0;
            font-size: 26px;
        }
        .music-info p {
            margin: 6px 0;
            color: #555;
        }
        .music-audio {
            width: 100%;
            margin-top: 30px;
        }
        .btn-box {
            margin-top: 30px;
            text-align: right;
        }
        .btn {
            display: inline-block;
            padding: 10px 18px;
            background: #212529;
            color: white;
            border-radius: 6px;
            text-decoration: none;
            margin-left: 10px;
            transition: background 0.3s;
        }
        .btn:hover {
            background: #343a40;
        }
    </style>
</head>

<body>
    <div class="music-detail">

        <!-- 상단 정보 영역 -->
        <div class="music-header">
            <c:choose>
                <c:when test="${coverImageNo > 0}">
                    <img class="music-cover" 
                         src="./file?attachmentNo=${coverImageNo}" 
                         alt="앨범 커버">
                </c:when>
                <c:otherwise>
                    <img class="music-cover" 
                         src="/images/error/no-image.png" 
                         alt="기본 앨범 커버">
                </c:otherwise>
            </c:choose>

            <div class="music-info">
                <h2>${musicDto.musicTitle}</h2>
                <p><b>아티스트:</b> ${musicDto.musicArtist}</p>
                <p><b>앨범:</b> 
                    <c:choose>
                        <c:when test="${not empty musicDto.musicAlbum}">
                            ${musicDto.musicAlbum}
                        </c:when>
                        <c:otherwise>미등록</c:otherwise>
                    </c:choose>
                </p>
                <p><b>업로더:</b> ${musicDto.musicUploader}</p>
                <p><b>등록일:</b>
                    <fmt:formatDate value="${musicDto.musicUtime}" pattern="yyyy-MM-dd HH:mm"/>
                </p>
                <p><b>재생수:</b> ${musicDto.musicPlay}</p>
                <p><b>좋아요:</b> ${musicDto.musicLike}</p>
            </div>
        </div>

        <!-- 오디오 재생 영역 -->
        <div class="music-audio">
            <c:choose>
                <c:when test="${musicFileNo > 0}">
                    <audio controls>
                        <source src="./file?attachmentNo=${musicFileNo}" type="audio/mpeg">
                        브라우저가 오디오 태그를 지원하지 않습니다.
                    </audio>
                </c:when>
                <c:otherwise>
                    <p>음원 파일이 존재하지 않습니다.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 버튼 영역 -->
        <div class="btn-box">
            <a href="/music/list" class="btn">목록으로</a>
            <a href="/music/edit?musicNo=${musicDto.musicNo}" class="btn">수정</a>
        </div>
    </div>
</body>
</html>
