<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Магазин</title>
</head>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
    $('#button-find').click(function () {
      var from =document.getElementById("from").value;
      var to =document.getElementById("to").value;
      $.ajax({
        //crossDomain: true,
        type: 'GET',
        url: 'https://localhost:8443/SOALab2-0.0.1-SNAPSHOT/shop/search/by-number-of-wheels/' + from + '/' + to,
        headers: {
          'Access-Control-Allow-Origin' : 'https://localhost:8443',
          Accept: "application/json; charset=utf-8",
          "Content-Type": "application/json; charset=utf-8",
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
          document.getElementById('result-find').innerHTML = s;
        },
        error: function (XMLHttpRequest) {
          console.log(XMLHttpRequest.responseText)
          document.getElementById('error-find').innerHTML = XMLHttpRequest.responseText
        }
      })
    })

    $('#button-update').click(function () {
      var id =document.getElementById("id").value;
      var number =document.getElementById("number").value;
      $.ajax({
        type: 'PUT',
        url: 'https://localhost:8443/SOALab2-0.0.1-SNAPSHOT/shop/fix-distance/'+ id + "?engine-power="+number,
        headers: {
          Accept: "application/json; charset=utf-8",
          "Content-Type": "application/json; charset=utf-8"
        },
        success: function (result) {
          alert("Success")
        },
        error: function (XMLHttpRequest) {
          console.log(XMLHttpRequest.responseText)
          document.getElementById('error-creation').innerHTML = XMLHttpRequest.responseText
        }
      })
    })
  })
</script>


<body>
Возможности:
<br>
Найти транспортные средства с числом колес от и до:
<input type="text" id="from" placeholder="From">
<input type="text" id="to" placeholder="To">
<input type="button" value="Получить" id="button-find">
<br>
<div id="result-find"></div>
<div id="error-find"></div>
<br>
Изменить мощность двигателя:
<br>
<input type="text" id="id" placeholder="id">
<input type="text" id="number" placeholder="number">
<input type="button" value="Обновить" id="button-update">
<br>
<div id="result-update"></div>
<div id="error-update"></div>
<br>
</body>
</html>
