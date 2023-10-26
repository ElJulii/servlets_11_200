import java.util.List;

public interface userRepository <T> {
    List<T> find_password_mail(String mail, String password);
    Long getIdByPassword(String password);
}
