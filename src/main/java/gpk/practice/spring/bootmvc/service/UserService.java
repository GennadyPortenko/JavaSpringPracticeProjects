package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.model.Role;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.repository.RoleRepository;
import gpk.practice.spring.bootmvc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User registerNewUserAccount(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }
    public User findByName(String username) {
        return userRepository.findByName(username);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public void deleteAll() { userRepository.deleteAll(); }
    public List<String > getUserNames() {
        return userRepository.findAll().stream().map(User::getName).collect(toList());
    }
}
