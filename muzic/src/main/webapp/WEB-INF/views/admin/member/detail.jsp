<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 · 회원 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>
<div class="admin-wrap">

  <!-- 사이드바 -->
  <aside class="admin-sb">
    <div class="logo">MUZIC Admin</div>
    <c:set var="uri" value="${pageContext.request.requestURI}"/>
    <nav class="nav">
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
    <div class="crumb">관리자 &rsaquo; 회원 &rsaquo; 상세</div>
    <div class="page-title">회원 상세</div>

    <!-- [A] 기본 정보 -->
    <section class="card" style="margin-top:12px">
      <div class="row" style="justify-content:space-between; align-items:center; margin-bottom:10px">
        <div class="chip">기본 정보</div>
        <div class="row gap-8">
          <a class="btn btn-ghost" href="<c:url value='/admin/member/list'/>">목록으로</a>
          <a class="btn btn-primary"
             href="<c:url value='/admin/member/edit'><c:param name='memberId' value='${memberDto.memberId}'/></c:url>">정보 수정</a>
        </div>
      </div>

      <table class="table">
        <tbody>
        <tr>
          <th style="width:160px">아이디</th>
          <td>${memberDto.memberId}</td>
          <th style="width:160px">닉네임</th>
          <td>${memberDto.memberNickname}</td>
        </tr>
        <tr>
          <th>이름</th>
          <td>${memberDto.memberName}</td>
          <th>이메일</th>
          <td>${memberDto.memberEmail}</td>
        </tr>
        <tr>
          <th>MBTI</th>
          <td>${memberDto.memberMbti}</td>
          <th>생년월일</th>
          <td>${memberDto.memberBirth}</td>
        </tr>
        <tr>
          <th>연락처</th>
          <td>${memberDto.memberContact}</td>
          <th>권한</th>
          <td>${memberDto.memberRole}</td>
        </tr>
        <tr>
          <th>포인트</th>
          <td>${memberDto.memberPoint}</td>
          <th>가입일</th>
          <td>
            <c:choose>
              <c:when test="${not empty memberDto.memberJoin}">
                <fmt:formatDate value="${memberDto.memberJoin}" pattern="yyyy-MM-dd HH:mm:ss"/>
              </c:when>
              <c:otherwise>-</c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr>
          <th>최근수정</th>
          <td colspan="3">
            <c:choose>
              <c:when test="${not empty memberDto.memberEtime}">
                <fmt:formatDate value="${memberDto.memberEtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
              </c:when>
              <c:otherwise>-</c:otherwise>
            </c:choose>
          </td>
        </tr>
        </tbody>
      </table>
    </section>

    <!-- [B] 블랙리스트 상태/조치 -->
    <section class="card" style="margin-top:16px">
      <div class="row" style="justify-content:space-between; align-items:center; margin-bottom:10px">
        <div class="row gap-12" style="align-items:center">
          <span class="chip">블랙리스트</span>
          <span class="badge ${blacklistYn=='Y'?'y':'n'}">${blacklistYn}</span>
        </div>

        <div class="row gap-8">
          <c:choose>
            <c:when test="${blacklistYn == 'Y'}">
              <form method="post"
                    action="<c:url value='/admin/member/blacklist/release'/>"
                    onsubmit="return confirm('블랙리스트를 해제할까요?');">
                <input type="hidden" name="memberId" value="${memberDto.memberId}">
                <button class="btn btn-primary" type="submit">블랙리스트 해제</button>
              </form>
            </c:when>
            <c:otherwise>
              <form method="post" action="<c:url value='/admin/member/blacklist/add'/>"
                    class="row" style="gap:8px">
                <input type="hidden" name="memberId" value="${memberDto.memberId}">
                <input class="input w-320" name="reason" placeholder="사유 입력(예: 결제 사기 의심)" required>
                <button class="btn btn-danger" type="submit">블랙리스트 등록</button>
              </form>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <c:if test="${not empty activeBlacklist}">
        <table class="table">
          <thead>
          <tr>
            <th style="width:80px">번호</th>
            <th>사유</th>
            <th style="width:140px">상태</th>
            <th style="width:200px">등록일</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="b" items="${activeBlacklist}" varStatus="st">
            <tr>
              <td>${st.index + 1}</td>
              <td>${b.blacklistReason}</td>
              <td><span class="badge y">${b.blacklistStatus}</span></td>
              <td>
                <c:choose>
                  <c:when test="${not empty b.blacklistRegistrationTime}">
                    <fmt:formatDate value="${b.blacklistRegistrationTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:if>
      <c:if test="${empty activeBlacklist}">
        <div class="muted">활성 블랙리스트 내역이 없습니다.</div>
      </c:if>
    </section>

    <!-- [C] 회원 하드 삭제 -->
    <section class="card" style="margin-top:16px">
      <div class="row" style="justify-content:flex-end; align-items:center">
        <form method="post"
              action="<c:url value='/admin/member/drop'/>"
              onsubmit="return confirm('정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.');">
          <input type="hidden" name="memberId" value="${memberDto.memberId}">
          <%-- CSRF 사용 시 주석 해제
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          --%>
          <button class="btn btn-danger" type="submit">회원 하드 삭제</button>
        </form>
      </div>
    </section>

  </main>
</div>
</body>
</html>
