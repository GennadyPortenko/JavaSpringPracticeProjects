package gpk.practice.spring.bootmvc.repository;

import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    Message findById(long id);
    Message findByText(String text);
    Set<Message> findByUser(User user);
    Message findByModified(Instant modified);

    List<Message> findTop20ByOrderByMessageIdDesc();
    List<Message> findTop20ByMessageIdLessThanOrderByMessageIdDesc(long id);

    @Query( nativeQuery = true,
            value = "SELECT COUNT (*) FROM message;" )
    long getNumberOfMessages();

    @Query( nativeQuery = true,
            value= "SELECT COUNT (*) FROM message WHERE user_fk = (SELECT user_id FROM account WHERE name = ?1);" )
    long getNumberOfMessagesOfUser(String userName);

    List<Message> findByDeletedGreaterThanEqual(Instant deleted);
    List<Message> findByModifiedGreaterThanEqual(Instant modified);

    @Query( nativeQuery = true,
            value= "SELECT * FROM message WHERE deleted IS NOT NULL ORDER BY deleted DESC limit 1;" )
    Message findTopDeleted();

    @Query( nativeQuery = true,
            value= "SELECT * FROM message WHERE modified IS NOT NULL ORDER BY modified DESC limit 1;" )
    Message findTopModified();

    void deleteAll();
}
