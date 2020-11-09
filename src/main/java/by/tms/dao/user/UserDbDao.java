package by.tms.dao.user;


import by.tms.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional
public class UserDbDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null!");
        }
        entityManager.persist(user);
        entityManager.clear();
    }

    @Override
    public Optional<User> getUserById(long userId) {
        if (userId < 1) {
            throw new IllegalArgumentException("UserId is not correct!");
        }
        User user = entityManager.find(User.class, userId);
        entityManager.clear();
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Login is null!");
        }
        String sql = "from User u where u.login = :login";
        Optional<User> optionalUser = entityManager.createQuery(sql, User.class)
                .setParameter("login", login)
                .getResultStream()
                .findAny();
        entityManager.clear();
        return optionalUser;
    }
}
