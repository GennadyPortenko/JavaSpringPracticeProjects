package gpk.practice.spring.bootmvc.repository;

import gpk.practice.spring.bootmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String username);
    User findByEmail(String email);
}
