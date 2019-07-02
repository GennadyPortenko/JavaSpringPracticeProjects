package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.jpaspecification.MessageSpecifications;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.repository.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageService {
    final MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        message.setDatetime(Instant.now());
        List<Message> messagesToReply = new ArrayList<>();
        message.getMessagesToReply().forEach(msgToRply ->
            messagesToReply.add(findById(msgToRply.getId()))
        );
        message.setMessagesToReply(messagesToReply);
        return messageRepository.save(message);
    }

    public Message findById(long id) {
        return messageRepository.findById(id);
    }
    public Set<Message> findByUser(User user) {
        return messageRepository.findByUser(user);
    }
    public List<Message> findAll(){
        return messageRepository.findAll();
    }
    public List<Message> findAllAfterId(long id) {
        return messageRepository.findAll(MessageSpecifications.IdMoreThan(id));
    }
}
