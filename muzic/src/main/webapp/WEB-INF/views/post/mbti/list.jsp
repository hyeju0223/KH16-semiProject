<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <style>
        .table {
		    /* 1. Corrected: Added border-collapse to fix double borders */
		    border-collapse: collapse;
		    border: 1px solid #000000;
		    /* 2. Adjusted: Changed width to 100% so it fits inside the 800px container */
		    width: 100%; 
		}
		
		.container {
		    box-shadow: 2px 2px 5px #8d8b8b; /* Added standard box-shadow values for better effect */
		    width: 800px;
		    margin: 0 auto; /* Correctly centers the container */
		}
		
		/* 3. Corrected: Typo 'soild' changed to 'solid' */
		th, td {
		    text-align: center;
		    border: 1px solid #000000;
		    padding: 8px 36px;
		}
		
		.center {
		    /* 4. Removed align-content as it's for flex/grid. Keep text-align for text centering. */
		    text-align: center;
		}
		
		.table.table-striped > thead > tr {
		    background-color: #e4e4e4;
		} 
		
		.table.table-striped > tbody > tr:nth-child(2n) {
		    background-color: #eeeded;
		}
		
		.table-list > tbody > tr:hover {
		    background-color: #e7e7e7;
		}
    </style>
    
<div class="center w-1000">
	<div class="cell center">
 		<h2>${memberDto.memberMbti} 게시판</h2>
	</div>
	
	<div class="cell center">
		<p>타인에 대한 우앵앵애</p>
	</div>
	<div class="cell right">
			<c:choose>
				<c:when test="${sessionScope.loginMemberId != null}">
				<h3>
				<!-- mbti/list.jsp 파일의 글쓰기 버튼 -->
					<a href="${pageContext.request.contextPath}/post/mbti/write" class="btn btn-neutral">글쓰기</a>
				</h3>
			</c:when>
			<c:otherwise>
				<h3><a href="/member/login">로그인</a>을 해야 글을 작성할 수 있습니다</h3>
			</c:otherwise>
			</c:choose>
	</div>
	<div class="cell">
		<a href="/">메인으로</a>
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
							<c:if test="${boardListVO.boardNotice == 'Y'}">
								<span class="badge">공지</span>
							</c:if>
							
							<a href="${pageContext.request.contextPath}/post/detail?postNo=${postVO.postNo}">
    							${postVO.postTitle}
							</a>
						</div>
					</td>
						<td>${postVO.postWriter}</td>
						<td>${postVO.postWriteTime}</td>
						<td>${postVO.postLike}</td>
<%-- 					<td>${postVO.post }</td> --%>
					</tr> 
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>