package gpk.practice.spring.bootmvc.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long messageId;
    @NonNull
    @NotNull
    Instant datetime;
    @NonNull
    @NotEmpty
    String text;
    @NonNull
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_fk")
    User user;
    @NonNull
    @ManyToMany( cascade = { CascadeType.PERSIST, CascadeType.MERGE },
                 targetEntity = gpk.practice.spring.bootmvc.model.Message.class)
    @JoinTable(name = "message_message_to_reply", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "message_to_reply_id"))
    List<Message> MessagesToReply = new ArrayList<>();
}
