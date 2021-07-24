<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Profile</title>

    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          crossorigin="anonymous">
</head>
<body>
<header>
    <nav class="navbar navbar-expand-md navbar-dark"
         style="background-color: tomato">
        <div>
            <a href="https://vk.com/artpheus" class="navbar-brand"> Todo App</a>
        </div>

        <ul class="navbar-nav navbar-collapse justify-content-end">
            <li><a href="/list" class="nav-link">Todo</a></li>
            <li><a href="/logout" class="nav-link">Logout</a></li>
        </ul>
    </nav>
</header>
<h1><c:out value="${user.username}" /></h1>
<h1><c:out value="${user.firstName}" /></h1>
<h1><c:out value="${user.lastName}" /></h1>
<form action="/profile" method="post">
    <input type="text" name="firstName" placeholder="first name">
    <input type="text" name="lastName" placeholder="last name">
    <input type="submit" value="update info">
</form>
</body>
</html>