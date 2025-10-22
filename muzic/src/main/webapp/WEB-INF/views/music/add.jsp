<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">

<!-- loginMemberId라는 이름으로 세션에 저장된 값을 출력 -->
<p>현재 로그인된 ID: ${sessionScope.loginMemberId}</p>

<!-- (선택) sessionScope를 생략하고 바로 사용 가능 -->
<p>현재 로그인된 ID: ${loginMemberId}</p>

<!-- 예시: MusicController에서 세션에 저장했던 닉네임과 역할 출력 -->
<p>회원 닉네임: ${loginMemberNickName}</p>
<p>회원 역할: ${loginMemberRole}</p>

<div class="container w-600">
	<div class="cell center">
		<h1>음원 등록 페이지</h1>
	</div>
	<form action="add" method="post" enctype="multipart/form-data"
		autocomplete="off">
		<div class="cell">
			<label>제목<span class="red">*</span></label> <input type="text"
				name="musicTitle" placeholder="한글 및 영어 특수기호 가능" class="field w-100">
		</div>
		<div class="cell">
			<label>가수<span class="red">*</span></label> <input type="text"
				name="musicArtist" placeholder="한글 및 영어 특수기호 가능" class="field w-100">
		</div>
		<div class="cell">
			<label>앨범</label> <input type="text" name="musicAlbum"
				placeholder="한글 및 영어 특수기호 가능" class="field w-100">
		</div>
		<div class="cell">
			<label>장르<span class="red">*</span></label>
		</div>
		<div class="cell checkbox-group" >
			<label><input type="checkbox" name="musicGenreSet" value="신나는">신나는</label>
			<label><input type="checkbox" name="musicGenreSet" value="슬픈">슬픈</label>
			<label><input type="checkbox" name="musicGenreSet" value="감성적인">감성적인</label>
			<label><input type="checkbox" name="musicGenreSet" value="힙한">힙한</label>
			<label><input type="checkbox" name="musicGenreSet" value="잔잔한">잔잔한</label>
			<label><input type="checkbox" name="musicGenreSet" value="여유로운">여유로운</label>
			<label><input type="checkbox" name="musicGenreSet" value="분위기 있는">분위기 있는</label> 
			<label><input type="checkbox" name="musicGenreSet" value="차분한">차분한</label> 
			<label><input	type="checkbox" name="musicGenreSet" value="몽환적인">몽환적인</label> 
			<label><input	type="checkbox" name="musicGenreSet" value="중독성 있는">중독성 있는</label> 
			<label><input	type="checkbox" name="musicGenreSet" value="설레는">설레는</label> 
			<label><input	type="checkbox" name="musicGenreSet" value="강렬한">강렬한</label>
			<label><input	type="checkbox" name="musicGenreSet" value="어두운">어두운</label> 
			<label><input	type="checkbox" name="musicGenreSet" value="따뜻한">따뜻한</label> 
			<label><input	type="checkbox" name="musicGenreSet" value="청량한">청량한</label> 
			<label><input	type="checkbox" name="musicGenreSet" value="센치한">센치한</label>
		</div>
		<div class="cell">
			<label>프로필 이미지</label> <input type="file" name="coverImage"
				class="field w-100" accept="image/*">
		</div>
		<div class="cell">
			<label>음악 파일</label> <input type="file" name="musicFile"
				class="field w-100" accept="audio/*">
		</div>
		<button type="submit" class="btn btn-positive w-100">
			<i class="fa-solid fa-music"></i><span>등록하기</span>
		</button>
	</form>
</div>