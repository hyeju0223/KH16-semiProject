<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 · 회원 목록</title>
  <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>
<div class="admin-wrap">

  <!-- ===== 사이드바 (회원/상품/게임) ===== -->
 <!-- ===== 사이드바 ===== -->
<aside class="admin-sb">
  <div class="logo">MUZIC Admin</div>
  <c:set var="uri" value="${pageContext.request.requestURI}"/>
  <nav class="nav">
    <!-- 홈: 사이트 루트로 이동 -->
    <a href="<c:url value='/'/>"><span class="ico">🏠</span>홈</a>

    <!-- 기존 메뉴 -->
    <a class="${fn:contains(uri,'/admin/member') ? 'active' : ''}"
       href="<c:url value='/admin/member/list'/>"><span class="ico">👤</span>회원</a>
    <a class="${fn:contains(uri,'/admin/music') ? 'active' : ''}"
       href="<c:url value='/admin/music/list'/>"><span class="ico">🎵</span>음원</a>
    <a class="${fn:contains(uri,'/admin/goods') ? 'active' : ''}"
       href="<c:url value='/admin/goods/list'/>"><span class="ico">🛒</span>상품</a>
    <a class="${fn:contains(uri,'/admin/game') ? 'active' : ''}"
       href="<c:url value='/admin/game/list'/>"><span class="ico">🎮</span>게임</a>
  </nav>
</aside>


  <!-- ===== 메인 ===== -->
  <main class="admin-main">
    <div class="toolbar">
      <div class="breadcrumb">관리자 &rsaquo; 회원 &rsaquo; 목록</div>
    </div>

    <div class="page-title">회원 목록</div>

    <section class="card">
      <!-- 검색/필터 -->
      <form class="filters" method="get" action="${pageContext.request.contextPath}/admin/member/list">
        <c:set var="tSel" value="${empty pageVO.column ? 'all' : pageVO.column}"/>
        <select name="type" class="select">
          <option value="all"      ${tSel=='all'?'selected':''}>전체</option>
          <option value="id"       ${tSel=='id'?'selected':''}>아이디</option>
          <option value="email"    ${tSel=='email'?'selected':''}>이메일</option>
          <option value="name"     ${tSel=='name'?'selected':''}>이름</option>
          <option value="nickname" ${tSel=='nickname'?'selected':''}>닉네임</option>
        </select>

        <input class="input" type="text" name="keyword"
               value="${fn:escapeXml(pageVO.keyword)}" placeholder="검색어를 입력하세요">
        <input type="hidden" name="page" value="1">
        <input type="hidden" name="size" value="${pageVO.size}">
        <button class="btn key" type="submit">검색</button>

        <span class="count">전체 ${pageVO.allData}건</span>
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
            <td>
              <span class="badge ${row.blacklistYn=='Y'?'y':'n'}">${row.blacklistYn}</span>
            </td>
            <td>
              <a class="btn"
                 href="${pageContext.request.contextPath}/admin/member/detail?memberId=${row.memberId}">상세보기</a>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty list}">
          <tr><td colspan="11" style="text-align:center;color:#6b7280">데이터가 없습니다.</td></tr>
        </c:if>
        </tbody>
      </table>

      <%-- ===== 페이징(블록 이동: << >>, 중앙정렬) ===== --%>
      <c:set var="tp" value="${pageVO.totalPage}" />
      <c:set var="sp" value="${pageVO.strPage}" />
      <c:set var="rawEp" value="${pageVO.endPage}" />
      <c:set var="ep" value="${rawEp > tp ? tp : rawEp}" />

      <div class="pager pager-center">
        <%-- << 이전 블록 --%>
        <c:choose>
          <c:when test="${sp > 1}">
            <a class="nav"
               href="${pageContext.request.contextPath}/admin/member/list?page=${sp-1}&size=${pageVO.size}&type=${tSel}&keyword=${fn:escapeXml(pageVO.keyword)}">&lt;&lt;</a>
          </c:when>
          <c:otherwise>
            <span class="nav disabled">&lt;&lt;</span>
          </c:otherwise>
        </c:choose>

        <%-- 숫자 페이지 --%>
        <c:forEach var="p" begin="${sp}" end="${ep}">
          <a class="${p==pageVO.page?'active':''}"
             href="${pageContext.request.contextPath}/admin/member/list?page=${p}&size=${pageVO.size}&type=${tSel}&keyword=${fn:escapeXml(pageVO.keyword)}">
            ${p}
          </a>
        </c:forEach>

        <%-- >> 다음 블록 --%>
        <c:choose>
          <c:when test="${ep < tp}">
            <a class="nav"
               href="${pageContext.request.contextPath}/admin/member/list?page=${ep+1}&size=${pageVO.size}&type=${tSel}&keyword=${fn:escapeXml(pageVO.keyword)}">&gt;&gt;</a>
          </c:when>
          <c:otherwise>
            <span class="nav disabled">&gt;&gt;</span>
          </c:otherwise>
        </c:choose>
      </div>
    </section>
  </main>
</div>
</body>
</html>
