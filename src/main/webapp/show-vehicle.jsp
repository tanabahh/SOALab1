<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Список Vehicle</title>
</head>
<body>
<form action = "${pageContext.request.contextPath}/vehicle" method="get">
    <input type="text" name="page" placeholder="Страница">
    <input type="number" name="per-page" placeholder="На страницу">
    <input type="text" name="sort-state" placeholder="Парметры сортировки в формате column&true,..">
    <input type="text" name="id" placeholder="id =">
    <input type="text" name="name" placeholder="name =">
    <input type="text" name="creation-date" placeholder="creationDate =">
    <input type="text" name="engine-power" placeholder="enginePower =">
    <input type="text" name="type" placeholder="type =">
    <input type="text" name="fuel-type" placeholder="fuelType =">
    <input type="text" name="x" placeholder="coordinates.x =">
    <input type="text" name="y" placeholder="coordinates.y =">
    <input type="submit" value="Поиск">
</form>

<c:if test="${not empty error}">
Произошла ошибка: ${error}
</c:if>


<table border="2">
    <tr>
        <td>id</td>
        <td>name</td>
        <td>coordinates x</td>
        <td>coordinates y</td>
        <td>creatingDate</td>
        <td>enginePower</td>
        <td>type</td>
        <td>fuelType</td>
    </tr>
    <c:forEach items="${vehicle}" var = "vehicle">
        <tr>
            <td>${vehicle.getId()}</td>
            <td>${vehicle.getName()}</td>
            <td>${vehicle.getX()}</td>
            <td>${vehicle.getY()}</td>
            <td>${vehicle.getCreationDate()}</td>
            <td>${vehicle.getEnginePower()}</td>
            <td>${vehicle.getType()}</td>
            <td>${vehicle.getFuelType()}</td>
            <td>
                <form action = "update-vehicle.jsp" method="post">
                    <input type="hidden" name="id" value="${vehicle.getId()}">
                    <input type="submit" value="Изменить" style="float:left">
                </form>
                <form action="delete-vehicle.jsp" method="post">
                    <input type="hidden" name="id" value="${vehicle.getId()}">
                    <input type="submit" value="Удалить" style="float:left">
                </form></td>
        </tr>
    </c:forEach>
</table>

<form action = "add-vehicle.jsp">
    <input type="submit" value="Добавить нового пользователя">
</form>

Дополнительные возможности:

Удалить все vehicle, где enginePower =
<form action = "${pageContext.request.contextPath}/extra/delete" method="post">
    <input type="text" name="engine-power" placeholder="engine-power =">
    <input type="hidden" name="_method" value="delete">
    <input type="submit" value="Удалить">
</form>

Вернуть vehicle, где creatingDate максимальное
<form action = "${pageContext.request.contextPath}/extra/max-creation-date" method="get">
    <input type="submit" value="Получить">
</form>

Сгруппировать по creatingDate и вернуть количество элементов
<form action = "${pageContext.request.contextPath}/extra/group-by-creation-date" method="get">
    <input type="submit" value="Посмотреть">
</form>

<c:if test="${not empty groups}">
    <table border="2">
        <tr>
            <td>Date</td>
            <td>Count</td>
        </tr>
        <c:forEach items="${groups}" var = "group">
            <tr>
                <td>${group.getCreationDate()}</td>
                <td>${group.getCount()}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>