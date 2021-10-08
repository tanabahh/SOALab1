<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Изменить данные пользователя</title>
</head>
<body>

Редактировать vehicle

<form action="${pageContext.request.contextPath}/vehicle/${param.id}" method="post">
    <input type="hidden" name="_method" value="PUT"/>
    <input type="hidden" name = "id" value="${param.id}">
    <input required type="text" name="name" placeholder="Имя">
    <input required type="text" name="x" placeholder="Координата x">
    <input required type="text" name="y" placeholder="Координата y">
    <input required type="text" name="enginePower" placeholder="Мощность">
    <input required type="text" name="type" placeholder="Тип">
    <input required type="text" name="fuelType" placeholder="Тип топлива">
    <input type="submit" value="Обновить">
</form>

</body>
</html>