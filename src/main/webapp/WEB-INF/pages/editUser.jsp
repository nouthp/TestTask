<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
<head>
<title>Данные пользователя</title>
</head>
<body>

	<div align="center">
		<form:form action="save" method="post" commandName="user">
			<table border="0">
				<tr>
					<td>Login:</td>
					<td><form:input path="login" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><form:password path="password" /></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><form:input path="name" /></td>
				</tr>
				<tr>
					<td>Phone:</td>
					<td><form:input path="phone" /></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="Save" />
					</td>
				</tr>
			</table>
			<form:input path="id" type="hidden"/>
			<form:input path="parentId" type="hidden"/>
		</form:form>
	</div>

</body>