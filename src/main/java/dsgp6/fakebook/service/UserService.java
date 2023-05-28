
package dsgp6.fakebook.service;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.UserRepository;
import dsgp6.fakebook.web.forms.LoginForm;
import dsgp6.fakebook.web.forms.RegisterForm;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(RegisterForm registerForm) {
        if (!isPasswordSame(registerForm)) {
            return false;
        }
        // Check if the username or email already exists
        if (userRepository.existsByUsername(registerForm.getUsername()) || userRepository.existsByEmail(registerForm.getEmail())) {
            return false;
        }

        // Create a new User object from the registration form data
        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setPassword(registerForm.getPassword());
        user.setEmail(registerForm.getEmail());
        user.setPhone(registerForm.getPhone());
        userRepository.save(user);
        return true;
    }

    public User loginUser(LoginForm loginForm) {
        String emailOrPhone = loginForm.getEmailOrPhone();
        String password = loginForm.getPassword();

        //Search for user in db that has the same email or phone number
        User user = userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Password incorrect.");
        }
        return user;
    }

    public boolean isPasswordSame(RegisterForm registerForm) {
        return registerForm.getPassword().equals(registerForm.getPasswordCheck());
    }
}
