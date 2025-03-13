<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>社員一覧</title>
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
				<h3 class="card-title">社員一覧</h3>
				<br>
				<c:choose>
					<c:when test="${empAllList==null||empAllList.isEmpty()}">
						<div class="alert alert-danger" role="alert">登録されている社員はいません</div>
					</c:when>
					<c:otherwise>
						<table class="table table-bordered table-striped">
							<thead class="thead-dark">
								<tr>
									<th>名前</th>
									<th>所属部門</th>
									<th>電話番号</th>
									<th>E-mailアドレス</th>
									<th>更新</th>
									<th>削除</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="emp" items="${empAllList}">
									<tr>
										<td><c:out value="${emp.empName}" /></td>
										<td><c:out value="${emp.department.deptName}" /></td>
										<td><c:out value="${emp.phone}" /></td>
										<td><c:out value="${emp.mailAddress}" /></td>
										<td>
											<form
												action="<%=request.getContextPath()%>/empupdateselectbutton"
												method="post">
												<input type="hidden" name="empId" value="${emp.empId}">
												<button type="submit" class="btn btn-warning">更新</button>
											</form>
										</td>
										<td>
											<form
												action="<%=request.getContextPath()%>/empdeleteselectbutton"
												method="post">
												<input type="hidden" name="empId" value="${emp.empId}">
												<button type="submit" class="btn btn-danger">削除</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
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