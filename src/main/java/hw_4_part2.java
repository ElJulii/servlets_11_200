import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/log_in")
public class hw_4_part2 extends HttpServlet {
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
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String userUUID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("UUID")) {
                    userUUID = cookie.getValue();
                    break;
                }
            }
        }
        if (userUUID != null) {
            if (userExistsInTable(userUUID)) {
                response.sendRedirect("/welcome");
                return;
            }
        }
        request.getRequestDispatcher("/HTML/log_in.html").forward(request, response);
    }
    private boolean userExistsInTable(String userUUID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT COUNT(*) FROM datausers WHERE uuid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userUUID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            userRepository userRepository =new UserRepositoryJdbcImpl(connection);
            String mail = request.getParameter("login");
            String password = request.getParameter("password");
            List<User> password_mail = userRepository.find_password_mail(mail, password);
            if (!password_mail.isEmpty()) {
                response.sendRedirect("/welcome");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
