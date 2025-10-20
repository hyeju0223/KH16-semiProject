<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h1>장바구니</h1>
<c:forEach var="cart" items="${cartList}">
	<tr>
		<td>${cart.cartGoods}</td>
		<td>${cart.cartQuantity}</td>
		<td>${cart.cartTotal}</td>
	</tr>
</c:forEach>