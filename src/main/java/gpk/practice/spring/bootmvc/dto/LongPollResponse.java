package gpk.practice.spring.bootmvc.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongPollResponse {
    @Getter
    @Setter
    LongPollResponseType type;
    @Getter
    @Setter
    List<MessageDto> messages;
}
