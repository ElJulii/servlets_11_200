import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbcImpl implements userRepository {
    private final Connection connection;
    private static final String SQL_SELECT_ALL_FROM_DRIVER = "select * from datausers";
    public UserRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List find_password_mail(String mail, String password) {

        try {
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_FROM_DRIVER);
            List<User> result = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("login"),
                        resultSet.getString("password")
                );
                if (user.getLogin().equals(mail) && user.getPassword().equals(password)) {
                    result.add(user);
                    System.out.println("Good");
                }
            }
            if (result.isEmpty()) {
                System.out.println("The user does not exist");
            }
            return result;

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Long getIdByPassword(String password) {
        try {
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_FROM_DRIVER);

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("login"),
                        resultSet.getString("password")

                );
                if (user.getPassword().equals(password)) {
                    return user.getId();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
