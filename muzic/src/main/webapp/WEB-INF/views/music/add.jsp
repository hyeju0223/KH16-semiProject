<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/music/music-commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/music/music-form.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
<div class="container w-800 mt-40">

    <h2 class="mz-page-title"><i class="fa-solid fa-cloud-upload"></i> 음원 업로드</h2>
    <p class="mz-sub center mb-30">당신의 음악을 공유해보세요 🎧✨</p>

    <form action="add" method="post" enctype="multipart/form-data" id="musicAddForm">

        <!-- 장르 영역 -->
        <div class="genre-box mb-30">
            <div class="genre-box-title">🎵 장르 선택 <span class="red">*</span></div>
            <div class="genre-box-wrap">
                <c:forEach var="g" items="${genreList}">
                    <label class="genre-tag">
                        <input type="checkbox" name="musicGenreSet" value="${g}">
                        <span>#${g}</span>
                    </label>
                </c:forEach>
            </div>
            <small id="genreError" class="red" style="display:none;">장르를 1개 이상 선택해주세요</small>
        </div>

        <div class="upload-flex">

            <!-- 앨범 커버 -->
            <div class="cover-upload" id="coverDropArea">
                <img id="coverPreview" class="cover-preview" style="display:none;">
                <div class="cover-placeholder" id="coverPlaceholder">
                    <i class="fa-solid fa-plus"></i>
                    <span>앨범 커버 업로드</span>
                </div>
                <input type="file" name="coverImage" id="coverInput" accept="image/*" class="file-input">
                <button type="button" id="coverResetBtn" class="cover-reset-btn"><i class="fa-solid fa-xmark"></i></button>
            </div>

            <!-- 정보 입력 -->
            <div class="upload-fields">
                <label class="upload-label">곡 제목<span class="red">*</span></label>
                <input type="text" class="field mz-input" name="musicTitle" required placeholder="곡 제목">

                <label class="upload-label mt-10">아티스트<span class="red">*</span></label>
                <input type="text" class="field mz-input" name="musicArtist" required placeholder="아티스트">

                <label class="upload-label mt-10">앨범명<span class="red">*</span></label>
                <input type="text" class="field mz-input" name="musicAlbum" placeholder="앨범명">
            </div>
        </div>

        <!-- 음악 파일-->
        <div class="audio-upload-box" id="audioDropArea">
            <i class="fa-solid fa-music"></i>
            <span id="audioText">음악 파일을 업로드하세요<span class="red">*</span></span>
            <span id="musicDuration" class="duration-text"></span>

            <input type="file" name="musicFile" id="musicFileInput" accept="audio/*" class="file-input" required>
            <button type="button" id="audioResetBtn" class="audio-reset-btn"><i class="fa-solid fa-xmark"></i></button>
        </div>

        <button type="submit" class="mz-submit-btn mt-20">
            <i class="fa-solid fa-upload"></i> 업로드하기
        </button>
    </form>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/js/music/music-form.js"></script>