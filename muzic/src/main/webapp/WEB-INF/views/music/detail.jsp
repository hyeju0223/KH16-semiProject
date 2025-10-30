<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/music/detail.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"></script>

<div class="container w-800 mt-40">
    
    <div class="music-detail-wrapper flex gap-30">

        <!-- 앨범 커버 영역 -->
        <div class="album-box relative">
            <img 
                class="album-cover"
                src="<c:choose>
                        <c:when test='${coverAttachmentNo > 0}'>
                            /attachment/download/${coverAttachmentNo}
                        </c:when>
                        <c:otherwise>
                            /images/default_cover.png
                        </c:otherwise>
                    </c:choose>"
                alt="cover"
            >

            <!-- 재생 버튼 -->
            <button id="play-btn" class="play-button center-abs">
                <i class="fa-solid fa-play"></i>
            </button>

            <!-- 재생 시간 -->
            <div class="duration-box">03:00</div>
        </div>

        <!-- 정보 -->
        <div class="info-box flex-col gap-10">
            <h2 class="music-title">${musicDto.musicTitle}</h2>
            <p class="artist">👤 ${musicDto.musicArtist}</p>

            <c:if test="${not empty musicDto.musicAlbum}">
                <p class="album">💿 ${musicDto.musicAlbum}</p>
            </c:if>

            <p class="uploader">
                업로더: 
                <c:choose>
                    <c:when test="${not empty musicUserVO.memberNickname}">
                        ${musicUserVO.memberNickname}
                    </c:when>
                    <c:otherwise>
                        탈퇴회원
                    </c:otherwise>
                </c:choose>
            </p>

            <p class="meta">업로드: ${musicDto.musicUtime}</p>
            <p class="meta">재생수: ${musicDto.musicPlay}</p>
            <p class="meta">좋아요: ${musicDto.musicLike}</p>

            <!-- 좋아요 버튼 -->
            <form action="/music/like" method="post">
                <input type="hidden" name="musicNo" value="${musicDto.musicNo}">
                <button class="btn-primary w-100">❤️ 좋아요</button>
            </form>

        </div>
    </div>

    <!-- 오디오 -->
    <audio id="audio-player" style="display:none;">
        <source src="/attachment/download/${musicAttachmentNo}" type="audio/mpeg">
    </audio>
</div>

<script>
$(function(){
    const audio = $("#audio-player")[0];

    $("#play-btn").click(function(){
        if(audio.paused){
            audio.play();
            $(this).html('<i class="fa-solid fa-pause"></i>');
        } else {
            audio.pause();
            $(this).html('<i class="fa-solid fa-play"></i>');
        }
    });
});
</script>