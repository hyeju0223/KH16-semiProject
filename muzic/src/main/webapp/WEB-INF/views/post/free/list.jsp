<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- <style> -->
<!-- /* 	.center { */ -->
<!-- /* 		 text-align: center !important; */ -->
<!-- /* 	} */ -->
	
<!-- /* 	.w-1000 { */ -->
<!-- /* 		width: 1000px !important; */ -->
<!-- /* 	} */ -->
	
<!-- /* 	.cell { */ -->
<!-- /* 		margin: 10px 0; */ -->
<!-- /* 	} */ -->
	
<!-- /* 	.right { */ -->
<!-- /* 		text-align: right !important; */ -->
<!-- /* 	} */ -->
	
<!-- /* 	.table { */ -->
<!-- /* 		border-collapse: collapse; */ -->
<!-- /*     	font-size: 16px; */ -->
<!-- /*     	font-weight: 400 !important; */ -->
<!-- /* 	} */ -->
	
<!-- /* 	.table.table-border, */ -->
<!-- /* 	.table.table-border > thead > tr > th,  */ -->
<!-- /* 	.table.table-border > thead > tr > td, */ -->
<!-- /* 	.table.table-border > tbody > tr > th,  */ -->
<!-- /* 	.table.table-border > tbody > tr > td,	 */ -->
<!-- /* 	.table.table-border > tfoot > tr > th,  */ -->
<!-- /* 	.table.table-border > tfoot > tr > td */ -->
<!-- /* 	{ */ -->
<!-- /* 	    border: 1px solid #636e72; */ -->
<!-- /* 	} */ -->
	
<!-- /* 	.w-100 { */ -->
<!-- /* 		width:100% !important; */ -->
<!-- /* 	} */ -->
	
<!-- </style> -->
<link rel="stylesheet" type="text/css" href="./commons.css">
    <style>
        .table{
            border: 1px solid #000000;
            width: 1008px;
        }
        .center {
            align-content: center;
            text-align: center;
        }
        th, td {
            text-align: center;
            border: 1px soild #000000;
            padding: 8px 36px;
        }
        .container {
            box-shadow: #8d8b8b;
            width: 800px;
            margin: 0 auto;
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
		<h2>자유게시판</h2>
	</div>
	
	<div class="cell center">
		<p>타인에 대한 우앵앵애</p>
	</div>
	<div class="cell right">
			<c:choose>
				<c:when test="${sessionScope.loginMemberId != null}">
				<h3><a href="write" class="btn btn-neutral">글쓰기</a></h3>
			</c:when>
			<c:otherwise>
				<h3><a href="/member/login">로그인</a>을 해야 글을 작성할 수 있습니다</h3>
			</c:otherwise>
			</c:choose>
	</div>
	<div class="cell">
	<table class="table table-border w-100">
		<thead>
			<tr>
				<th width="40%">제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>좋아요</th>
<!-- 				<th>댓글</th> -->
				<th>조회수</th>
			</tr>
		</thead>
		<tbody align="center">
			<c:forEach var="postVO" item="${postList}" varStatus="status">
				<tr>
					<td>제목</td>
					<td>
						<div>
							<c:if test="${boardListVO.boardNotice == 'Y'}">
								<span class="badge">공지</span>
							</c:if>
							
							<a href="detail?postNo=${postVO.postNO}">
								${postVO.postTitle}
							</a>
						</div>
						
					</td>
					<td>${postVO.postWriteTime}</td>
					<td>${postVO.postLike}</td>
<%-- 					<td>${postVO.post }</td> --%>
					<td>조회수</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
</div>