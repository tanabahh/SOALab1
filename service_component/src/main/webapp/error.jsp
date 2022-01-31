<%--
  Created by IntelliJ IDEA.
  User: t.mozhanova
  Date: 08.10.2021
  Time: 12:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>

<c:if test="${not empty error}">
    Произошла ошибка: ${error}
</c:if>
<body>

</body>
</html>
