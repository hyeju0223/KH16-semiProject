<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 · 게임 수정</title>
  <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>
<div class="admin-wrap">

  <!-- ===== 사이드바 ===== -->
  <aside class="admin-sb">
    <div class="logo">MUZIC Admin</div>
    <c:set var="uri" value="${pageContext.request.requestURI}"/>
    <nav class="nav">
      <a href="<c:url value='/'/>"><span class="ico">🏠</span>홈</a>
      <a class="${fn:contains(uri,'/admin/member') ? 'active' : ''}"
         href="<c:url value='/admin/member/list'/>"><span class="ico">👤</span>회원</a>
      <a class="${fn:contains(uri,'/admin/goods') ? 'active' : ''}"
         href="<c:url value='/admin/goods/list'/>"><span class="ico">🛒</span>상품</a>
      <a class="${fn:contains(uri,'/admin/game') ? 'active' : ''}"
         href="<c:url value='/admin/game/list'/>"><span class="ico">🎮</span>게임</a>
    </nav>
  </aside>

  <!-- ===== 메인 ===== -->
  <main class="admin-main">
    <div class="crumb">관리자 &rsaquo; 게임 &rsaquo; 수정</div>
    <div class="page-title">게임 정보 수정</div>

    <section class="card card-large" style="margin-top:12px">
      <div class="row" style="justify-content:space-between; align-items:center; margin-bottom:10px">
        <div class="chip">기본 정보</div>
        <div class="row gap-8">
          <a class="btn btn-soft"
             href="<c:url value='/admin/game/detail'><c:param name='rouletteNo' value='${game.rouletteNo}'/></c:url>">상세로 돌아가기</a>
          <a class="btn btn-ghost" href="<c:url value='/admin/game/list'/>">목록으로</a>
        </div>
      </div>

      <!-- 이름만 수정 -->
      <form method="post" action="<c:url value='/admin/game/edit'/>">
        <input type="hidden" name="rouletteNo" value="${game.rouletteNo}">
        <table class="table">
          <tbody>
          <tr>
            <th style="width:160px">게임 번호</th>
            <td class="muted" colspan="3">${game.rouletteNo}</td>
          </tr>
          <tr>
            <th>게임명</th>
            <td colspan="3">
              <input class="input input-lg w-400" name="rouletteName"
                     value="${game.rouletteName}" maxlength="100" required>
            </td>
          </tr>
          </tbody>
        </table>

        <div class="row" style="justify-content:flex-end; gap:8px; margin-top:12px">
          <a class="btn btn-danger btn-lg"
             href="<c:url value='/admin/game/detail'><c:param name='rouletteNo' value='${game.rouletteNo}'/></c:url>">취소</a>
          <button class="btn btn-primary btn-lg" type="submit">저장</button>
        </div>
      </form>
    </section>
  </main>
</div>
</body>
</html>
