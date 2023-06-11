package dsgp6.fakebook.web.controller;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.service.FriendService;
import dsgp6.fakebook.service.UserService;
import dsgp6.fakebook.web.forms.RegisterForm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private FriendService friendService;

    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    //Currently assuming frontend sends back a form payload
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm) {
        boolean registrationSuccess = userService.registerUser(registerForm);
        if (registrationSuccess) {
            return ResponseEntity.ok("Registration successful");
        } else if (!registrationSuccess){
            return ResponseEntity.badRequest().body("Registration failed");
        }
        else{
            return ResponseEntity.badRequest().body("Registration failed, but is else");
        }
    }

    @PostMapping("/login")
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
            @RequestParam(value = "option") String option,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone_number", required = false) String phone_number,
            @RequestParam(value = "birthday", required = false) String birthday,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "hobbies", required = false) String hobbies,
            @RequestParam(value = "jobs", required = false) String jobs,
            @RequestParam(value = "friends", required = false) String friends,
            @CookieValue(name = "token") String requestToken) {

        boolean setProfileSuccess = userService.setUserProfile(uid, option, username, email, phone_number, birthday, address, gender, hobbies, jobs, friends, requestToken);
        if (setProfileSuccess) {
            return ResponseEntity.ok("User profile setup successful.");
        } else {
            return ResponseEntity.badRequest().body("User profile setup failed.");
        }
    }


    @GetMapping("/get_profile")
    public ResponseEntity<?> viewAccount(@RequestParam("uid") String uid, @CookieValue(name = "token") String token) {
        return userService.viewAccount(uid, token);
    }



    //Dummy method to test for authentication
    @GetMapping("/dummy")
    public String dummy() {
        return "This is a dummy endpoint. ";
    }

    @PostMapping("/getCookieValue")
    public String getCookieValue(@RequestParam("uid") String uid,
            @RequestParam("username") String username,
            @CookieValue(name = "token") String token){
        return "This token in cookie is: "+token+uid+username;
    }

    @PostMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/search/friend")
    public ResponseEntity<User> searchFriend(@RequestParam("friend") String friendId,
            @CookieValue(name = "token") String token) {
        User searchResult = userService.searchFriend(friendId, token);

        if (searchResult == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(searchResult);
        }
    }
    //friend-related endpoint
    @GetMapping("/{userUid}/add_friend")
    public ResponseEntity<?> addFriend(@PathVariable("userUid") String userUid, @RequestParam("friend_uid") String friendUid) {
        boolean addFriendSuccess = friendService.addFriend(userUid, friendUid);
        if (addFriendSuccess) {return ResponseEntity.ok("Friend added successfully.");}
        else {return ResponseEntity.badRequest().body("Failed to add friend.");}
    }
    @GetMapping("/{userUid}/remove_friend")
    public ResponseEntity<?> removeFriend(@PathVariable("userUid") String userUid, @RequestParam("friend_uid") String friendUid) {
        boolean removeFriendSuccess = friendService.removeFriend(userUid, friendUid);
        if (removeFriendSuccess) {return ResponseEntity.ok("Friend removed successfully.");}
        else {return ResponseEntity.badRequest().body("Failed to remove friend.");}
    }
    @GetMapping("/get_friendlist")
    public String[] getFriendList (@RequestParam("user_uid") String userUid) {
        return friendService.getFriendList(userUid);
    }
    @GetMapping("/{user1id}/mutual_friends/{user2id}")
    public ResponseEntity<?> findMutualFriends(@PathVariable("user1id") String user1id, @PathVariable("user2id") String user2id) {
        List<String> mutualFriends = friendService.findMutualFriends(user1id, user2id);
        if (mutualFriends.isEmpty()) {
            return ResponseEntity.ok("No mutual friends found.");
        } else {
            return ResponseEntity.ok(mutualFriends);
        }
    }

}
