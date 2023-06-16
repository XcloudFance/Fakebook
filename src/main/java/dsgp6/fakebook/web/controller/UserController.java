package dsgp6.fakebook.web.controller;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.service.UserService;
import dsgp6.fakebook.web.forms.RegisterForm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/avatar/{uid}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getAvatar(@PathVariable String uid) {
        try {
            String avatarPath = "/static/avatar/" + uid + ".png";

            Resource resource = new ClassPathResource(avatarPath);

            if (resource.exists()) {
                return ResponseEntity.ok(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Resource defaultAvatar = new ClassPathResource("static/avatar/default.png");
            return ResponseEntity.ok(defaultAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
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

    @GetMapping("/searchUser")
    public ResponseEntity<String> searchUser(@RequestParam(value = "keyword") String keyword) {
        User[] results = userService.searchFriend(keyword);
        String uids = "[";
        String unames = "[";
        if (results.length == 0) {
            uids = "[]";
            unames = "[]";
        }
        for(int i = 0;i < results.length;i ++)
            if(i < results.length - 1) {
                uids += "\"" + results[i].getUid() + "\",";
                unames += "\"" + results[i].getUsername() + "\",";
            }
            else {
                uids += "\"" + results[i].getUid() + "\"]";
                unames += "\"" + results[i].getUsername() + "\"]";
            }
        return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success!\", \"data\":{\"uids\":" + uids +",\"unames\":" + unames +"}}", HttpStatus.OK);
    }

    @GetMapping("/get_profile")
    public ResponseEntity<String> getProfile(@CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            String data = "{";
            Map<String, String> profile =  userService.getProfile(uid);
            for (Map.Entry<String, String> entry : profile.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                data += "\"" + key + "\":\"" + value + "\",";
            }
            data = data.substring(0, data.length() - 1) + "}";
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"success!\",\"data\":" + data + "}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile file, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            try {
                // 检查文件是否为空
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body("File is empty");
                }

                // 获取文件字节数组
                byte[] bytes = file.getBytes();

                // 定义文件保存路径
                String filePath = "src/main/resources/static/avatar/" + uid + ".png";

                // 写入文件
                Files.write(Path.of(filePath), bytes, StandardOpenOption.CREATE);

                return ResponseEntity.ok("File uploaded successfully");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Authentication");
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
}
