package dsgp6.fakebook.service;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.UserRepository;
import dsgp6.fakebook.web.forms.LoginForm;
import dsgp6.fakebook.web.forms.RegisterForm;
import dsgp6.fakebook.web.forms.SetProfileForm;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(RegisterForm registerForm) {
        //Check if entered password is valid or not
        if (!isPasswordValid(registerForm.getPassword())) {
            return false;
        }
        //Check if entered uid is valid or not
        if (!isUidValid(registerForm.getUid())) {
            return false;
        }
        //Check if entered username is valid or not
        if (!isUsernameValid(registerForm.getUsername())) {
            return false;
        }
        //Check if entered email is valid or not
        if (!isEmailValid(registerForm.getEmail())) {
            return false;
        }

        // Create a new User object from the registration form data
        User user = new User();
        user.setUid(registerForm.getUid());
        user.setUsername(registerForm.getUsername());

        //Encode the password using BCrypt Encoder
//        String encodedPassword = passwordEncoder.encode(registerForm.getPassword());
        user.setPassword(registerForm.getPassword());

        user.setEmail(registerForm.getEmail());
        user.setPhone_number(registerForm.getPhone_number());

        userRepository.save(user);
        return true;
    }

    public User loginUser(String uid, String username, String email, String password) {
        User user = null;
        if (uid != null) {
            user = userRepository.findByUid(uid);
        } else if (username != null) {
            user = userRepository.findByUsername(username);
        } else if (email != null) {
            user = userRepository.findByEmail(email);
        }

        if (user == null) {
            return null;
        }

//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            return null;
//        }
        return user;
    }

    public boolean setUserProfile(String uid, String token, String option, String username, String email, String phone_number, String birthday, String address, String gender, ArrayList<String> hobbies, ArrayList<String> jobs) {
        User user = userRepository.findByUid(uid);
        if (user == null) {
            return false;
        }
        if (option.equals("username")) {
            user.setUsername(username);
        }
        if (option.equals("phone_number")) {
            user.setPhone_number(phone_number);
        }
        if (option.equals("birthday")) {
            user.setBirthday(birthday);
            user.setAge(calcAge(user.getBirthday()));
        }
        if (option.equals("address")) {
            user.setAddress(address);
        }
        if (option.equals("gender")) {
            user.setGender(gender);
        }
        if (option.equals("hobbies")) {
            user.setHobbies(hobbies);
        }
        if (option.equals("jobs")) {
            user.setJobs(jobs);
        }
        if (option.equals("email")) {
            user.setEmail(email);
        }
        userRepository.save(user);
        return true;

    }

    public int calcAge(LocalDate birthday) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthday, currentDate);
        return period.getYears();
    }

    //Checks uid is not longer than 15, its availability and consists only of alphabets and numbers
    public boolean isUidValid(String uid) {
        if (uid == null || uid.equals("")) {
            return false;
        }
        if (uid.length() > 15) {
            return false;
        }
        if (userRepository.existsByUid(uid)) {
            return false;
        }
        return uid.matches("^[A-Za-z0-9]*$");
    }

    //Checks username is within 5-20 characters and its availability
    public boolean isUsernameValid(String username) {
        if (username.length() > 20 || username.length() < 5) {
            return false;
        }
        return !userRepository.existsByUsername(username);
    }

    //Checks whether password length is within 6-15 characters, and it only contains alphabet and numbers
    public boolean isPasswordValid(String password) {
        if (password.length() < 6 || password.length() > 14) {
            return false;
        }
        return password.matches("^[A-Za-z0-9]*$");
    }

    //Checks email's availability and format
    public boolean isEmailValid(String email) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }
        return email.matches("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}");
    }
//    
//    public String generateToken(){ }
//    

}
//add generate token function 16-20 length 0-9 a-z A-Z, user add token field, login will save and return a token, set profile will accept a token field and verify the token with saved token
