<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Funny Game</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f0f0f5;
            text-align: center;
            padding: 30px;
        }
        #game-container {
            background: #fff;
            border-radius: 8px;
            padding: 20px;
            margin: auto;
            max-width: 600px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        button.choice-btn {
            margin: 10px;
            padding: 10px 20px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            background: #007BFF;
            color: white;
            cursor: pointer;
        }
        button.choice-btn:hover {
            background: #0056b3;
        }

        .game-stats {
                background-color: #e0f7fa;
                border-radius: 6px;
                padding: 10px;
                margin-bottom: 20px;
            }
            .game-stats p {
                margin: 4px 0;
            }
    </style>
</head>
<body>
    <h1>Bienvenido al Juego Interactivo 🎮</h1>

    <div id="game-container">
        <jsp:include page="/WEB-INF/jsp/game-content.jsp"/>
    </div>
</body>
</html>
