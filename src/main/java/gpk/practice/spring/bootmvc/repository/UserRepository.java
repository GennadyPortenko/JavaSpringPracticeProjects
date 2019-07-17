package gpk.practice.spring.bootmvc.repository;

import gpk.practice.spring.bootmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface UserRepository extends JpaRepository<User, Long> {
    void deleteAll();
    List<User> findAll();
    User findByName(String username);
    User findByEmail(String email);
    User findByUserId(int id);
}
