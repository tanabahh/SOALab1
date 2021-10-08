<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить нового пользователя</title>
</head>
<body>
<form action = "${pageContext.request.contextPath}/vehicle" method="post">
    <input type="text" name="name" placeholder="Имя">
    <input type="text" name="x" placeholder="Координата x">
    <input type="text" name="y" placeholder="Координата y">
    <input type="text" name="enginePower" placeholder="Мощность">
    <input type="text" name="type" placeholder="Тип">
    <input type="text" name="fuelType" placeholder="Тип топлива">
    <input type="submit" value="Сохранить">
</form>
</body>
</html>