<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить нового пользователя</title>
</head>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
    $('#button-add').click(function () {
      console.log("hello")
      var name = document.getElementById("name").value;
      var enginePower =document.getElementById("engine-power").value;
      var type =document.getElementById("type").value;
      var fuelType =document.getElementById("fuel-type").value;
      var x =document.getElementById("x").value;
      var y =document.getElementById("y").value;

      $.ajax({
        type: 'POST',
        url: 'https://localhost:8444/SOALab1-1.0-SNAPSHOT/api/vehicle?'+ "name=" + name +
            "&engine-power=" + enginePower + "&type=" + type +
            "&fuel-type=" + fuelType + "&x=" + x + "&y=" + y,
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
      //
      // $.post('http://localhost:8080/SOALab1-1.0-SNAPSHOT/api/vehicle?'+ "name=" + name +
      //       "&engine-power=" + enginePower + "&type=" + type +
      //       "&fuel-type=" + fuelType + "&x=" + x + "&y=" + y)
      // .done(function(msg){
      //   alert("Success")
      // })
      // .fail(function(xhr, status, error) {
      //   console.log(XMLHttpRequest.responseText)
      //   document.getElementById('error').innerHTML = XMLHttpRequest.responseText
      // });

      // $.ajax({
      //   type: 'POST',
      //   url: 'http://localhost:8080/SOALab1-1.0-SNAPSHOT/api/vehicle',
      //   dataType: 'json',
      //   data: {
      //     "name": name,
      //     "engine-power": enginePower,
      //     "type": type,
      //     "fuel-type": fuelType,
      //     "x": x,
      //     "y": y
      //   },
      //   headers: {
      //     Accept: "application/json; charset=utf-8",
      //     "Content-Type": "application/json; charset=utf-8"
      //   },
      //   success: function (result) {
      //     console.log(result);
      //   },
      //   error: function (XMLHttpRequest) {
      //     console.log(XMLHttpRequest.responseText)
      //     document.getElementById('error').innerHTML = XMLHttpRequest.responseText
      //   }
      // })
    })
  })
</script>


<body>
<input type="text" id="name" placeholder="Имя">
<input type="text" id="x" placeholder="Координата x">
<input type="text" id="y" placeholder="Координата y">
<input type="text" id="engine-power" placeholder="Мощность">
<input type="text" id="type" placeholder="Тип">
<input type="text" id="fuel-type" placeholder="Тип топлива">

<input type="button" value="Добавить" id="button-add">
<br>
<div id="error"></div>

<form action = "show-vehicle.jsp">
    <input type="submit" value="Вернуться">
</form>
</body>
</html>