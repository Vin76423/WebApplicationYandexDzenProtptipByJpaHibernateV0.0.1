package by.tms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
//    private static final String DATE_PATTERN = "yyyy-MM-dd  HH:mm:ss";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String massage;
//    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "comments_author_id", referencedColumnName = "id")
    private User author;



//    public String showDate() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
//        return date.format(formatter);
//    }
}
