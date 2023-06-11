package dsgp6.fakebook.repository;

import dsgp6.fakebook.model.User;
import java.util.List;
import org.springframework.data.mongodb.repository.*;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUid(String uid);

    boolean existsByToken(String token);

    User findByUsername(String username);

    User findByUid(String uid);

    User findByEmail(String email);

    User findByUidOrEmail(String uid, String email);

    User findByToken(String token);

}
