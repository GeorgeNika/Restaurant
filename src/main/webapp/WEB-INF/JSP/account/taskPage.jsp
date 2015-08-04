<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
</head>
<h3>Требования проекта</h3>

<h3>Уровень 1</h3>
<h4>
    Напишите веб-приложение для ресторана, в котором посетители смогут заказывать обеды:<br/>
    Открытая регистрация, авторизация пользователей.<br/>
    Меню ресторана, редактируется администраторами, имеет разделы (например “Первые блюда” и “Десерты”)
    Пользователи могут создавать заказы, отправлять заказы, редактировать еще не отправленные заказы.
</h4>

<h3> Уровень 2</h3>
<h4>
    Группы пользователей. Группы можно свободно создавать, покидать, удалить группу может ее создатель, вступать с
    согласия владельца группы.
    Пользователи могут создавать групповые заказы, отправлять их и редактировать еще не отправленные.
    Групповой заказ должен содержать
    детализацию по пользователям – кто что заказал, сколько стоит, сумму по пользователю и общую,
    детализацию по блюдам – сколько каждого блюда заказано, стоимость и сумма.
    Администраторы могут просматривать всю информацию, пользователи только заказы свои и групп, в которых они
    состоят.
</h4>

<h3>Уровень 3</h3>
<h4>
    Сделать форму группового заказа с использованием AJAX, где бы в реальном времени отображались изменения, сделанные
    как самим пользователем, так и другими членами группы.
</h4>
<button class="simple_button" onclick="window.location.href ='${context}/welcomeAction'">
    Close
</button>
</body>
</html>