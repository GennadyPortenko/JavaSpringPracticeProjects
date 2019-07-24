package gpk.practice.spring.bootmvc.dto.info;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class UsersInfoDto {
    final List<String> users;
    final long totalNumberOfMessages;
}
