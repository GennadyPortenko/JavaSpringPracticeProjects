package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.model.LongPollSubscriber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SubscribersManager {
    private List<LongPollSubscriber> subscribers = new CopyOnWriteArrayList<>();
    public void addSubscriber(LongPollSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    private void removeSubscriber(LongPollSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    // long polling timeout (no new messages available)
    public void abortSubscriber(LongPollSubscriber subscriber) {
        subscriber.getResponse().setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        removeSubscriber(subscriber);
    }

    public void broadcast() {
        for (LongPollSubscriber subscriber : subscribers) {
            List<MessageDto> response = new ArrayList<>();
            response.add(new MessageDto(1, Instant.now(), "Message 1 text"));
            response.add(new MessageDto(2, Instant.now(), "Message 2 text"));
            subscriber.getResponse().setResult(new ResponseEntity<>(response, HttpStatus.OK));
            removeSubscriber(subscriber);
        }
    }
}
