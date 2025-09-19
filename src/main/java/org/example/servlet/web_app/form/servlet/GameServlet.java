package org.example.servlet.web_app.form.servlet;

import org.example.servlet.web_app.form.model.GameNode;
import org.example.servlet.web_app.form.model.LastNode;
import org.example.servlet.web_app.form.model.Tree;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.example.servlet.web_app.form.util.Constants.*;

@WebServlet("/game")
public class GameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nodeId = getCookieValue(req, CURRENT_NODE);

        GameNode currentNode = nodeId != null ?
                Tree.findNodeById(Tree.ROOT, nodeId)
                : null;

        if (currentNode == null) {
            currentNode = Tree.ROOT;
            setNodeCookie(resp, CURRENT_NODE, currentNode.getId(), req.getContextPath());
        }

        req.setAttribute(CURRENT_NODE, currentNode);
        req.getRequestDispatcher(GAME_PAGE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String choice = req.getParameter("choice");
        String nodeId = getCookieValue(req, CURRENT_NODE);

        GameNode currentNode = (nodeId != null)
                ? Tree.findNodeById(Tree.ROOT, nodeId)
                : Tree.ROOT;


        if ("RESTART".equals(choice)) {
            currentNode = Tree.ROOT;
            setNodeCookie(resp, CURRENT_NODE, currentNode.getId(), req.getContextPath());
        }
        else if (currentNode != null && !currentNode.isFinalNode()) {
            GameNode nextNode = currentNode.getNextNode(choice);
            if (nextNode != null) {
                currentNode = nextNode;
                setNodeCookie(resp, CURRENT_NODE, currentNode.getId(), req.getContextPath());
            }
        }

        int gamesPlayed = Integer.parseInt(getCookieValue(req, GAMES_PLAYED));
        int gamesWon = Integer.parseInt(getCookieValue(req, GAMES_WON));
        int gamesLost = Integer.parseInt(getCookieValue(req, GAMES_LOST));

        if (currentNode.isFinalNode() && currentNode instanceof LastNode lastNode) {
            gamesPlayed++;
            if (lastNode.isWinning()) {
                gamesWon++;
            } else {
                gamesLost++;
            }

            setNodeCookie(resp, GAMES_PLAYED, String.valueOf(gamesPlayed), req.getContextPath());
            setNodeCookie(resp, GAMES_WON, String.valueOf(gamesWon), req.getContextPath());
            setNodeCookie(resp, GAMES_LOST, String.valueOf(gamesLost), req.getContextPath());
        }

        req.setAttribute("gamesPlayed", gamesPlayed);
        req.setAttribute("gamesWon", gamesWon);
        req.setAttribute("gamesLost", gamesLost);
        req.setAttribute(CURRENT_NODE, currentNode);

        req.getRequestDispatcher("/WEB-INF/jsp/game-content.jsp").forward(req, resp);
    }

    private String getCookieValue(HttpServletRequest req, String name) {
        return Optional.ofNullable(req.getCookies())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .filter(c -> name.equals(c.getName()))
                .map(c -> URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))
                .findFirst()
                .orElse(null);
    }

    private void setNodeCookie(HttpServletResponse resp, String name, String value, String path) {
        Cookie cookie = new Cookie(name, URLEncoder.encode(value, StandardCharsets.UTF_8));
        cookie.setPath(path);
        resp.addCookie(cookie);
    }
}