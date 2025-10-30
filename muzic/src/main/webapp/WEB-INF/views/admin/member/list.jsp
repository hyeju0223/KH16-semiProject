<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
  String uri = request.getRequestURI();
  String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 · 회원 목록</title>
  <link rel="stylesheet" href="<%=ctx%>/css/admin.css">
</head>
<body>
<div class="admin-wrap">

  <!-- 사이드바 -->
  <aside class="admin-sb">
    <div class="logo">MUZIC Admin</div>
    <nav class="nav">
      <a class="<c:out value='${fn:contains(uri,"/admin/member")?"active":""}'/>" href="<%=ctx%>/admin/member/list"><span class="ico">👤</span>회원</a>
      <a class="<c:out value='${fn:contains(uri,"/admin/goods")?"active":""}'/>"  href="<%=ctx%>/admin/goods/list"><span class="ico">🛒</span>상품</a>
      <a class="<c:out value='${fn:contains(uri,"/admin/game")?"active":""}'/>"   href="<%=ctx%>/admin/game/list"><span class="ico">🎮</span>게임</a>
      <a class="<c:out value='${fn:contains(uri,"/admin/help")?"active":""}'/>"   href="<%=ctx%>/admin/help/list"><span class="ico">💬</span>고객센터</a>
    </nav>
  </aside>

  <!-- 메인 -->
  <main class="admin-main">
    <div class="toolbar">
      <div class="breadcrumb">관리자 &rsaquo; 회원 &rsaquo; 목록</div>
    </div>

    <div class="page-title">회원 목록</div>

    <section class="card">
      <!-- 검색/필터 -->
      <form class="filters" method="get" action="<%=ctx%>/admin/member/list">
        <select name="type" class="select">
          <c:set var="t" value="${empty param.type ? 'all' : param.type}"/>
          <option value="all"      ${t=='all'?'selected':''}>전체</option>
          <option value="id"       ${t=='id'?'selected':''}>아이디</option>
          <option value="email"    ${t=='email'?'selected':''}>이메일</option>
          <option value="name"     ${t=='name'?'selected':''}>이름</option>
          <option value="nickname" ${t=='nickname'?'selected':''}>닉네임</option>
        </select>
        <input class="input" type="text" name="keyword" value="${fn:escapeXml(param.keyword)}" placeholder="검색어를 입력하세요">
        <input type="hidden" name="page" value="1">
        <button class="btn key" type="submit">검색</button>
        <span class="count">전체 ${totalCount}건</span>
      </form>

      <!-- 테이블 -->
      <table class="table">
        <thead>
        <tr>
          <th>아이디</th>
          <th>닉네임</th>
          <th>이름</th>
          <th>이메일</th>
          <th>MBTI</th>
          <th>생년월일</th>
          <th>연락처</th>
          <th>권한</th>
          <th>포인트</th>
          <th>최근로그인</th>
          <th>블랙리스트</th>
          <th>상세보기</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" items="${list}">
          <tr>
            <td>${row.memberId}</td>
            <td>${row.memberNickname}</td>
            <td>${row.memberName}</td>
            <td>${row.memberEmail}</td>
            <td>${row.memberMbti}</td>
            <td>${row.memberBirth}</td>
            <td>${row.memberContact}</td>
            <td>${row.memberRole}</td>
            <td>${row.memberPoint}</td>
            <td><c:out value="${row.memberLogin}"/></td>
            <td>
              <span class="badge ${row.blacklistYn=='Y'?'y':'n'}">
                ${row.blacklistYn}
              </span>
            </td>
            <td>
              <a class="btn" href="<%=ctx%>/admin/member/detail?memberId=${row.memberId}">상세보기</a>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty list}">
          <tr><td colspan="12" style="text-align:center;color:#6b7280">데이터가 없습니다.</td></tr>
        </c:if>
        </tbody>
      </table>

      <!-- 페이징 -->
      <div class="pager">
        <c:forEach var="p" begin="1" end="${lastPage}">
          <a class="${p==page?'active':''}"
             href="<%=ctx%>/admin/member/list?type=${t}&keyword=${fn:escapeXml(param.keyword)}&page=${p}&size=${size}">${p}</a>
        </c:forEach>
      </div>
    </section>
  </main>
</div>
</body>
</html>
