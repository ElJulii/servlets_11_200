import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/settings")
public class SettingsPage extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] color = request.getCookies();
        for (Cookie cookie : color) {
            System.out.println(cookie.getValue() + " " + cookie.getName());
        }
        request.getRequestDispatcher("/jsp/settings.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String color = request.getParameter("color");
        Cookie colorCookie = new Cookie("color", color);
        response.addCookie(colorCookie);
        response.sendRedirect("/settings");
    }
}
