import org.example.servlet.web_app.form.model.GameNode;
import org.example.servlet.web_app.form.model.LastNode;
import org.example.servlet.web_app.form.model.Tree;
import org.example.servlet.web_app.form.servlet.GameServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.example.servlet.web_app.form.util.Constants.*;
import static org.mockito.Mockito.*;

class GameServletTest {
    private GameServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setup() {
        servlet = new GameServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void doGetRootNodeTest() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{});
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestDispatcher(GAME_PAGE)).thenReturn(dispatcher);

        Method doGetMethod = GameServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doGetMethod.invoke(servlet, request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostRestartTest() throws Exception {
        when(request.getParameter("choice")).thenReturn("RESTART");
        when(request.getContextPath()).thenReturn("");

        Cookie gamesPlayed = new Cookie(GAMES_PLAYED, "0");
        Cookie gamesWon = new Cookie(GAMES_WON, "0");
        Cookie gamesLost = new Cookie(GAMES_LOST, "0");
        when(request.getCookies()).thenReturn(new Cookie[]{gamesPlayed, gamesWon, gamesLost});

        when(request.getRequestDispatcher("/WEB-INF/jsp/game-content.jsp")).thenReturn(dispatcher);

        Method doPostMethod = GameServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        doPostMethod.invoke(servlet, request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostFinalNodeWinningTest() throws Exception {

        LastNode finalNode = (LastNode) Tree.findNodeById(Tree.ROOT, "candyTreeNode");

        when(request.getParameter("choice")).thenReturn("Seguir corriendo");
        when(request.getContextPath()).thenReturn("");

        Cookie gamesPlayed = new Cookie(GAMES_PLAYED, "0");
        Cookie gamesWon = new Cookie(GAMES_WON, "0");
        Cookie gamesLost = new Cookie(GAMES_LOST, "0");
        Cookie currentNode = new Cookie(CURRENT_NODE, finalNode.getId());
        when(request.getCookies()).thenReturn(new Cookie[]{gamesPlayed, gamesWon, gamesLost, currentNode});

        when(request.getRequestDispatcher("/WEB-INF/jsp/game-content.jsp")).thenReturn(dispatcher);

        Method doPostMethod = GameServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostLastNodeLosingTest() throws Exception {
        LastNode finalNode = (LastNode) Tree.findNodeById(Tree.ROOT, "sodaLakeNode");

        when(request.getParameter("choice")).thenReturn("Seguir corriendo");
        when(request.getContextPath()).thenReturn("");

        Cookie gamesPlayed = new Cookie(GAMES_PLAYED, "0");
        Cookie gamesWon = new Cookie(GAMES_WON, "0");
        Cookie gamesLost = new Cookie(GAMES_LOST, "0");
        Cookie currentNode = new Cookie(CURRENT_NODE, finalNode.getId());

        when(request.getCookies()).thenReturn(new Cookie[]{gamesPlayed, gamesWon, gamesLost, currentNode});
        when(request.getRequestDispatcher("/WEB-INF/jsp/game-content.jsp")).thenReturn(dispatcher);

        Method doPostMethod = GameServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostMiddleNodeTest() throws Exception {

        GameNode currentNode = Tree.ROOT;
        String choice = "Isla Dulce";

        Cookie currentNodeCookie = new Cookie(CURRENT_NODE, currentNode.getId());
        Cookie gamesPlayed = new Cookie(GAMES_PLAYED, "0");
        Cookie gamesWon = new Cookie(GAMES_WON, "0");
        Cookie gamesLost = new Cookie(GAMES_LOST, "0");

        when(request.getParameter("choice")).thenReturn(choice);
        when(request.getCookies()).thenReturn(new Cookie[]{currentNodeCookie, gamesPlayed, gamesWon, gamesLost});
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestDispatcher("/WEB-INF/jsp/game-content.jsp")).thenReturn(dispatcher);

        Method doPostMethod = GameServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, request, response);

        verify(dispatcher).forward(request, response);

        verify(response).addCookie(argThat(cookie ->
                CURRENT_NODE.equals(cookie.getName()) &&
                        "candyIslandNode".equals(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
        ));
    }
}