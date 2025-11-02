<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/css/commons.css">
<link rel="stylesheet" href="/css/music/music-commons.css">
<link rel="stylesheet" href="/css/music/music-form.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<div class="container w-800 mt-40">

    <h2 class="mz-page-title"><i class="fa-solid fa-pen-nib"></i> 음원 수정</h2>
    <p class="mz-sub center mb-30"
   style="background: #ffe6f2; color:#d63384; font-weight:600; padding:10px 15px; border-radius:10px; display:inline-block;">
   음원 수정을 신청하면 관리자의 승인 이후에 전체 목록에 표기됩니다. 🙏
</p>

    <form action="/music/edit" method="post" enctype="multipart/form-data" id="musicEditForm">

        <input type="hidden" name="musicNo" value="${param.musicNo}">

        <!-- 장르 영역 -->
        <div class="genre-box mb-30">
            <div class="genre-box-title">🎵 장르 선택 <span class="red">*</span></div>
            <div class="genre-box-wrap">
                <c:forEach var="g" items="${genreList}">
                    <label class="genre-tag">
                        <input type="checkbox" name="musicGenreSet" value="${g}"
                            <c:if test="${musicFormDto.musicGenreSet.contains(g)}">checked</c:if>>
                        <span>#${g}</span>
                    </label>
                </c:forEach>
            </div>
        </div>

        <div class="upload-flex">

            <!-- 기존 커버 이미지 + 변경 -->
            <div class="cover-upload" id="coverDropArea">

			    <c:choose>
			        <c:when test="${coverImageNo > 0}">
			            <!-- 기존 커버 표시 -->
			            <img src="/music/file?attachmentNo=${coverImageNo}"
			                 id="coverPreview" class="cover-preview">
			            <div id="coverPlaceholder" class="cover-placeholder" style="display:none;">
			                <i class="fa-solid fa-plus"></i>
			                <span>앨범 커버 업로드</span>
			            </div>
			        </c:when>
			        <c:otherwise>
			            <!-- 커버가 없을 때: 이미지 숨기고 placeholder만 -->
			            <img id="coverPreview" class="cover-preview" style="display:none;">
			            <div id="coverPlaceholder" class="cover-placeholder">
			                <i class="fa-solid fa-plus"></i>
			                <span>앨범 커버 업로드</span>
			            </div>
			        </c:otherwise>
			    </c:choose>
			
			    <input type="file" name="coverImage" id="coverInput" accept="image/*" class="file-input">
			    <button type="button" id="coverResetBtn" class="cover-reset-btn" style="display:none;">
			        <i class="fa-solid fa-xmark"></i>
			    </button>
			</div>

            <!-- 수정 영역 -->
            <div class="upload-fields">
                <label class="upload-label">곡 제목<span class="red">*</span></label>
                <input type="text" class="field mz-input" name="musicTitle"
                       value="${musicFormDto.musicTitle}" required>

                <label class="upload-label mt-10">아티스트<span class="red">*</span></label>
                <input type="text" class="field mz-input" name="musicArtist"
                       value="${musicFormDto.musicArtist}" required>

                <label class="upload-label mt-10">앨범명<span class="red">*</span></label>
                <input type="text" class="field mz-input" name="musicAlbum"
                       value="${musicFormDto.musicAlbum}">
            </div>
        </div>

        <!-- 음원 파일 (선택) -->
        <div class="audio-upload-box" id="audioDropArea">
            <i class="fa-solid fa-music"></i>
            <span id="audioText">음악 파일 변경 (선택)</span>
            <span id="musicDuration" class="duration-text"></span>

            <input type="file" name="musicFile" id="musicFileInput" accept="audio/*" class="file-input">
            <button type="button" id="audioResetBtn" class="audio-reset-btn"><i class="fa-solid fa-xmark"></i></button>
        </div>

        <button type="submit" class="mz-submit-btn mt-20">
            <i class="fa-solid fa-gear"></i> 수정하기
        </button>

    </form>
</div>

<script src="/js/music/music-form.js"></script>