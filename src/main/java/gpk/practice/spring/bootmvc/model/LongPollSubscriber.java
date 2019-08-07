package gpk.practice.spring.bootmvc.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongPollSubscriber {
    @Getter
    @Setter
    DeferredResult<ResponseEntity<?>> response;
}
