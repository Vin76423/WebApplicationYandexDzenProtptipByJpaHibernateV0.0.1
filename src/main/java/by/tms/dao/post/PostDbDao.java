package by.tms.dao.post;

import by.tms.entity.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class PostDbDao implements PostDao {

    @PersistenceContext
    private EntityManager entityManager;


    // ......................................DSL.............................................................
    @Override
    public List<Post> getAllPosts() {
        List<Post> postList = entityManager.createQuery("from Post", Post.class).getResultList();
        return postList;
    }
    @Override
    public Optional<Post> getPostByTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title is null!");
        }
        String hql = "from Post p where p.title = :title";
        Optional<Post> optionalPost = entityManager.createQuery(hql, Post.class)
                .setParameter("title", title)
                .getResultStream()
                .findAny();
        entityManager.clear();
        return optionalPost;
    }
    @Override
    public Optional<Post> getPostById(long postId) {
        if (postId < 1) {
            throw new IllegalArgumentException("PostId is not correct!");
        }
//        Долбанет EntityNotFoundException, если не найдет искомый обьект:

//        Post post = entityManager.getReference(Post.class, postId);

        Post post = entityManager.find(Post.class, postId);
        entityManager.clear();
        return Optional.ofNullable(post);
    }


    // ......................................DML.............................................................
    @Override
    public void createPost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post is null!");
        }
        // При использовании метода persist(post), метод flush() делать не нужно, т.к.
        // метод persist(post) сам автоматически заносит изменения в БД:
        entityManager.persist(post);
        entityManager.clear();
    }
    @Override
    public void updatePost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post is null!");
        }
        // При использовании DML методов таких как merge(post) и remove(post), изменения в БД сразу НЕ попадают,
        // при любых операциях метод flush() выполняется после завершения сеанса/транзакции, сл-но
        // если в конце метода мы чистим кеш методом clear(), метод flush() нужно выполнять вручную, иначе
        // изменения в БД так и не попадут:
        entityManager.merge(post);
        entityManager.flush();
        entityManager.clear();
    }
    @Override
    public void deletePost(long postId) {
        if (postId < 1) {
            throw new IllegalArgumentException("PostId is not correct!");
        }

//        Все-таки это запрос, прокаченный HQL но все же запрос, поэтому работает более локально в рамках одной таблицы
//        (если без джоинов, а для DМL оперций помоему Hibernate джойны и не использует), поэтому в данном случае схватим
//        оштбку: org.postgresql.util.PSQLException: ОШИБКА: UPDATE или DELETE в таблице "posts" нарушает ограничение внешнего ключа
//        "fka22m6vdy2ysj0mh135mdycs1s" таблицы "comment".
//        Вывод: для комплексных DМL операций лучше использовать методы remove(post), merge(post) с последующим flush()
//        (полагаю они разбивают комплексную операцию на составляющие внутри себя и выполняют средствами HQL) :

//        currentSession.createQuery("delete from Post p where p.id = :postId")
//                .setParameter("postId", postId).
//                executeUpdate();

        Post post = entityManager.getReference(Post.class, postId);
        entityManager.remove(post);
        entityManager.flush();
        entityManager.clear();
    }


    //......................................isExist...............................................
    @Override
    public boolean existPostByTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title is null!");
        }
        String hql = "select count(*) from Post p where p.title = :title";
        long result = (Long) entityManager.createQuery(hql)
                .setParameter("title", title)
                .getSingleResult();
        entityManager.clear();
        return result > 0;
    }
    @Override
    public boolean existPostById(long postId) {
        if (postId < 1) {
            throw new IllegalArgumentException("PostId is not correct!");
        }
        Post post = entityManager.find(Post.class, postId);
        entityManager.clear();
        return post != null;
    }
}
