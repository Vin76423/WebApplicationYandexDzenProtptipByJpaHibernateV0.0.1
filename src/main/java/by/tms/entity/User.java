package by.tms.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Name is empty!")
    @NotBlank(message = "Name is empty!")
    private String name;
    @NotEmpty(message = "Login is empty!")
    @NotBlank(message = "Login is empty!")
    private String login;
    @NotEmpty(message = "Password is empty!")
    @NotBlank(message = "Password is empty!")
    private String password;
    private int age;

}
