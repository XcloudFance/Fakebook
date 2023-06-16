package dsgp6.fakebook.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import dsgp6.fakebook.model.Post;

public interface PRepository extends MongoRepository<Post, String> {

    Post getById(String Id) ;
}
