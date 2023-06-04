package dsgp6.fakebook.repository;
import dsgp6.fakebook.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
    void saveMessage(Message message);
}
