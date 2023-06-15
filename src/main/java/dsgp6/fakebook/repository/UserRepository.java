package dsgp6.fakebook.repository;

import dsgp6.fakebook.model.User;
import org.springframework.data.mongodb.repository.*;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User getByUsername(String username);

    User getByUid(String uid);

    User getByEmail(String email);
}
