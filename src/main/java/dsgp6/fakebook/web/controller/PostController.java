package dsgp6.fakebook.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dsgp6.fakebook.model.Post;
import dsgp6.fakebook.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }



    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable String id) {
        postService.likePost(id);
        return ResponseEntity.ok("Post liked successfully.");
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<String> viewPost(@PathVariable String id) {
        postService.viewPost(id);
        return ResponseEntity.ok("Post viewed successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id, @CookieValue("userid") String userId) {
        // This is for admin used
        if (userId.equals("user123")) {
            postService.deletePost(id);
            return ResponseEntity.ok("Post deleted successfully.");
        } else {
            return ResponseEntity.status(403).body("Access denied.");
        }
    }
}
