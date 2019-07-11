package gpk.practice.spring.bootmvc.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDto {
    @NonNull
    long id;
    @NonNull
    Instant datetime;
    @NonNull
    @NotEmpty
    String text;
    @NonNull
    String username;
    @NonNull
    List<MessageDto> MessagesToReply;
}
