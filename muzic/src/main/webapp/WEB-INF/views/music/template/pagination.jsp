<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${searchCondition != null && searchCondition.allData > 0}">
<div class="pagination">

    <!-- 이전 -->
    <c:if test="${!searchCondition.firstBlock}">
        <a href="${pageURL}?page=${searchCondition.prevPage}&${searchCondition.allParams}">&lt;</a>
    </c:if>

    <!-- 페이지 번호 -->
    <c:forEach var="i" begin="${searchCondition.startPage}" end="${searchCondition.endPage}">
        <c:choose>
            <c:when test="${i == searchCondition.page}">
                <a class="on">${i}</a>
            </c:when>
            <c:otherwise>
                <a href="${pageURL}?page=${i}&${searchCondition.allParams}">
                    ${i}
                </a>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <!-- 다음 -->
    <c:if test="${!searchCondition.lastBlock}">
        <a href="${pageURL}?page=${searchCondition.nextPage}&${searchCondition.allParams}">&gt;</a>
    </c:if>

</div>
</c:if>