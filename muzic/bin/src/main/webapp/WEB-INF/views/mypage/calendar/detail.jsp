<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
 <h1>일정 상세 보기 페이지</h1>
 
 <h1>${calendarDto.calendarScheduleTitle}</h1>
 <h2>${calendarDto.calendarDay}</h2>
 
 <h3><fmt:formatDate value="${calendarDto.calendarWtime}" pattern="yyyy년 MM dd일 E HH:MM"/></h3>
 <c:if test="${calendarDto.calendarEtime != null}"><h3><fmt:formatDate value="${calendarDto.calendarEtime}" pattern="yyyy년 MM dd일 E HH:MM"/>(수정됨)</h3></c:if>

 <textarea rows="20" cols="70" readonly="readonly">${calendarDto.calendarScheduleContent}</textarea><br>
 
 <a href="edit?calendarNo=${calendarDto.calendarNo}">수정</a><br>
 <a href="delete?calendarNo=${calendarDto.calendarNo}">삭제</a>