<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Удалить пользователя</title>
</head>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
    $('#button-delete').click(function () {
      var id =document.getElementById("id").value;
      $.ajax({
        type: 'DELETE',
        url: 'https://localhost:8444/service_component-1.0-SNAPSHOT/api/vehicle/'+id,
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

Вы действительно хотите удалить пользователя ${param.id}?

<input type="hidden" id="id" value="${param.id}">
<input type="button" value="Удалить" id="button-delete">
<br>
<div id="result-delete"></div>
<div id="error-delete"></div>
<form action = "show-vehicle.jsp">
    <input type="submit" value="Вернуться">
</form>
</body>
</html>