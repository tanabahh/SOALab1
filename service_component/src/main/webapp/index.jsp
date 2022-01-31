<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Здравствуйте!</title>
</head>
<body>
<form action = "${pageContext.request.contextPath}/show-vehicle.jsp">
    <input type="submit" value="Начать работу с базой данных">
</form>

<form action = "${pageContext.request.contextPath}/shop.jsp">
    <input type="submit" value="Начать работу со сторонним сервисом">
</form>
</body>
</html>