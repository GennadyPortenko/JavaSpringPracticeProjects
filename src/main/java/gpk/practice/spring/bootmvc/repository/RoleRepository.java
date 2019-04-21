package gpk.practice.spring.bootmvc.repository;

import gpk.practice.spring.bootmvc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
