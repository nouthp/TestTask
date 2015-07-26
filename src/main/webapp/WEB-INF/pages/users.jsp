<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
   <title>Пользователи</title>
</head>
<body>
<h3>Пользователи</h3>
 
 <table>
 <thead>
 <tr>
     <td>Имя</td>
     <td>Телефон</td>
   </tr>
 </thead>
   <c:forEach items="${users}" var="user">
   <tr>
     <td><a href="edit?id=${user.id}">${user.name}</a></td>
     <td><a href="edit?id=${user.id}">${user.phone}</a></td>
   </tr>
   </c:forEach>
 </table>
 <form action="create" method="post">
    <input type="submit" value="Добавить пользователя" />
</form>
</body>
</html>
