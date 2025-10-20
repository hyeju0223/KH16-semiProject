<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">

<h1>음원 등록 페이지</h1>
<form action="add" method="post" enctype="multipart/form-data"
	autocomplete="off">
	<div class="container w-600">
		<div class="cell">
			<label>제목<span class="red">*</span></label> <input type="text"
				name="musicTitle" placeholder="한글 및 영어 특수기호 가능" class="field w-100">
		</div>
		<div class="cell">
			<label>가수<span class="red">*</span></label> <input type="text"
				name="musicTitle" placeholder="한글 및 영어 특수기호 가능" class="field w-100">
		</div>
		<div class="cell">
			<label>앨범</label> <input type="text" name="musicTitle"
				placeholder="한글 및 영어 특수기호 가능" class="field w-100">
		</div>
		<div class="cell">
			<label>장르<span class="red">*</span></label>
			<select name="musicGenre" class="field w-100">
				<option value ="">선택하세요</option>
				<option>신나는</option>
				<option>슬픈</option>
				<option>감성적인</option>
				<option>힙한</option>
				<option>잔잔한</option>
				<option>여유로운</option>
				<option>분위기 있는</option>
				<option>차분한</option>
				<option>몽환적인</option>
				<option>중독성 있는</option>
				<option>설레는</option>
				<option>강렬한</option>
				<option>어두운</option>
				<option>따뜻한</option>
				<option>청량한</option>
				<option>센치한</option>
			</select>
		</div>
		<div class="cell">
			<label>음원 제목<span class="red">*</span></label> <input type="text"
				name="musicTitle" placeholder="한글 및 영어 특수기호 가능" class="field w-100">
		</div>
	</div>
</form>