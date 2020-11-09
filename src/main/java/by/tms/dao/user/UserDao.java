package by.tms.dao.user;

import by.tms.entity.User;
import java.util.Optional;

public interface UserDao {
    void createUser(User user);
    Optional<User> getUserById(long id);
    Optional<User> getUserByLogin(String login);

}
