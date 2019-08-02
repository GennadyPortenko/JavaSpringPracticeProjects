package gpk.practice.spring.bootmvc.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongPollRequest {
    @Getter
    @Setter
    long lastMessageId;
    @Getter
    @Setter
    long lastDeletedMessageId;
}
