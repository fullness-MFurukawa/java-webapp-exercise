<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>部門削除(確認)</title>
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
				<h3 class="card-title">部門削除(確認)</h3>
				<c:forEach var="msg" items="${errMsgs}">
					<div class="alert alert-danger" role="alert">${msg}</div>
				</c:forEach>
				<h4 class="card-text">以下の内容を削除します。</h4>
				<br>
				<form method="post">
					<table class="table table-bordered table-striped">
						<tbody>
							<tr>
								<td class="bg-dark text-light" width="60">名前</td>
								<td width="180">${delDeptCheckViewData.deptName}</td>
							</tr>
						</tbody>
					</table>
					<br> 
					<input type="hidden" name="deptId"
						value="${delDeptCheckViewData.deptId}">
						<input type="hidden" name="name"
						value="${delDeptCheckViewData.deptName}">
					<div style="display: inline-flex;">
						<button type="submit"
							formaction="<%=request.getContextPath()%>/deptdeletecheck"
							class="btn btn-primary btn-lg m-3" name="regist">削除</button>
					</div>
				</form>
				<br>
				<ul class="nav justify-content-center">
					<li><a href="<%=request.getContextPath()%>/menu"
						class="btn btn-info btn-lg">メニュー</a></li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>