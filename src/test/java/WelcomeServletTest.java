import org.example.servlet.web_app.form.servlet.WelcomeServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

import static org.example.servlet.web_app.form.util.Constants.*;
import static org.mockito.Mockito.*;

public class WelcomeServletTest {
    private WelcomeServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new WelcomeServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void doGetWithPlayerCookieTest() throws Exception {
        Cookie playerCookie = new Cookie(PLAYER_NAME, "Rodrigo");
        when(request.getCookies()).thenReturn(new Cookie[]{playerCookie});
        when(request.getContextPath()).thenReturn("");

        Method doGetMethod = WelcomeServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doGetMethod.invoke(servlet, request, response);

        verify(response).sendRedirect("/game");
    }

    @Test
    void doGetWithoutPlayerCookieTest() throws Exception {
        when(request.getCookies()).thenReturn(null);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(WELCOME_PAGE)).thenReturn(dispatcher);

        Method doGetMethod = WelcomeServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doGetMethod.invoke(servlet, request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostWithValidName() throws Exception {
        when(request.getParameter(PLAYER_NAME)).thenReturn("Rodrigo");
        when(request.getContextPath()).thenReturn("");

        Method doPostMethod = WelcomeServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        doPostMethod.invoke(servlet, request, response);

        verify(response).sendRedirect("/game");

        verify(response, times(4)).addCookie(any(Cookie.class));
    }

    @Test
    void doPostWithInvalidNameTest() throws Exception {
        when(request.getParameter(PLAYER_NAME)).thenReturn("   ");
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(WELCOME_PAGE)).thenReturn(dispatcher);

        Method doPostMethod = WelcomeServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        doPostMethod.invoke(servlet, request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
