import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/sign_up")
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
    request.getRequestDispatcher("/HTML/sign_up.html").forward(request, response);
    }
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        userRepository userRepository = new UserRepositoryJdbcImpl(connection);
        Statement statement = connection.createStatement();


        String mail = request.getParameter("login");
        String password = request.getParameter("password");
        String firstName = request.getParameter("name");
        String surname = request.getParameter("surname");

        String sqlInsertUser = "insert into datausers(name, surname, login, password) values('" +
                firstName +"', '" + surname + "', '" + mail + "','" + password +"');";

        List<User> password_mail = userRepository.find_password_mail(mail, password);
        if (!password_mail.isEmpty()) {
            response.sendRedirect("/log_in");
        }
        int affectedRows2 = statement.executeUpdate(sqlInsertUser);

    } catch (SQLException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
}
