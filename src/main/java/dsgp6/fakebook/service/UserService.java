package dsgp6.fakebook.service;

import dsgp6.fakebook.Utils.Auxiliary;
import dsgp6.fakebook.Utils.Random;
import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.UserRepository;
import dsgp6.fakebook.web.forms.RegisterForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkIdentity(String uid, String token) {
        User user = userRepository.getByUid(uid);
        if(user == null) return false;
        if(user.getToken().equals(token)) return true;
        return false;
    }

    public Map<String, String> getProfile(String uid) {
        Map<String, String> profile = new HashMap<>();
        User user = userRepository.getByUid(uid);
        profile.put("email", user.getEmail());
        profile.put("uid", user.getUid());
        profile.put("username", user.getUsername());
        profile.put("phone", user.getPhone_number());
        profile.put("address", user.getAddress());
        return profile;
    }

    public User registerUser(RegisterForm registerForm) {
        //Check if entered password is valid or not
        if (!isPasswordValid(registerForm.getPassword()))
            return null;
        //Check if entered uid is valid or not
        if (!isUidValid(registerForm.getUid()))
            return null;
        //Check if entered username is valid or not
        if (!isUsernameValid(registerForm.getUsername()))
            return null;
        //Check if entered email is valid or not
        if (!Auxiliary.isEmailValid(registerForm.getEmail()))
            return null;

        // Create a new User object from the registration form data
        User user = new User();
        user.setUid(registerForm.getUid());
        user.setUsername(registerForm.getUsername());
        user.setPassword(registerForm.getPassword());
        user.setEmail(registerForm.getEmail());
        user.setPhone_number(registerForm.getPhone_number());

        userRepository.save(user);
        return user;
    }

    public User loginUser(String uidOrEmail, String password) {
        User user;
        if(Auxiliary.isEmailValid(uidOrEmail))
            user = userRepository.getByEmail(uidOrEmail);
        else
            user = userRepository.getByUid(uidOrEmail);
        //Checks if user exists
        if (user == null) {
            return null;
        }
        //Checks if password is correct
        if (!password.equals(user.getPassword())) {
            return null;
        }
        //Set a token to the user
        user.setToken(Random.generateToken(16));
        userRepository.save(user);
        return user;
    }

    public boolean setUserProfile(String uid, String option, String username, String email, String phone_number, String birthday, String address, String gender, String hobbies, ArrayList<String> jobs, String requestToken) {
        User user = userRepository.getByUid(uid);
        if (user == null) {
            return false;
        }
        if (!requestToken.equals(user.getToken())) {
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

    public User searchFriend(String friendId, String uid) {
        User user = userRepository.getByUid(uid);
        User friend = null;
        ArrayList<String> friends = user.getFriends();

        for (String f : friends) {
            if(f.equals(friendId)){
                friend = userRepository.getByUid(f);
                break;
            }
        }

        return friend;
    }

    //Checks uid is not longer than 15, its availability and consists only of alphabets and numbers
    public boolean isUidValid(String uid) {
        if (uid == null || uid.equals("")) {
            return false;
        }
        if (uid.length() > 15) {
            return false;
        }
        if(userRepository.getByUid(uid) != null) {
            return false;
        }
        return uid.matches("^[A-Za-z0-9]*$");
    }

    //Checks username is within 5-20 characters and its availability
    public boolean isUsernameValid(String username) {
        if (username.length() > 20 || username.length() < 5) {
            return false;
        }
        return true;
    }

    public boolean isPasswordValid(String password) {
        if (password.length() < 6 || password.length() > 14) {
            return false;
        }
        return password.matches("^[A-Za-z0-9]*$");
    }
}
