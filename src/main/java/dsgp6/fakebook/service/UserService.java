package dsgp6.fakebook.service;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.UserRepository;
import dsgp6.fakebook.web.forms.RegisterForm;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;

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
        user.setPassword(registerForm.getPassword());
        user.setEmail(registerForm.getEmail());
        user.setPhone_number(registerForm.getPhone_number());

        userRepository.save(user);
        return true;
    }

    public User loginUser(String uidOrEmail, String password) {
        User user = userRepository.findByUidOrEmail(uidOrEmail, uidOrEmail);
        //Checks if user exists
        if (user == null) {
            return null;
        }
        //Checks if password is correct
        if (!password.equals(user.getPassword())) {
            return null;
        }
        //Set a token to the user
        user.setToken(generateToken());
        userRepository.save(user);
        return user;
    }

    public boolean setUserProfile(String uid, String token, String option, String username, String email, String phone_number, String birthday, String address, String gender, ArrayList<String> hobbies, ArrayList<String> jobs) {
        User user = userRepository.findByUid(uid);
        if (user == null) {
            return false;
        }
        if(!token.equals(user.getToken())){
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
    //Calculate age of user based on birthday
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
    
    public String generateToken(){ 
        int tokenLength = 15;
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder token = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < tokenLength; i++) {
            int randomIndex = r.nextInt(characters.length());
            token.append(characters.charAt(randomIndex));
        }
        return token.toString();
    }

    public User getUserByUid(String uid) {
        return userRepository.findByUid(uid);
    }
    
    public boolean validateToken(String token){
        User user = userRepository.findByToken(token);
        return user != null;
    }

}
