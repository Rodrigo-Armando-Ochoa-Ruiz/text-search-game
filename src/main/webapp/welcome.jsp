<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Bienvenido al Juego</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f0f8ff; text-align: center; padding-top: 50px; }
        input[type=text] { padding: 5px; font-size: 16px; }
        input[type=submit] { padding: 5px 15px; font-size: 16px; cursor: pointer; }
        .error { color: red; margin-top: 10px; }
    </style>
</head>
<body>
    <h1>¡Hola, <c:out value="${sessionScope.playerName}" />! Bienvenido al Juego 🎮</h1>
    <p>Ingresa tu nombre para comenzar:</p>

    <form method="post" action="${pageContext.request.contextPath}/welcome">
        <input type="text" name="playerName" placeholder="Tu nombre">
        <input type="submit" value="Comenzar">
    </form>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
</body>
</html>