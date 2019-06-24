package gpk.practice.spring.bootmvc.sqlquery;

import gpk.practice.spring.bootmvc.model.Message;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public abstract class SQLQueries {
    @Query("SELECT * from message as m WHERE m.id > 50;")
    public abstract List<Message> findPrevious(long id);
}
