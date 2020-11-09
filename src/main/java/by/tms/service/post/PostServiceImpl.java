package by.tms.service.post;

import by.tms.dao.like.LikeDao;
import by.tms.dao.post.PostDao;
import by.tms.entity.Comment;
import by.tms.entity.Like;
import by.tms.entity.Post;
import by.tms.service.post.exception.DuplicatePostException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostDao postDao;
    private LikeDao likeDao;

    public PostServiceImpl(PostDao postDao, LikeDao likeDao) {
        this.postDao = postDao;
        this.likeDao = likeDao;
    }



    @Override
    public List<Post> getAllPosts() {
        return postDao.getAllPosts();
    }
    @Override
    public Optional<Post> getPostByTitle(String title) {
        return postDao.getPostByTitle(title);
    }
    @Override
    public Optional<Post> GetPostById(long id) {
        return postDao.getPostById(id);
    }



    @Override
    public void createPost(Post post) {
        if (postDao.existPostByTitle(post.getTitle())) {
            throw new DuplicatePostException();
        }
        postDao.createPost(post);
    }
    @Override
    public void deletePost(long postId) {
        postDao.deletePost(postId);
        likeDao.deleteLikesByPostId(postId);
    }



    @Override
    public void setComment(Comment comment, long postId) {
        // Т.к. коментарий является внутренним обьектом поста и имеется односторонняя связь со стороны поста -
        // изменяю поле обьекта Post и делаю updatePost(post)
        Post post = postDao.getPostById(postId).orElseThrow(RuntimeException::new);
        post.getComments().add(comment);
        postDao.updatePost(post);
    }
    @Override
    public void deleteComment(long commentId, long postId) {
        // Т.к. коментарий является внутренним обьектом поста и имеется односторонняя связь со стороны поста -
        // изменяю поле обьекта Post и делаю updatePost(post)
        Post post = postDao.getPostById(postId).orElseThrow(RuntimeException::new);
        List<Comment> comments = post.getComments()
                .stream()
                .filter(comment -> comment.getId() != commentId)
                .collect(Collectors.toList());
        post.setComments(comments);
        postDao.updatePost(post);
    }



    @Override
    public void trySetLike(Like like) {
        Post post = postDao.getPostById(like.getId().getPostId()).orElseThrow(RuntimeException::new);

        if (likeDao.existLikeById(like.getId())) {
            post.deleteLike();
            likeDao.deleteLikeById(like.getId());
        } else {
            post.addLike();
            likeDao.saveLike(like);
        }

        postDao.updatePost(post);
    }
    @Override
    public List<Like> getAllLikes(long postId) {
        return likeDao.getLikes(postId);
    }
}
