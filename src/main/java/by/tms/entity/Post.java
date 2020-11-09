package by.tms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
//    private static final String DATE_PATTERN = "yyyy-MM-dd  HH:mm:ss";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String massage;
//    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "posts_author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "commented_post_id", referencedColumnName = "id")
    private List<Comment> comments = new ArrayList<>();

    private int likes;



    public void addLike() {likes++;}
    public void deleteLike() {likes--;}
//    public String showDate() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
//        return date.format(formatter);
//    }
}
