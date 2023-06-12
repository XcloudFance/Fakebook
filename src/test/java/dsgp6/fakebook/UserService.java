package dsgp6.fakebook;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username exist");
        }

        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("Username or password wrong");
        }
        return user;
    }

    // public User updatePrivacySettings(String uid, PrivacySettings privacySettings) {
    //     User user = userRepository.findByUid(uid);
    //     if (user != null) {
    //         user.setPrivacySettings(privacySettings);
    //         return userRepository.save(user);
    //     }
    //     throw new RuntimeException("User not found");
    // }

    public User getUserByUid(String uid) { return userRepository.findByUid(uid); }
}
