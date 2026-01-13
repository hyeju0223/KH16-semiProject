<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <h1>비밀번호 변경</h1>
    안전한 비밀번호로 내정보를 보호하세요<br><br>
    
    다른 사이트에서 사용한 적 없거나 이전에 사용한 적 없는 비밀번호가 안전합니다 <br>    
    
    <form action="password" method="post">
    현재 비밀번호<input type="password" name="loginPw"><br><br>
    새 비밀번호<input type="password" name="changePw"><br><br>
    새 비밀번호 확인<input type="password" name="changePw2"><br><br>
    <button type="submit">확인</button><br><br>
    <a href="/mypage/profile">취소</a>
    </form>