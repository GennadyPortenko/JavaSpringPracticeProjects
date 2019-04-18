package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User registerNewUserAccount(User user) throws Exception {
        userRepository.save(user);
        return user;
    }
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
