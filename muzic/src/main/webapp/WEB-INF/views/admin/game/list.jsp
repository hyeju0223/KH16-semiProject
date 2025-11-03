<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 · 게임 목록</title>
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
    <div class="toolbar">
      <div class="breadcrumb">관리자 &rsaquo; 게임 &rsaquo; 목록</div>
    </div>

    <div class="page-title">게임 목록</div>

    <section class="card">
      <!-- 검색: 이름만 -->
      <form class="filters" method="get" action="<c:url value='/admin/game/list'/>">
        <input class="input" type="text" name="keyword"
               value="${fn:escapeXml(pageVO.keyword)}" placeholder="게임 이름 검색">
        <input type="hidden" name="page" value="1">
        <input type="hidden" name="size" value="${pageVO.size}">
        <button class="btn key" type="submit">검색</button>

        <span class="count">전체 ${pageVO.allData}건</span>
      </form>

      <!-- 테이블 -->
      <table class="table">
        <thead>
        <tr>
          <th style="width:100px">번호</th>
          <th>게임명</th>
          <th style="width:140px">일일횟수</th>
          <th style="width:160px">최대포인트</th>
          <th style="width:160px">최소포인트</th>
          <th style="width:160px">시작일</th>
          <th style="width:140px">상세</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" items="${list}">
          <tr>
            <td>${row.rouletteNo}</td>
            <td>${row.rouletteName}</td>
            <td>${row.rouletteDailyCount}</td>
            <td><c:out value="${row.rouletteMaxPoint}"/></td>
            <td><c:out value="${row.rouletteMinPoint}"/></td>
            <td>${row.rouletteDate}</td>
            <td>
              <a class="btn" href="<c:url value='/admin/game/detail'><c:param name='rouletteNo' value='${row.rouletteNo}'/></c:url>">상세보기</a>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty list}">
          <tr><td colspan="7" style="text-align:center;color:#6b7280">데이터가 없습니다.</td></tr>
        </c:if>
        </tbody>
      </table>

      <!-- 페이징 (블록 네비게이션) -->
      <c:set var="tp" value="${pageVO.totalPage}" />
      <c:set var="sp" value="${pageVO.strPage}" />
      <c:set var="rawEp" value="${pageVO.endPage}" />
      <c:set var="ep" value="${rawEp > tp ? tp : rawEp}" />

      <div class="pager pager-center">
        <!-- << -->
        <c:choose>
          <c:when test="${sp > 1}">
            <a class="nav"
               href="<c:url value='/admin/game/list'><c:param name='page' value='${sp-1}'/><c:param name='size' value='${pageVO.size}'/><c:param name='keyword' value='${pageVO.keyword}'/></c:url>">&laquo;</a>
          </c:when>
          <c:otherwise><span class="nav disabled">&laquo;</span></c:otherwise>
        </c:choose>

        <!-- numbers -->
        <c:forEach var="p" begin="${sp}" end="${ep}">
          <a class="${p==pageVO.page?'active':''}"
             href="<c:url value='/admin/game/list'><c:param name='page' value='${p}'/><c:param name='size' value='${pageVO.size}'/><c:param name='keyword' value='${pageVO.keyword}'/></c:url>">${p}</a>
        </c:forEach>

        <!-- >> -->
        <c:choose>
          <c:when test="${ep < tp}">
            <a class="nav"
               href="<c:url value='/admin/game/list'><c:param name='page' value='${ep+1}'/><c:param name='size' value='${pageVO.size}'/><c:param name='keyword' value='${pageVO.keyword}'/></c:url>">&raquo;</a>
          </c:when>
          <c:otherwise><span class="nav disabled">&raquo;</span></c:otherwise>
        </c:choose>
      </div>
    </section>
  </main>
</div>
</body>
</html>
