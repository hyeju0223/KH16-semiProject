<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 회원 탈퇴 전 유의사항 안내 (멀티페이지 1)-->

   	<h1>회원탈퇴 유의사항</h1>
   	<p>회원탈퇴를 신청하기 전에 안내사항을 꼭 확인해주세요.</p>
   
   <h4>사용하고 계신 아이디(${memberDto.memberId})는 탈퇴할 경우 재사용 및 복구가 불가능합니다</h4>
   
   <h4>탈퇴 후 회원정보 및 개인형 서비스 이용기록은 모두 삭제됩니다. </h4>
	<pre>
	회원정보 및 메일, 블로그, 주소록 등 개인형 서비스 이용기록은 모두 삭제되며, 삭제된 데이터는 복구되지 않습니다.
삭제되는 내용을 확인하시고 필요한 데이터는 미리 백업을 해주세요.
	</pre>
   <h4>탈퇴 후에도 게시판 및 앨범형 서비스에 등록한 게시물 및 음원은 그대로 남아있습니다 </h4>
	
	<pre>뉴스, 카페, 지식iN 등에 올린 게시글 및 댓글은 탈퇴 시 자동 삭제되지 않고 그대로 남아 있습니다.
삭제를 원하는 게시글이 있다면 반드시 탈퇴 전 비공개 처리하거나 삭제하시기 바랍니다.
탈퇴 후에는 회원정보가 삭제되어 본인 여부를 확인할 수 있는 방법이 없어, 게시글을 임의로 삭제해드릴 수 없습니다.</pre>

<form action="/mypage/withDraw" method="post">
<input type="checkbox" name="agree" > 안내 사항을 모두 확인하였으며, 이에 동의합니다.
<button type="button" id="#">확인</button>


<!-- 비밀번호 확인 후 탈퇴하기 (멀티페이지 2) -->
	<hr>
	<h1>회원 비밀번호 확인</h1>

	비밀번호를 한번 더 입력해주세요<br>
	비밀번호를 입력하시면 회원탈퇴가 완료됩니다<br>
	비밀번호를 모르시면 정보수정 페이지에서 새로 설정 후 진행하시면 됩니다<br>
	<a href="password">수정하러 가기</a><br>
	
	<h3>회원 아이디 : ${memberDto.memberId}</h3>
	<input type="password" name="memberPw">
	<button type="submit">탈퇴하기</button>
	</form>
