package gpk.practice.spring.bootmvc.jpaspecification;

import gpk.practice.spring.bootmvc.model.Message;
import org.springframework.data.jpa.domain.Specification;

public class MessageSpecifications {

    public static Specification<Message> IdMoreThan(long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("messageId"), id);
    }

}

