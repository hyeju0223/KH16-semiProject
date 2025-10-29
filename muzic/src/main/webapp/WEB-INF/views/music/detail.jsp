<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/music/detail.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
	
    <!-- 🔸 JS -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>

<body>
<div class="container w-600 mt-40">
    <div class="music-card" data-music-no="${musicDto.musicNo}">
        <!-- 🎵 앨범 커버 -->
        <div class="music-cover">
            <img src="/attachment/download/${coverAttachmentNo}" alt="앨범 커버" class="cover-img">

            <!-- ▶ 재생버튼 -->
            <div class="play-btn" data-music-no="${musicDto.musicNo}">
                <i class="fa-solid fa-play"></i>
            </div>
        </div>

        <!-- 🎧 정보 영역 -->
        <div class="music-info">
            <div class="music-title">
                <i class="fa-solid fa-music purple"></i> ${musicDto.musicTitle}
            </div>

            <div class="music-artist">
                <i class="fa-solid fa-user gray"></i> ${musicDto.musicArtist}
            </div>

            <div class="music-album">
                <i class="fa-solid fa-compact-disc gray"></i> ${musicDto.musicAlbum}
            </div>

            <div class="music-meta">
                <div class="music-time">
                    <i class="fa-solid fa-clock blue"></i>
                    <span>${musicDto.musicUtime}</span>
                </div>

                <div class="music-like">
                    <i class="fa-regular fa-heart"></i>
                    <span class="like-count">${musicDto.musicLike}</span>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>