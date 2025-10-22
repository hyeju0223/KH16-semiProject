<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
	.center {
		 text-align: center !important;
	}
	
	.w-1000 {
		width: 1000px !important;
	}
	
	.cell {
		margin: 10px 0;
	}
	
	.right {
		text-align: right !important;
	}
	
	.table {
		border-collapse: collapse;
    	font-size: 16px;
    	font-weight: 400 !important;
	}
	
	.table.table-border,
	.table.table-border > thead > tr > th, 
	.table.table-border > thead > tr > td,
	.table.table-border > tbody > tr > th, 
	.table.table-border > tbody > tr > td,	
	.table.table-border > tfoot > tr > th, 
	.table.table-border > tfoot > tr > td
	{
	    border: 1px solid #636e72;
	}
	
	.w-100 {
		width:100% !important;
	}
	
</style>

<div class="center w-1000">
	<div class="cell center">
 		<h2>${postDto.postMbti} 게시판</h2>
	</div>
	
	<div class="cell center">
		<p>타인에 대한 우앵앵애</p>
	</div>
	<%-- <div class="cell right">
			<c:choose>
				<c:when test="${sessionScope.loginId != null}">
					<h3><a href="write">글쓰기</a></h3>
				</c:when>
				<c:otherwise>
					<h3><a href="/member/login">로그인</a>을 해야 글을 작성할 수 있습니다</h3>
				</c:otherwise>
			</c:choose>
	</div> --%>
	<div class="cell">
	<table class="table table-border w-100">
		<thead>
			<tr>
				<th>번호</th>
				<th width="40%">제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>조회수</th>
				<th>좋아요</th>
			</tr>
		</thead>
	</div>
</div>


<%-- <tbody align="center">
				<c:forEach var="boardListVO" items="${boardList}" varStatus="status">
				<tr>
					<td>${boardListVO.boardNo}</td>
					<td class="left" style="padding-left:${boardListVO.boardDepth * 20  + 10}px">
						<c:if test="${boardListVO.boardDepth > 0}">
							<img src="/images/board/reply.png" width="16" height="16">
						</c:if>
					
						공지사항인 경우는 제목앞에 (공지) 추가
						<c:if test="${boardListVO.boardNotice == 'Y'}">
							<span class="badge">공지</span>
						</c:if>
						
						<a href="detail?boardNo=${boardListVO.boardNo}">
							${boardListVO.boardTitle}
						</a>
					</td>
					<td>${boardListVO.boardWriter == null ? '(탈퇴한사용자)' : boardListVO.memberNickname}</td>
					<td>${boardListVO.boardWriteTime}</td>
					<td>${boardListVO.boardRead}</td>
					<td>${boardListVO.boardLike}</td>

				</tr>
				</c:forEach>
			</tbody> --%>
		</table>
	</div>
</div>