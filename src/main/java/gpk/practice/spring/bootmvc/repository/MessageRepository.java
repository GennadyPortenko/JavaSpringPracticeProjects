package gpk.practice.spring.bootmvc.repository;

import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    Message findById(long id);
    Set<Message> findByUser(User user);

    List<Message> findTop20ByOrderByMessageIdDesc();
    List<Message> findTop20ByMessageIdLessThanOrderByMessageIdDesc(long id);

    @Query( nativeQuery = true,
            value = "SELECT COUNT (*) FROM message;" )
    long getNumberOfMessages();

    @Query( nativeQuery = true,
            value= "SELECT COUNT (*) FROM message WHERE user_fk = (SELECT user_id FROM account WHERE name = ?1);" )
    long getNumberOfMessagesOfUser(String userName);

    void deleteAll();
}
