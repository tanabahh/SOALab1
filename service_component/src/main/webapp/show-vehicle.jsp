<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.wink.json4j.internal.Parser"%>
<html>
<head>
    <title>Список Vehicle</title>
</head>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
    $('#button-find').click(function () {
      var page =document.getElementById("page").value;
      var perPage =document.getElementById("per-page").value;
      var sortState =document.getElementById("sort-state").value;
      var id =document.getElementById("id").value;
      var name =document.getElementById("name").value;
      var creationDate =document.getElementById("creation-date").value;
      var enginePower =document.getElementById("engine-power").value;
      var type =document.getElementById("type").value;
      var fuelType =document.getElementById("fuel-type").value;
      var x =document.getElementById("x").value;
      var y =document.getElementById("y").value;
      $.ajax({
        type: 'GET',
        url: 'https://localhost:8444/service_component-1.0-SNAPSHOT/api/vehicle',
        data: {
          "page": page,
          "per-page": perPage,
          "sort-state": sortState,
          "id": id,
          "name": name,
          "creation-date": creationDate,
          "engine-power": enginePower,
          "type": type,
          "fuel-type": fuelType,
          "x": x,
          "y": y
        },
        headers: {
          Accept: "application/json; charset=utf-8",
          "Content-Type": "application/json; charset=utf-8"
        },
        success: function (result) {
          console.log(result);
          var listProduct = result;
          var s = '';
          console.log(listProduct)
          for (var i = 0; i < listProduct.length; i++) {
            s += 'Id: ' + listProduct[i].id + '<br>Name: ' + listProduct[i].name +
                '<br>FuelType : ' + listProduct[i].fuelType +
                '<br>EnginePower: ' + listProduct[i].enginePower +
                '<br>CreationDate: ' + listProduct[i].creationDate.year + '-' + listProduct[i].creationDate.month + '-' + listProduct[i].creationDate.day +
                '<br>Type: ' + listProduct[i].type +
                '<br>X: ' + listProduct[i].x +
                '<br>Y: ' + listProduct[i].y +
                '<br>==========================<br>'
          }
          document.getElementById('result').innerHTML = s;
        },
        error: function (XMLHttpRequest) {
          console.log(XMLHttpRequest.responseText)
          document.getElementById('error').innerHTML = XMLHttpRequest.responseText
        }
      })
    })


    $('#button-delete').click(function () {
      var enginePower =document.getElementById("engine-power-for-delete").value;
      $.ajax({
        type: 'DELETE',
        url: 'https://localhost:8444/service_component-1.0-SNAPSHOT/api/extra/delete?engine-power=' + enginePower,
        headers: {
          Accept: "application/json; charset=utf-8",
          "Content-Type": "application/json; charset=utf-8"
        },
        success: function () {
          alert("success")
        },
        error: function (XMLHttpRequest) {
          console.log(XMLHttpRequest.responseText)
          document.getElementById('error-delete').innerHTML = XMLHttpRequest.responseText
        }
      })
    })

    $('#button-creation').click(function () {
      $.ajax({
        type: 'GET',
        url: 'https://localhost:8444/service_component-1.0-SNAPSHOT/api/extra/max-creation-date',
        headers: {
          Accept: "application/json; charset=utf-8",
          "Content-Type": "application/json; charset=utf-8"
        },
        success: function (result) {
          console.log(result);
          var listProduct = result;
          var s = '';
          console.log(listProduct)
          for (var i = 0; i < listProduct.length; i++) {
            s += 'Id: ' + listProduct[i].id + '<br>Name: ' + listProduct[i].name +
                '<br>FuelType : ' + listProduct[i].fuelType +
                '<br>EnginePower: ' + listProduct[i].enginePower +
                '<br>CreationDate: ' + listProduct[i].creationDate.year + '-' + listProduct[i].creationDate.month + '-' + listProduct[i].creationDate.day +
                '<br>Type: ' + listProduct[i].type +
                '<br>X: ' + listProduct[i].x +
                '<br>Y: ' + listProduct[i].y +
                '<br>==========================<br>'
          }
          document.getElementById('result-creation').innerHTML = s;
        },
        error: function (XMLHttpRequest) {
          console.log(XMLHttpRequest.responseText)
          document.getElementById('error-creation').innerHTML = XMLHttpRequest.responseText
        }
      })
    })


    $('#button-show').click(function () {
      $.ajax({
        type: 'GET',
        url: 'https://localhost:8444/service_component-1.0-SNAPSHOT/api/extra/group-by-creation-date',
        headers: {
          Accept: "application/json; charset=utf-8",
          "Content-Type": "application/json; charset=utf-8"
        },
        success: function (result) {
          console.log(result);
          var group = result;
          var s = '';
          for (var i = 0; i < group.length; i++) {
            s += 'Date: ' + group[i].creationDate + '<br>Count: ' + group[i].count +
                '<br>==========================<br>'
          }
          document.getElementById('result-show').innerHTML = s;
        },
        error: function (XMLHttpRequest) {
          console.log(XMLHttpRequest.responseText)
          document.getElementById('error-show').innerHTML = XMLHttpRequest.responseText
        }
      })
    })
  })
</script>
<body>

<input type="text" id="page" placeholder="Страница">
<input type="number" id="per-page" placeholder="На страницу">
<input type="text" id="sort-state" placeholder="Парметры сортировки в формате column&true,..">
<input type="text" id="id" placeholder="id =">
<input type="text" id="name" placeholder="name =">
<input type="text" id="creation-date" placeholder="creationDate =">
<input type="text" id="engine-power" placeholder="enginePower =">
<input type="text" id="type" placeholder="type =">
<input type="text" id="fuel-type" placeholder="fuelType =">
<input type="text" id="x" placeholder="coordinates.x =">
<input type="text" id="y" placeholder="coordinates.y =">
<input type="button" value="Поиск" id="button-find">
<br>
<div id="result"></div>
<div id="error"></div>

<form action = "update-vehicle.jsp" method="post">
    <input type="text" name="id" placeholder="id =">
    <input type="submit" value="Изменить" style="float:left">
</form>
<form action="delete-vehicle.jsp" method="post">
    <input type="text" name="id" placeholder="id =">
    <input type="submit" value="Удалить" style="float:left">
</form>

<form action = "add-vehicle.jsp">
    <input type="submit" value="Добавить нового пользователя">
</form>


Дополнительные возможности:
 <br>
Удалить все vehicle, где enginePower =
<input type="text" id="engine-power-for-delete" placeholder="engine-power =">
<input type="button" value="Удалить" id="button-delete">
<br>
<div id="result-delete"></div>
<div id="error-delete"></div>
<br>
Вернуть vehicle, где creatingDate максимальное
<input type="button" value="Получить" id="button-creation">
<br>
<div id="result-creation"></div>
<div id="error-creation"></div>
<br>
Сгруппировать по creatingDate и вернуть количество элементов
<input type="button" value="Посмотреть" id="button-show">
<br>
<div id="result-show"></div>
<div id="error-show"></div>
</body>
</html>