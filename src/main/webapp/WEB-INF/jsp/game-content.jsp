<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="game-node">

    <c:choose>
        <c:when test="${currentNode.finalNode}">
            <c:if test="${not empty gamesPlayed}">
                <div class="game-stats">
                    <h3>📊 Estadísticas ${sessionScope.playerName}</h3>
                    <p>Juegos jugados: ${gamesPlayed}</p>
                    <p>Juegos ganados: ${gamesWon}</p>
                    <p>Juegos perdidos: ${gamesLost}</p>
                </div>
            </c:if>
            <p>${currentNode.text}</p>
            <button id="restart-btn" type="button">Reiniciar juego</button>
        </c:when>

        <c:otherwise>
            <h3>${currentNode.text}</h3>
            <div class="game-options">
                <c:forEach var="entry" items="${currentNode.choicesMap}">
                    <button type="button" class="choice-btn" data-choice="${entry.key}">
                        ${entry.key}
                    </button>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>


<script>
    $(function () {
        $(".game-node").on("click", ".choice-btn", function () {
            var choice = $(this).data("choice");

            $.ajax({
                url: "${pageContext.request.contextPath}/game",
                method: "POST",
                data: { choice: choice },
                success: function (response) {
                    $("#game-container").html(response);
                },
                error: function () {
                    alert("Ocurrió un error al procesar la elección.");
                }
            });
        });

        $(".game-node").on("click", "#restart-btn", function () {
            $.ajax({
                url: "${pageContext.request.contextPath}/game",
                method: "POST",
                data: { choice: "RESTART" },
                success: function (response) {
                    $("#game-container").html(response);
                },
                error: function () {
                    alert("No se pudo reiniciar el juego.");
                }
            });
        });
    });
</script>
