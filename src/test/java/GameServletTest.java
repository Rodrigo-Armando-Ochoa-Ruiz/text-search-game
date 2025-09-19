import org.example.servlet.web_app.form.model.GameNode;
import org.example.servlet.web_app.form.model.LastNode;
import org.example.servlet.web_app.form.model.Tree;
import org.example.servlet.web_app.form.servlet.GameServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.example.servlet.web_app.form.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
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

        ArgumentCaptor<GameNode> nodeCaptor = ArgumentCaptor.forClass(GameNode.class);
        verify(request).setAttribute(eq(CURRENT_NODE), nodeCaptor.capture());

        GameNode capturedNode = nodeCaptor.getValue();

        assertEquals(Tree.ROOT.getId(), capturedNode.getId());

        verify(dispatcher).forward(request, response);

        verify(response).addCookie(argThat(cookie -> CURRENT_NODE.equals(cookie.getName())));
    }

    @Test
    void doPostRestartTest() throws Exception {
        when(request.getParameter("choice")).thenReturn("RESTART");
        when(request.getContextPath()).thenReturn("");

        when(request.getCookies()).thenReturn(initCokkies());

        when(request.getRequestDispatcher("/WEB-INF/jsp/game-content.jsp")).thenReturn(dispatcher);

        Method doPostMethod = GameServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, request, response);

        verify(response).addCookie(argThat(cookie ->
                CURRENT_NODE.equals(cookie.getName()) &&
                        Tree.ROOT.getId().equals(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
        ));

        ArgumentCaptor<GameNode> nodeCaptor = ArgumentCaptor.forClass(GameNode.class);

        verify(request).setAttribute(eq(CURRENT_NODE), nodeCaptor.capture());

        GameNode capturedNode = nodeCaptor.getValue();

        assertEquals(Tree.ROOT.getId(), capturedNode.getId());
    }


    @Test
    void doPostFinalNodeWinningTest() throws Exception {

        LastNode finalNode = (LastNode) Tree.findNodeById(Tree.ROOT, "candyTreeNode");
        assertTrue(finalNode.isWinning(), "El nodo debería ser ganador");

        when(request.getParameter("choice")).thenReturn("Seguir corriendo");
        when(request.getContextPath()).thenReturn("");

        when(request.getCookies()).thenReturn(initCokkies(finalNode.getId()));

        when(request.getRequestDispatcher("/WEB-INF/jsp/game-content.jsp")).thenReturn(dispatcher);

        Method doPostMethod = GameServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, request, response);

        ArgumentCaptor<GameNode> nodeCaptor = ArgumentCaptor.forClass(GameNode.class);
        verify(request).setAttribute(eq(CURRENT_NODE), nodeCaptor.capture());
        GameNode capturedNode = nodeCaptor.getValue();
        assertEquals(finalNode.getId(), capturedNode.getId());

        verify(request).setAttribute(eq("gamesPlayed"), eq(1));  // se incrementa en 1
        verify(request).setAttribute(eq("gamesWon"), eq(1));     // ganador
        verify(request).setAttribute(eq("gamesLost"), eq(0));    // no cambia


        verify(dispatcher).forward(request, response);

        verify(response).addCookie(argThat(c -> GAMES_PLAYED.equals(c.getName()) && "1".equals(URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))));
        verify(response).addCookie(argThat(c -> GAMES_WON.equals(c.getName()) && "1".equals(URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))));
        verify(response).addCookie(argThat(c -> GAMES_LOST.equals(c.getName()) && "0".equals(URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))));
    }


    @Test
    void doPostLastNodeLosingTest() throws Exception {
        LastNode finalNode = (LastNode) Tree.findNodeById(Tree.ROOT, "sodaLakeNode");
        assertFalse(finalNode.isWinning(), "El nodo debería ser perdedor");

        when(request.getParameter("choice")).thenReturn("Seguir corriendo");
        when(request.getContextPath()).thenReturn("");

        when(request.getCookies()).thenReturn(initCokkies(finalNode.getId()));

        when(request.getRequestDispatcher("/WEB-INF/jsp/game-content.jsp")).thenReturn(dispatcher);

        Method doPostMethod = GameServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, request, response);

        ArgumentCaptor<GameNode> nodeCaptor = ArgumentCaptor.forClass(GameNode.class);
        verify(request).setAttribute(eq(CURRENT_NODE), nodeCaptor.capture());
        GameNode capturedNode = nodeCaptor.getValue();
        assertEquals(finalNode.getId(), capturedNode.getId());

        verify(request).setAttribute(eq("gamesPlayed"), eq(1));
        verify(request).setAttribute(eq("gamesWon"), eq(0));
        verify(request).setAttribute(eq("gamesLost"), eq(1));

        verify(dispatcher).forward(request, response);

        verify(response).addCookie(argThat(c -> GAMES_PLAYED.equals(c.getName()) && "1".equals(URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))));
        verify(response).addCookie(argThat(c -> GAMES_WON.equals(c.getName()) && "0".equals(URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))));
        verify(response).addCookie(argThat(c -> GAMES_LOST.equals(c.getName()) && "1".equals(URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))));
    }


    @Test
    void doPostMiddleNodeTest() throws Exception {
        GameNode currentNode = Tree.ROOT;
        String choice = "Isla Dulce";

        when(request.getParameter("choice")).thenReturn(choice);
        when(request.getCookies()).thenReturn(initCokkies(currentNode.getId()));
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

        ArgumentCaptor<GameNode> nodeCaptor = ArgumentCaptor.forClass(GameNode.class);
        verify(request).setAttribute(eq(CURRENT_NODE), nodeCaptor.capture());

        GameNode capturedNode = nodeCaptor.getValue();
        assertEquals("candyIslandNode", capturedNode.getId());
    }


    private Cookie [] initCokkies(String currentNode){
        return new Cookie[]{
            new Cookie(GAMES_PLAYED, "0"),
            new Cookie(GAMES_WON, "0"),
            new Cookie(GAMES_LOST, "0"),
            new Cookie(CURRENT_NODE, currentNode),
            };
    }

    private Cookie [] initCokkies(){
        return new Cookie[]{
                new Cookie(GAMES_PLAYED, "0"),
                new Cookie(GAMES_WON, "0"),
                new Cookie(GAMES_LOST, "0")
        };
    }
}