<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/template/header-1.jsp" />

<title>MBTI게시판</title>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
<link rel="stylesheet" type="text/css" href="/css/post.css">

<style>

</style>

<div class="center w-1000">
	<div class="cell center">
 		<h2>${memberDto.memberMbti} 게시판</h2>
	</div>
	
	<div class="cell center">
		<p>관리자가 늘 지켜보고 있습니다👀</p>
	</div>
	<div class="cell right">
			<c:choose>
				<c:when test="${sessionScope.loginMemberId != null}">
				<h3>
				<!-- mbti/list.jsp 파일의 글쓰기 버튼 -->
					<a href="${pageContext.request.contextPath}/post/mbti/write" class="btn btn-navy">글쓰기</a>
				</h3>
			</c:when>
			<c:otherwise>
				<h3><a href="/member/login">로그인</a>을 해야 글을 작성할 수 있습니다</h3>
			</c:otherwise>
			</c:choose>
	</div>
	<div class="right" style="color: #2d4575">
		<a href="/" class="btn btn-navy">메인으로</a>
	</div>
	<div class="cell">
		<table class="table table-border w-100">
			<thead>
				<tr>
					<th width="40%">제목</th>
					<th>작성자</th>
					<th>작성일</th>
<!-- 					<th>조회수</th> -->
					<th>좋아요</th>
				</tr>
			</thead>


			<tbody align="center">
				<c:forEach var="postVO" items="${postList}" varStatus="status">
					<tr>
						<td>
							<div>
								<c:if test="${postVO.postNotice == 'Y'}">
	        							<i class="fa-solid fa-alarm-clock" style="color: red; margin-right: 5px;"></i>
	        							<span style="font-weight: bold; color: red;">공지</span>
	        							<span style="margin-right: 10px;"></span> 
	    							</c:if>
								
								<a href="${pageContext.request.contextPath}/post/detail?postNo=${postVO.postNo}">
	    							${postVO.postTitle}
								</a>
							</div>
						</td>
<!-- 						<td> -->
<%-- 							<c:choose> --%>
<%-- 								<c:when test="${not empty postVO.memberNickname}"> --%>
<%-- 									${postVO.memberNickname} --%>
<%-- 								</c:when> --%>
<%-- 								<c:otherwise> --%>
<!-- 									(탈퇴한 사용자) -->
<%-- 								</c:otherwise> --%>
<%-- 							</c:choose> --%>
<!-- 						</td> -->

						<td>
                            <c:choose>
                                <%-- 1. postNotice가 'Y' (공지)인 경우: 무조건 "운영진"으로 표시 --%>
                                <c:when test="${postVO.postNotice == 'Y'}">
                                    <span style="font-weight: bold; color: #block;">운영진</span>
                                </c:when>
                                
                                <%-- 2. 일반 게시글이고 닉네임이 존재하는 경우 (정상 사용자) --%>
                                <c:when test="${not empty postVO.memberNickname}">
                                    ${postVO.memberNickname}
                                </c:when>
                                
                                <%-- 3. 일반 게시글이고 닉네임이 없는 경우 (탈퇴한 사용자) --%>
                                <c:otherwise>
                                    (탈퇴한 사용자)
                                </c:otherwise>
                            </c:choose>
						</td>
						
						<td>${postVO.postWriteTime}</td>
						<td>${postVO.postLike}</td>
	<%-- 				<td>${postVO.post }</td> --%>
					</tr> 
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="mbti-pagination">
		<jsp:include page="/WEB-INF/views/template/pagination.jsp"></jsp:include>
	</div>
	
	<div class="cell center mt-30 mb-50">
		<form action="${pageContext.request.contextPath}/post/mbti/list">
			<div class="flex-box" style="justify-content:center;">
				<select name="column" class="field">
					<option value="post_title" ${searchCondition.column == 'post_title' ? 'selected' : ''}>제목</option>
					<option value="post_writer" ${searchCondition.column == 'post_writer' ? 'selected' : ''}>ID</option>
				<option value="member_nickname" ${searchCondition.column == 'member_nickname' ? 'selected' : ''}>닉네임</option>
				</select>
				<input type="text" name="keyword" value="${searchCondition.keyword}" class="field ms-10">
				<button type="submit" class="btn btn-navy ms-10">검색</button>
			</div>
		</form>
	</div>
</div>

<jsp:include page="/WEB-INF/views/template/footer.jsp" />