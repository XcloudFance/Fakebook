package dsgp6.fakebook.web.controller;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.service.FriendService;
import dsgp6.fakebook.service.UserService;
import dsgp6.fakebook.web.forms.RegisterForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    //Currently assuming frontend sends back a form payload
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm) {
        System.out.println(registerForm);
        User user = userService.registerUser(registerForm);
        if (user != null) {
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"Registration succeed!\", \"cookie\":\"" + "uid=" + user.getUid() + ";token=" + user.getToken() + "\"}", HttpStatus.OK);

        }
        else{
            return ResponseEntity.ok("{code:\"-1\", msg:\"Registration failed\"}");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam(value = "uidOrEmail") String uidOrEmail,
                                            @RequestParam("password") String password) {

        User user = userService.loginUser(uidOrEmail, password);

        if(user == null)
            return ResponseEntity.ok("{\"code\":-1,\"msg\":\"Invalid login info!\"}");

        return new ResponseEntity<>("{\"code\":0,\"msg\":\"Login succeed!\", \"cookie\":\"" + "uid=" + user.getUid() + ";token=" + user.getToken() + "\"}", HttpStatus.OK);
    }

    @PostMapping("/set_profile")
    public ResponseEntity<String> setUserProfile(@RequestParam(value = "option") String option,
                                                 @RequestParam(value = "username", required = false) String username,
                                                 @RequestParam(value = "email", required = false) String email,
                                                 @RequestParam(value = "phone_number", required = false) String phone_number,
                                                 @RequestParam(value = "birthday", required = false) String birthday,
                                                 @RequestParam(value = "address", required = false) String address,
                                                 @RequestParam(value = "gender", required = false) String gender,
                                                 @RequestParam(value = "hobbies", required = false) String hobbies,
                                                 @RequestParam(value = "jobs", required = false) ArrayList<String> jobs,
                                                 @CookieValue(name = "token") String requestToken,
                                                 @CookieValue(name = "uid") String uid){

        boolean setProfileSuccess = userService.setUserProfile(uid, option, username, email, phone_number, birthday, address, gender, hobbies, jobs, requestToken);
        if (setProfileSuccess) {
            return ResponseEntity.ok("User profile setup successful.");
        } else {
            return ResponseEntity.badRequest().body("User profile setup failed.");
        }
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
}
