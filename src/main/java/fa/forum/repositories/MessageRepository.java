package fa.forum.repositories;

import fa.forum.models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Integer> {
    List<MessageModel> findByContentFileNameIsNotNull();
    List<MessageModel> findAllByOrderBySendingTimeDesc();
}
