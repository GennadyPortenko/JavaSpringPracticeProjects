package gpk.practice.spring.bootmvc.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class MessageDto {
    long id;
    String datetime;
    String text;
    String username;
    Instant deleted;
    Instant modified;
    List<MessageDto> MessagesToReply;
}
