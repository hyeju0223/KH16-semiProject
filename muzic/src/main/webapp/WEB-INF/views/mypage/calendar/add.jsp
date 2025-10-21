<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <h1>일정 등록하기</h1>
    
        <form action="add" method="post">
    	<input type="date" name="calendarDay"><br>
    	<input type="hidden" name="calendarMember" value="${param.memberId}">
    	제목<input type="text" name="calendarScheduleTitle"><br>
    	내용<input type="text" name="calendarScheduleContent"> <br>
    	<button type="submit">등록</button>
    </form>