<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Font Awesome (CDN) -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<style>
/* 기본 스타일 */
.search-container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 30px auto;
    width: 100%;
    max-width: 500px;
}

.search-box {
    position: relative;
    width: 100%;
}

.search-box input[type="text"] {
    width: 100%;
    padding: 10px 40px 10px 15px;
    border: 1px solid #ccc;
    border-radius: 50px;
    outline: none;
    font-size: 15px;
    transition: all 0.2s ease;
}

.search-box input[type="text"]:focus {
    border-color: #0078ff;
    box-shadow: 0 0 5px rgba(0, 120, 255, 0.3);
}

.search-box button {
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    border: none;
    background: none;
    color: #555;
    font-size: 17px;
    cursor: pointer;
    transition: color 0.2s ease;
}

.search-box button:hover {
    color: #0078ff;
}
</style>


<!-- 실제 검색 폼 -->
<div class="search-container">
    <form action="/music/search/list" method="get" class="search-box" autocomplete="off" >
        <input type="text" id="search-input" name="keyword" placeholder="검색어를 입력하세요" value="${param.keyword}">
        <button type="submit">
            <i class="fa-solid fa-magnifying-glass"></i>
        </button>
        <!-- ✅ 미리보기 박스 추가 -->
        <div id="search-preview"
             style="display:none; position:absolute; top:45px; left:0; width:100%; background:white; border:1px solid #ccc; border-radius:10px; z-index:1000; box-shadow:0 4px 8px rgba(0,0,0,0.1);">
        </div>
    </form>
</div>