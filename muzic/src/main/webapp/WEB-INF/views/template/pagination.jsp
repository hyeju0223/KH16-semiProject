<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${searchCondition != null && searchCondition.allData > 0}">

	<div class="pagination">
        <c:if test="${!searchCondition.firstBlock}">
            <a href="${currentURL == null ? 'list' : currentURL}?page=${searchCondition.prevPage <= 0 ? 1 : searchCondition.prevPage}&${searchCondition.params}">&lt;</a>
        </c:if>

        <c:forEach var="i" begin="${searchCondition.startPage}" end="${searchCondition.endPage}" step="1">
            <c:choose>
                <c:when test="${i == searchCondition.page}">
                    <a class="on">${i}</a>
                </c:when>
                <c:otherwise>
                    <a href="${currentURL == null ? 'list' : currentURL}?page=${i}&${searchCondition.params}">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${!searchCondition.lastBlock}">
            <a href="${currentURL == null ? 'list' : currentURL}?page=${searchCondition.nextPage}&${searchCondition.params}">&gt;</a>
        </c:if>
    </div>
</c:if>