package by.tms.dao.like;

import by.tms.entity.Like;
import by.tms.entity.embeddable.LikeId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class LikeDbDao implements LikeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Like> getLikes(long postId) {
        if (postId < 1) {
            throw new IllegalArgumentException("PostID is not correct!");
        }
        List<Like> likes = entityManager.createQuery("from Like l where l.id.postId = :postId", Like.class)
                .setParameter("postId", postId)
                .getResultList();
        entityManager.clear();
        return likes;
    }

    @Override
    public void saveLike(Like like) {
        if (like == null) {
            throw new IllegalArgumentException("Like is null!");
        }
        entityManager.persist(like);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public void deleteLikesByPostId(long postId) {
        if (postId < 1) {
            throw new IllegalArgumentException("PostID is not correct!");
        }
//      Т.к. сдесь я в DML-скрипте НЕ пытаюсь сделать не явный джоин, а всего лишь прошу залесть внутрь обьекта в рамках одной таблици,
//      все работает нормально (напиминание: редактирование СДЖоиненых записей что в SQL что в HQL - запрещено, джоины в таком случае можно
//      использовать в качестве подзапроса после секции WHERE):
        String hql = "delete from Like l where l.id.postId = :postId";
        entityManager.createQuery(hql)
                .setParameter("postId", postId)
                .executeUpdate();
        entityManager.clear();
    }

    @Override
    public void deleteLikeById(LikeId likeId) {
        if (likeId == null) {
            throw new IllegalArgumentException("LikeID is null!");
        }
        Like like = entityManager.find(Like.class, likeId);
        entityManager.remove(like);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public boolean existLikeById(LikeId likeId) {
        String hql = "select count(*) from Like l where l.id = :likeId";
        long result = (Long) entityManager.createQuery(hql)
                .setParameter("likeId", likeId)
                .getSingleResult();
        entityManager.clear();
        return result > 0;
    }
}
