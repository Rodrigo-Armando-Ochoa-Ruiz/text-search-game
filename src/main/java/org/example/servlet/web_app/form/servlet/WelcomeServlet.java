package org.example.servlet.web_app.form.servlet;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.example.servlet.web_app.form.util.Constants.*;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName = Optional.ofNullable(req.getCookies())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .filter(cookie -> PLAYER_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (playerName != null) {
            resp.sendRedirect(req.getContextPath() + "/game");
        } else {
            req.getRequestDispatcher(WELCOME_PAGE).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName = req.getParameter(PLAYER_NAME);
        if (StringUtils.isBlank(playerName)){
            req.setAttribute("error", "Nombre inválido");
            req.getRequestDispatcher(WELCOME_PAGE).forward(req,resp);
            return;
        }
        List<Cookie> dataCookies = List.of(
                new Cookie(PLAYER_NAME, URLEncoder.encode(playerName, StandardCharsets.UTF_8)),
                new Cookie(GAMES_PLAYED, URLEncoder.encode("0", StandardCharsets.UTF_8)),
                new Cookie(GAMES_WON, URLEncoder.encode("0", StandardCharsets.UTF_8)),
                new Cookie(GAMES_LOST,URLEncoder.encode("0", StandardCharsets.UTF_8))
        );

        dataCookies.forEach(cookie -> {
            cookie.setPath(req.getContextPath());
            resp.addCookie(cookie);
        });

        resp.sendRedirect(req.getContextPath() + "/game");
    }
}
