import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/welcome")
public class welcome extends HttpServlet {
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Arlet_0804";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/11-200";
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/allUsers.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        List<User> userList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from datausers");

            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String login = result.getString("login");
                String password = result.getString("password");
                userList.add(new User(id, name, surname, login, password));
            }

        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("userList", userList);
    }
}
