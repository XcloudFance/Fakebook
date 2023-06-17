package dsgp6.fakebook.service;
import dsgp6.fakebook.model.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dsgp6.fakebook.repository.PRepository;

@Service
public class PostService {
    private final PRepository PRepository;

    @Autowired
    public PostService(PRepository PRepository) {
        this.PRepository = PRepository;
    }

    public Post createPost(String content, String uid) {
        Post post = new Post(content, uid);
        return PRepository.save(post);
    }

    public Post getPostById(String id) {
        return PRepository.findById(id).orElse(null);
    }

    public void likePost(String id) {
        Post post = PRepository.findById(id).orElse(null);
        if (post != null) {
            post.incrementLikes();
            PRepository.save(post);
        }
    }

    public void viewPost(String id) {
        Post post = PRepository.findById(id).orElse(null);
        if (post != null) {
            post.incrementViews();
            PRepository.save(post);
        }
    }

    public void deletePost(String id) {
        PRepository.deleteById(id);
    }
}
