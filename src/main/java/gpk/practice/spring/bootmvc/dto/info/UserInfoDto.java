package gpk.practice.spring.bootmvc.dto.info;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class UserInfoDto {
    final String name;
    final Long numberOfMessages;
}
