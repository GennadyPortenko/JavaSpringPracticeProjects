package gpk.practice.spring.bootmvc.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDto {
    @Getter
    @Setter
    int id;
    @NotNull
    @Getter
    @Setter
    Instant datetime;
    @NotEmpty
    @Getter
    @Setter
    String text;
}
