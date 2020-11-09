package by.tms.dao.like;

import by.tms.entity.Like;
import by.tms.entity.User;
import by.tms.entity.embeddable.LikeId;

import java.util.List;

public interface LikeDao {
    List<Like> getLikes(long postId);
    void saveLike(Like like);
    void deleteLikesByPostId(long postId);
    void deleteLikeById(LikeId likeId);
    boolean existLikeById(LikeId likeId);
}
