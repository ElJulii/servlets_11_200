import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.UUID;

@WebServlet("/log_in")
public class homeWork_4 extends HttpServlet {
    protected static final String DB_USER = "postgres";
    protected static final String DB_PASSWORD = "Arlet_0804";
    protected static final String DB_URL = "jdbc:postgresql://localhost:5432/11-200";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            System.out.println(cookie.getValue() + " " + cookie.getName());
//        }
//        request.getRequestDispatcher("/HTML/log_in.html").forward(request, response);
        boolean cookieExists = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UUID".equals(cookie.getName())) {
                    cookieExists = true;
                    break;
                }
            }
        }
        if (cookieExists) {
            response.sendRedirect("/welcome");
        } else {
            response.sendRedirect("/log_in");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            userRepository userRepository =new UserRepositoryJdbcImpl(connection);
            Statement statement = connection.createStatement();

            String uuid = UUID.randomUUID().toString();
            Cookie uuidCookie = new Cookie("UUID", uuid);

            String mail = request.getParameter("login");
            String password = request.getParameter("password");
            long id = userRepository.getIdByPassword(password);
            List<User> password_mail = userRepository.find_password_mail(mail, password);
            String insertQuery = "";
            if (!password_mail.isEmpty()) {
                insertQuery = "insert into user_uuid (id_uer, uuid) values ('" + id + "', '" + uuid + "');";
                response.addCookie(uuidCookie);
                response.sendRedirect("/welcome");
            }
            int affectedRows = statement.executeUpdate(insertQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
