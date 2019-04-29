package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.Role;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.repository.RoleRepository;
import gpk.practice.spring.bootmvc.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageService {
    // final UserRepository messageRepository;

    /*
    public Message saveMessage(Message message) {
        message.setDatetime(Instant.now());
        return messageRepository.save(Message);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    */

}
