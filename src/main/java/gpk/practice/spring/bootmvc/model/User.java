package gpk.practice.spring.bootmvc.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "account")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userId;
    @NonNull
    @NotEmpty
    @Email
    String email;
    @NonNull
    @NotEmpty
    String name;
    @NonNull
    @NotEmpty
    String password;
    int active;
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();
    @OneToMany(fetch=FetchType.LAZY, mappedBy="user")
    Set<Message> messages = new HashSet<>();
}
