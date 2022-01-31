<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Изменить данные пользователя</title>
</head>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
    $('#button-update').click(function () {
      var id =document.getElementById("id").value;
      var name =document.getElementById("name").value;
      var enginePower =document.getElementById("engine-power").value;
      var type =document.getElementById("type").value;
      var fuelType =document.getElementById("fuel-type").value;
      var x =document.getElementById("x").value;
      var y =document.getElementById("y").value;
      $.ajax({
        type: 'PUT',
        url: 'https://localhost:8444/service_component-1.0-SNAPSHOT/api/vehicle/'+id +
            "?name=" +  name + "&engine-power=" + enginePower +
            "&type=" + type + "&fuel-type=" + fuelType +
            "&x=" + x + "&y=" + y,
        headers: {
          Accept: "application/json; charset=utf-8",
          "Content-Type": "application/json; charset=utf-8"
        },
        success: function (result) {
          alert("Success")
        },
        error: function (XMLHttpRequest) {
          console.log(XMLHttpRequest.responseText)
          document.getElementById('error').innerHTML = XMLHttpRequest.responseText
        }
      })
    })
  })
</script>
<body>

Редактировать vehicle

<input type="hidden" id= "id" value="${param.id}">
<input type="text" id="name" placeholder="Имя">
<input type="text" id="x" placeholder="Координата x">
<input type="text" id="y" placeholder="Координата y">
<input type="text" id="engine-power" placeholder="Мощность">
<input type="text" id="type" placeholder="Тип">
<input type="text" id="fuel-type" placeholder="Тип топлива">
<input type="button" value="Обновить" id="button-update">
<br>
<div id="error"></div>
<form action = "show-vehicle.jsp">
    <input type="submit" value="Вернуться">
</form>
</body>
</html>