<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Здравствуйте!</title>
</head>
<body>

<c:if test="${not empty error}">
    Произошла ошибка: ${error}
</c:if>

<form action = "${pageContext.request.contextPath}/vehicle" method="get">
    <input type="submit" value="Начать работу с базой данных">
</form>
</body>
</html>