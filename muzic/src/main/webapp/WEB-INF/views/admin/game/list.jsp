<%--  <%@ page contentType="text/html; charset=UTF-8" %>
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
    <div class="crumb">관리자 &rsaquo; 게임 &rsaquo; 목록</div>
    <div class="page-title">게임 목록</div>

    <section class="card" style="margin-top:12px">
      <!-- 검색/필터: 이름만 -->
      <form class="filters" method="get" action="<c:url value='/admin/game/list'/>">
        <input class="input w-320" type="text" name="keyword"
               value="${fn:escapeXml(pageVO.keyword)}" placeholder="게임 이름으로 검색">
        <input type="hidden" name="page" value="1">
        <input type="hidden" name="size" value="${pageVO.size}">
        <button class="btn key" type="submit">검색</button>
        <span class="count">전체 ${pageVO.allData}건</span>
      </form>

      <!-- 목록 테이블 -->
      <table class="table">
        <thead>
        <tr>
          <th style="width:90px">번호</th>
          <th>게임 이름</th>
          <th style="width:140px">일일 횟수</th>
          <th style="width:140px">최대 포인트</th>
          <th style="width:140px">최소 포인트</th>
          <th style="width:160px">시작일</th>
          <th style="width:120px">상세보기</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="g" items="${list}">
          <tr>
            <td>${g.rouletteNo}</td>
            <td>${g.rouletteName}</td>
            <td>${g.rouletteDailyCount}</td>
            <td>${g.rouletteMaxPoint}</td>
            <td>${g.rouletteMinPoint}</td>
            <td>${g.rouletteDate}</td>
            <td>
              <a class="btn"
                 href="<c:url value='/admin/game/detail?rouletteNo=${g.rouletteNo}'/>">상세</a>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty list}">
          <tr><td colspan="7" style="text-align:center;color:#6b7280">데이터가 없습니다.</td></tr>
        </c:if>
        </tbody>
      </table>

      <!-- 페이징: << < 1 2 3 > >> (중앙정렬) -->
      <c:set var="tp" value="${pageVO.totalPage}" />
      <c:set var="sp" value="${pageVO.strPage}" />
      <c:set var="rawEp" value="${pageVO.endPage}" />
      <c:set var="ep" value="${rawEp > tp ? tp : rawEp}" />
      <c:set var="cur" value="${pageVO.page}" />

      <div class="pager pager-center" style="margin-top:14px">
        <!-- << 첫 블록 이전 -->
        <c:choose>
          <c:when test="${sp > 1}">
            <a class="nav" href="<c:url value='/admin/game/list?page=${sp-1}&size=${pageVO.size}&keyword=${fn:escapeXml(pageVO.keyword)}'/>">&laquo;</a>
          </c:when>
          <c:otherwise><span class="disabled nav">&laquo;</span></c:otherwise>
        </c:choose>

        <!-- < 이전 페이지 -->
        <c:choose>
          <c:when test="${cur > 1}">
            <a class="nav" href="<c:url value='/admin/game/list?page=${cur-1}&size=${pageVO.size}&keyword=${fn:escapeXml(pageVO.keyword)}'/>">&lsaquo;</a>
          </c:when>
          <c:otherwise><span class="disabled nav">&lsaquo;</span></c:otherwise>
        </c:choose>

        <!-- 숫자 -->
        <c:forEach var="p" begin="${sp}" end="${ep}">
          <a class="${p==cur?'active':''}"
             href="<c:url value='/admin/game/list?page=${p}&size=${pageVO.size}&keyword=${fn:escapeXml(pageVO.keyword)}'/>">${p}</a>
        </c:forEach>

        <!-- > 다음 페이지 -->
        <c:choose>
          <c:when test="${cur < tp}">
            <a class="nav" href="<c:url value='/admin/game/list?page=${cur+1}&size=${pageVO.size}&keyword=${fn:escapeXml(pageVO.keyword)}'/>">&rsaquo;</a>
          </c:when>
          <c:otherwise><span class="disabled nav">&rsaquo;</span></c:otherwise>
        </c:choose>

        <!-- >> 다음 블록 -->
        <c:choose>
          <c:when test="${ep < tp}">
            <a class="nav" href="<c:url value='/admin/game/list?page=${ep+1}&size=${pageVO.size}&keyword=${fn:escapeXml(pageVO.keyword)}'/>">&raquo;</a>
          </c:when>
          <c:otherwise><span class="disabled nav">&raquo;</span></c:otherwise>
        </c:choose>
      </div>
    </section>
  </main>
</div>
</body>
</html>
--%>
