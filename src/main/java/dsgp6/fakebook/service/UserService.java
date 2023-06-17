package dsgp6.fakebook.service;

import dsgp6.fakebook.Utils.Auxiliary;
import dsgp6.fakebook.Utils.Random;
import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.UserRepository;
import dsgp6.fakebook.web.forms.RegisterForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        profile.put("hobbies", user.getHobbies());
        profile.put("jobs", user.getJobs());
        profile.put("gender", user.getGender());
        profile.put("birthday", user.getBirthday().toString());
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
        user.setToken(Random.generateToken(16));

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

    public void setUserProfile(String uid, String username, String phone_number, String birthday, String address, String gender, String hobbies, String jobs) {
        User user = userRepository.getByUid(uid);
        if (username != null) {
            user.setUsername(username);
        }
        if (phone_number != null) {
            user.setPhone_number(phone_number);
        }
        if (birthday != null) {
            user.setBirthday(birthday);
        }
        if (address != null) {
            user.setAddress(address);
        }
        if (gender != null) {
            user.setGender(gender);
        }
        if (hobbies != null) {
            user.setHobbies(hobbies);
        }
        if (jobs != null) {
            user.setJobs(jobs);
        }
        userRepository.save(user);
        return;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User[] searchFriend(String keyword) {
        List<User> matchingUsers = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            if (user.getUid().contains(keyword)) {
                matchingUsers.add(user);
            }
        }

        User[] result = new User[matchingUsers.size()];
        return matchingUsers.toArray(result);
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

    public List<String> getFriends(String uid) {
        User user = userRepository.getByUid(uid);

        return user.getFriends();
    }

    public User getByUid(String friendId) {
        return userRepository.getByUid(friendId);
    }



  
}
