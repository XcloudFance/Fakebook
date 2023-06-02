package dsgp6.fakebook.web.controller;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.service.UserService;
import dsgp6.fakebook.web.forms.RegisterForm;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Currently assuming frontend sends back a form payload
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm) {
        boolean registrationSuccess = userService.registerUser(registerForm);
        if (registrationSuccess) {
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.badRequest().body("Registration failed");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam(value = "uidOrEmail") String uidOrEmail,
            @RequestParam("password") String password) {
        
        User user = userService.loginUser(uidOrEmail, password);
        
        // Set the token as a cookie in the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "token=" + user.getToken());
        return new ResponseEntity<>("Login successful", headers, HttpStatus.OK);
        
    }
    
    @PostMapping("/set_profile")
    public ResponseEntity<String> setUserProfile(@RequestParam("uid") String uid,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "option") String option,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone_number", required = false) String phone_number,
            @RequestParam(value = "birthday", required = false) String birthday,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "hobbies", required = false) ArrayList<String> hobbies,
            @RequestParam(value = "jobs", required = false) ArrayList<String> jobs) {

        boolean setProfileSuccess = userService.setUserProfile(uid, token, option, username, email, phone_number, birthday, address, gender, hobbies, jobs);
        if (setProfileSuccess) {
            return ResponseEntity.ok("User profile setup successful.");
        } else {
            return ResponseEntity.badRequest().body("User profile setup failed.");
        }
    }
    //Dummy method to test for authentication
    @GetMapping("/dummy")
    public String dummy(){
        return "This is a dummy endpoint. It tests for authentication interceptor";
    }
    
    
}
