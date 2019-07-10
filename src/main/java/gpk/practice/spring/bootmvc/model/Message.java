package gpk.practice.spring.bootmvc.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long messageId;
    @NotNull
    Instant datetime;
    @NotEmpty
    String text;
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_fk")
    User user;
    @ManyToMany( cascade = { CascadeType.PERSIST, CascadeType.MERGE },
                 targetEntity = gpk.practice.spring.bootmvc.model.Message.class)
    @JoinTable(name = "message_message_to_reply", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "message_to_reply_id"))
    List<Message> MessagesToReply;
}
