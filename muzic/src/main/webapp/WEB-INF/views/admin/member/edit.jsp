<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 · 회원 수정</title>
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
    <div class="crumb">관리자 &rsaquo; 회원 &rsaquo; 수정</div>
    <div class="page-title">회원 정보 수정</div>

    <section class="card card-large" style="margin-top:12px">
      <div class="row" style="justify-content:space-between; align-items:center; margin-bottom:10px">
        <div class="chip">기본 정보</div>
        <div class="row gap-8">
          <!-- 보라 칩 버튼 -->
          <a class="btn btn-soft"
             href="<c:url value='/admin/member/detail?memberId=${memberDto.memberId}'/>">상세로 돌아가기</a>
          <a class="btn btn-ghost"
             href="<c:url value='/admin/member/list'/>">목록으로</a>
        </div>
      </div>

      <!-- 수정 폼 -->
      <form method="post" action="<c:url value='/admin/member/edit'/>">
        <input type="hidden" name="memberId" value="${memberDto.memberId}">
        <%-- (시큐리티 사용 시)
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        --%>

        <table class="table">
          <tbody>
          <tr>
            <th style="width:160px">아이디</th>
            <td colspan="3" class="muted">${memberDto.memberId}</td>
          </tr>
          <tr>
            <th>닉네임</th>
            <td>
              <input class="input input-lg w-320" name="memberNickname"
                     value="${memberDto.memberNickname}" maxlength="30" required>
            </td>
            <th>이름</th>
            <td>
              <input class="input input-lg w-240" name="memberName"
                     value="${memberDto.memberName}" maxlength="18" required>
            </td>
          </tr>
          <tr>
            <th>이메일</th>
            <td>
              <input class="input input-lg w-320" type="email" name="memberEmail"
                     value="${memberDto.memberEmail}" maxlength="60" required>
            </td>
            <th>MBTI</th>
            <td>
              <input class="input input-lg w-240" name="memberMbti"
                     value="${memberDto.memberMbti}" maxlength="4" placeholder="예: INFP">
            </td>
          </tr>
          <tr>
            <th>생년월일</th>
            <td>
              <input class="input input-lg w-240" name="memberBirth"
                     value="${memberDto.memberBirth}" maxlength="10" placeholder="YYYY-MM-DD" required>
            </td>
            <th>연락처</th>
            <td>
              <input class="input input-lg w-240" name="memberContact"
                     value="${memberDto.memberContact}" maxlength="13" placeholder="010-0000-0000" required>
            </td>
          </tr>
          <tr>
            <th>권한</th>
            <td>
              <select class="select input-lg w-240" name="memberRole" required>
                <option value="일반회원" ${memberDto.memberRole=='일반회원'?'selected':''}>일반회원</option>
                <option value="관리자"   ${memberDto.memberRole=='관리자'?'selected':''}>관리자</option>
              </select>
            </td>
            <th>포인트</th>
            <td>
              <input class="input input-lg w-240" type="number" name="memberPoint"
                     value="${memberDto.memberPoint}" min="0" step="1" required>
            </td>
          </tr>
          </tbody>
        </table>

        <!-- 버튼: 취소=빨강, 저장=파랑 -->
        <div class="row" style="justify-content:flex-end; gap:8px; margin-top:12px">
          <a class="btn btn-danger btn-lg"
             href="<c:url value='/admin/member/detail?memberId=${memberDto.memberId}'/>">취소</a>
          <button class="btn btn-primary btn-lg" type="submit">저장</button>
        </div>
      </form>
    </section>
  </main>
</div>
</body>
</html>
