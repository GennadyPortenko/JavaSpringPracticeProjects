package gpk.practice.spring.bootmvc.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class NewMessageDto {
    String text;
    String username;
    List<IdDto> MessagesToReply = new ArrayList<>();
}
