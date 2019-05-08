package gpk.practice.spring.bootmvc.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDto {
    @Getter
    @Setter
    long id;
    @Getter
    @Setter
    Instant datetime;
    @NotEmpty
    @Getter
    @Setter
    String text;
    @Getter
    @Setter
    String username;
    @Getter
    @Setter
    List<MessageDto> MessagesToReply;
}
