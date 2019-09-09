package gpk.practice.spring.bootmvc.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongPollRequest {
    @Getter
    @Setter
    Long firstMessageId;
    @Getter
    @Setter
    Long lastMessageId;
    @Getter
    @Setter
    Long lastDeletedMessageId;
    @Getter
    @Setter
    Long lastModifiedMessageId;
}
