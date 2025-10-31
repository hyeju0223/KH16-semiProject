<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="/css/music/search-bar.css">

<div class="mz-search-container">
    <form action="/music/search/list" method="get" class="mz-search-box" autocomplete="off">

        <input type="text"
               id="mz-search-input"
               name="keyword"
               placeholder="노래, 아티스트, 앨범 검색"
               value="${param.keyword}">

        <button type="submit" class="mz-search-btn">
            <i class="fa-solid fa-magnifying-glass"></i>
        </button>

        <!-- 미리보기 -->
        <div id="mz-search-preview" class="mz-search-preview"></div>
    </form>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="/js/music/search-bar.js"></script>

<link rel="stylesheet" href="/css/music/search-bar.css">

