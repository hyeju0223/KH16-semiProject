<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 · 게임 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>
<div class="admin-wrap">

  <!-- 사이드바 -->
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

  <!-- 메인 -->
  <main class="admin-main">
    <div class="crumb">관리자 &rsaquo; 게임 &rsaquo; 상세</div>
    <div class="page-title">게임 상세</div>

    <!-- A. 게임 기본 정보 -->
    <section class="card" style="margin-top:12px">
      <div class="row" style="justify-content:space-between; align-items:center; margin-bottom:10px">
        <div class="chip">기본 정보</div>
        <div class="row gap-8">
          <a class="btn btn-ghost" href="<c:url value='/admin/game/list'/>">목록으로</a>
          <a class="btn btn-primary"
             href="<c:url value='/admin/game/edit'><c:param name='rouletteNo' value='${game.rouletteNo}'/></c:url>">정보 수정</a>
        </div>
      </div>

      <table class="table">
        <tbody>
        <tr>
          <th style="width:160px">번호</th>
          <td>${game.rouletteNo}</td>
          <th style="width:160px">게임명</th>
          <td>${game.rouletteName}</td>
        </tr>
        <tr>
          <th>일일 참여 횟수</th>
          <td>${game.rouletteDailyCount}</td>
          <th>시작일</th>
          <td>${game.rouletteDate}</td>
        </tr>
        <tr>
          <th>최대 포인트</th>
          <td><c:out value="${game.rouletteMaxPoint}"/></td>
          <th>최소 포인트</th>
          <td><c:out value="${game.rouletteMinPoint}"/></td>
        </tr>
        </tbody>
      </table>
    </section>

    <!-- B. 참여 로그 -->
    <section class="card" style="margin-top:16px">
      <div class="row" style="justify-content:space-between; align-items:center; margin-bottom:10px">
        <div class="chip">참여 로그</div>
        <form class="row" method="get" action="<c:url value='/admin/game/detail'/>" style="gap:8px">
          <input type="hidden" name="rouletteNo" value="${game.rouletteNo}">
          <select name="ltype" class="select">
            <option value="all"   ${empty param.ltype or param.ltype=='all'   ? 'selected="selected"' : ''}>전체</option>
            <option value="logNo" ${param.ltype=='logNo' ? 'selected="selected"' : ''}>로그PK</option>
            <option value="member"${param.ltype=='member' ? ' selected="selected"' : ''}>회원ID</option>
          </select>
          <input class="input" name="lkeyword" value="${fn:escapeXml(param.lkeyword)}" placeholder="검색어">
          <input type="hidden" name="lpage" value="1">
          <input type="hidden" name="lsize" value="${logPage.size}">
          <button class="btn key" type="submit">검색</button>
          <span class="count">전체 ${logPage.allData}건</span>
        </form>
      </div>

      <table class="table">
        <thead>
        <tr>
          <th style="width:120px">로그PK</th>
          <th style="width:140px">게임번호</th>
          <th>회원ID</th>
          <th style="width:140px">당첨포인트</th>
          <th style="width:220px">참여시간</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="log" items="${logList}">
          <tr>
            <td>${log.rouletteLogNo}</td>
            <td>${log.rouletteLogRoulette}</td>
            <td>${log.rouletteLogMember}</td>
            <td>${log.rouletteLogPoint}</td>
            <td>
              <c:choose>
                <c:when test="${not empty log.rouletteLogTime}">
                  <fmt:formatDate value="${log.rouletteLogTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </c:when>
                <c:otherwise>-</c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty logList}">
          <tr><td colspan="5" style="text-align:center;color:#6b7280">로그가 없습니다.</td></tr>
        </c:if>
        </tbody>
      </table>

      <!-- 로그 페이징 -->
      <c:set var="tp" value="${logPage.totalPage}" />
      <c:set var="sp" value="${logPage.strPage}" />
      <c:set var="rawEp" value="${logPage.endPage}" />
      <c:set var="ep" value="${rawEp > tp ? tp : rawEp}" />

      <div class="pager pager-center">
        <!-- << -->
        <c:choose>
          <c:when test="${sp > 1}">
            <a class="nav"
               href="<c:url value='/admin/game/detail'>
                       <c:param name='rouletteNo' value='${game.rouletteNo}'/>
                       <c:param name='lpage' value='${sp-1}'/>
                       <c:param name='lsize' value='${logPage.size}'/>
                       <c:param name='ltype' value='${empty param.ltype ? "all" : param.ltype}'/>
                       <c:param name='lkeyword' value='${param.lkeyword}'/>
                     </c:url>">&laquo;</a>
          </c:when>
          <c:otherwise><span class="nav disabled">&laquo;</span></c:otherwise>
        </c:choose>

        <!-- numbers -->
        <c:forEach var="p" begin="${sp}" end="${ep}">
          <a class="${p==logPage.page?'active':''}"
             href="<c:url value='/admin/game/detail'>
                     <c:param name='rouletteNo' value='${game.rouletteNo}'/>
                     <c:param name='lpage' value='${p}'/>
                     <c:param name='lsize' value='${logPage.size}'/>
                     <c:param name='ltype' value='${empty param.ltype ? "all" : param.ltype}'/>
                     <c:param name='lkeyword' value='${param.lkeyword}'/>
                   </c:url>">${p}</a>
        </c:forEach>

        <!-- >> -->
        <c:choose>
          <c:when test="${ep < tp}">
            <a class="nav"
               href="<c:url value='/admin/game/detail'>
                       <c:param name='rouletteNo' value='${game.rouletteNo}'/>
                       <c:param name='lpage' value='${ep+1}'/>
                       <c:param name='lsize' value='${logPage.size}'/>
                       <c:param name='ltype' value='${empty param.ltype ? "all" : param.ltype}'/>
                       <c:param name='lkeyword' value='${param.lkeyword}'/>
                     </c:url>">&raquo;</a>
          </c:when>
          <c:otherwise><span class="nav disabled">&raquo;</span></c:otherwise>
        </c:choose>
      </div>
    </section>

  </main>
</div>
</body>
</html>
