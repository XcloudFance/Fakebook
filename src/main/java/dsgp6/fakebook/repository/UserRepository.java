
package dsgp6.fakebook.repository;
import dsgp6.fakebook.model.User;
import org.springframework.data.mongodb.repository.*;

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    User findByEmailOrPhone(String email, String phone);
}
