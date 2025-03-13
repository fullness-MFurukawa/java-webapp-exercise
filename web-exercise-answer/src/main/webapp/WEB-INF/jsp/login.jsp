<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>ログイン</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
</head>
<body class="bg-success">
	<br>
	<div class="container">
		<div class="card text-center">
			<div class="card-body">
				<h3 class="card-title">ログイン画面</h3>
				<p>デモ用<strong style="color: red;">【 ユーザー名:「demo」＆パスワード：「pass」】</strong></p>
				<c:forEach var="msg" items="${errMsgs}">
					<div class="alert alert-danger" role="alert">${msg}</div>
				</c:forEach>
				<form action="<%=request.getContextPath()%>/" method="post">
					<div class="form-group mt-3">
						<label for="username">ユーザー名:</label> <input type="text"
							class="form-control" id="username" name="username">
					</div>
					<div class="form-group mt-3">
						<label for="password">パスワード:</label> <input type="password"
							class="form-control" id="password" name="password">
					</div>
					<button type="submit" class="btn btn-primary mt-3">ログイン</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>