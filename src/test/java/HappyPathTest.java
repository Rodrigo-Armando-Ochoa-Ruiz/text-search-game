import org.example.servlet.web_app.form.servlet.GameServlet;
import org.example.servlet.web_app.form.servlet.WelcomeServlet;
import org.example.servlet.web_app.form.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

public class HappyPathTest {

    private WelcomeServlet welcomeServlet;
    private GameServlet gameServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        welcomeServlet = new WelcomeServlet();
        gameServlet = new GameServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void fullHappyPath_userWins() throws Exception {
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter(Constants.PLAYER_NAME)).thenReturn("Rodrigo");
        when(request.getContextPath()).thenReturn("");

        Method welcomeDoPost = WelcomeServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        welcomeDoPost.setAccessible(true);
        welcomeDoPost.invoke(welcomeServlet, request, response);

        verify(response, atLeast(1)).sendRedirect("/game");

        Method gameDoPost = GameServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        gameDoPost.setAccessible(true);

        Cookie playerNameCookie = new Cookie(Constants.PLAYER_NAME, "Rodrigo");
        Cookie gamesPlayed = new Cookie(Constants.GAMES_PLAYED, "0");
        Cookie gamesWon = new Cookie(Constants.GAMES_WON, "0");
        Cookie gamesLost = new Cookie(Constants.GAMES_LOST, "0");

        Cookie currentNode = new Cookie(Constants.CURRENT_NODE, "arrivalNode");
        when(request.getCookies()).thenReturn(new Cookie[]{playerNameCookie, gamesPlayed, gamesWon, gamesLost, currentNode});
        when(request.getParameter("choice")).thenReturn("Isla Dulce");
        gameDoPost.invoke(gameServlet, request, response);

        currentNode = new Cookie(Constants.CURRENT_NODE, "candyIslandNode");
        when(request.getCookies()).thenReturn(new Cookie[]{playerNameCookie, gamesPlayed, gamesWon, gamesLost, currentNode});
        when(request.getParameter("choice")).thenReturn("Construir una casa de galletas");
        gameDoPost.invoke(gameServlet, request, response);

        currentNode = new Cookie(Constants.CURRENT_NODE, "candyWizardNode");
        when(request.getCookies()).thenReturn(new Cookie[]{playerNameCookie, gamesPlayed, gamesWon, gamesLost, currentNode});
        when(request.getParameter("choice")).thenReturn("Pedir ayuda a un caramelo viviente");
        gameDoPost.invoke(gameServlet, request, response);

        verify(response, atLeast(3)).addCookie(any(Cookie.class));

        verify(response, atLeast(1)).sendRedirect("/game");

        verify(response).addCookie(argThat(cookie ->
                Constants.GAMES_WON.equals(cookie.getName()) &&
                        "1".equals(cookie.getValue())
        ));

        verify(response).addCookie(argThat(cookie ->
                Constants.CURRENT_NODE.equals(cookie.getName()) &&
                        "buildGingerHouseNode".equals(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
        ));
    }
}
