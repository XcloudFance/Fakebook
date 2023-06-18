package dsgp6.fakebook.web.controller;

import dsgp6.fakebook.Utils.Auxiliary;
import dsgp6.fakebook.Utils.FuzzySearch;
import dsgp6.fakebook.model.Post;
import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.PRepository;
import dsgp6.fakebook.service.UserService;
import dsgp6.fakebook.web.forms.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

    private final UserService userService;
    private final PRepository pRepository;
    public UserController(UserService userService,PRepository pRepository) {
        this.userService = userService;
        this.pRepository = pRepository;
    }
    @GetMapping(value = "/avatar/{uid}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getAvatar(@PathVariable String uid) {
        try {
            String avatarPath = "avatar/" + uid + ".png";
            String basePath = System.getProperty("user.dir") + "/src/main/resources/static/";
            String filePath = basePath + avatarPath;

            File file = new File(filePath);

            if (file.exists()) {
                byte[] avatarBytes = Files.readAllBytes(file.toPath());
                return ResponseEntity.ok(avatarBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String defaultAvatarPath = "avatar/default.png";
            String basePath = System.getProperty("user.dir") + "/src/main/resources/static/";
            String filePath = basePath + defaultAvatarPath;

            File defaultAvatarFile = new File(filePath);
            byte[] defaultAvatarBytes = Files.readAllBytes(defaultAvatarFile.toPath());

            return ResponseEntity.ok(defaultAvatarBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/resource/{rid}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getResource(@PathVariable String rid) {
        try {
            String avatarPath = "uploads/" + rid + ".png";
            String basePath = System.getProperty("user.dir") + "/src/main/resources/static/";
            String filePath = basePath + avatarPath;

            File file = new File(filePath);

            if (file.exists()) {
                byte[] avatarBytes = Files.readAllBytes(file.toPath());
                return ResponseEntity.ok(avatarBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile file,
                                               @CookieValue(name = "uid") String uid,
                                               @CookieValue(name = "token") String token) {
        if (userService.checkIdentity(uid, token)) {
            try {
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body("File is empty");
                }

                String basePath = System.getProperty("user.dir") + "/src/main/resources/static/avatar/";
                String filePath = basePath + uid + ".png";

                Files.write(Paths.get(filePath), file.getBytes(), StandardOpenOption.CREATE);

                return ResponseEntity.ok("File uploaded successfully");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Authentication");
    }

    @PostMapping("/uploadImg")
    public ResponseEntity<String> uploadImg(@RequestParam("img") MultipartFile file,
                                               @CookieValue(name = "uid") String uid,
                                               @CookieValue(name = "token") String token) {
        if (userService.checkIdentity(uid, token)) {
            try {
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body("File is empty");
                }

                String basePath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                String rid = generatePostId();
                String filePath = basePath + rid + ".png";

                Files.write(Paths.get(filePath), file.getBytes(), StandardOpenOption.CREATE);

                return ResponseEntity.ok(rid);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Authentication");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm) {
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
    public ResponseEntity<String> getProfile(@RequestParam(name = "uid", required = false) String target_uid, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(target_uid != null) {
            String data = "{";
            if(userService.getByUid(target_uid) == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Uid Doesn't Exists!\"}", HttpStatus.OK);
            Map<String, String> profile =  userService.getProfile(target_uid);
            for (Map.Entry<String, String> entry : profile.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                data += "\"" + key + "\":\"" + value + "\",";
            }
            data = data.substring(0, data.length() - 1) + "}";
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"success!\",\"data\":" + data + "}", HttpStatus.OK);
        }
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


    @PostMapping("/set_profile")
    public ResponseEntity<String> setUserProfile(@RequestBody Map<String, String> requestBody,
                                                 @CookieValue(name = "token") String token,
                                                 @CookieValue(name = "uid") String uid) {
        if (userService.checkIdentity(uid, token)) {
            String username = requestBody.get("username");
            String phone_number = requestBody.get("phone");
            String birthday = requestBody.get("birthday");
            String address = requestBody.get("address");
            String gender = requestBody.get("gender");
            String hobbies = requestBody.get("hobbies");
            String jobs = requestBody.get("jobs");

            userService.setUserProfile(uid, username, phone_number, birthday, address, gender, hobbies, jobs);
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"Profile Modified!\"}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }


    @GetMapping("/applyFriend")
    public ResponseEntity<String> applyFriend(@RequestParam("friend_uid") String friend_uid,@CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            User friend = userService.getByUid(friend_uid);
            if(friend == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Target UID Doesn't Exist!\"}", HttpStatus.OK);
            if(userService.getFriends(uid).contains(friend_uid) || friend_uid.equals(uid))
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Already Friends!\"}", HttpStatus.OK);
            if(friend.getPendingReceivedRequest().contains(uid))
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Already Sent!\"}", HttpStatus.OK);
            friend.addPendingReceivedRequest(uid);
            User user = userService.getByUid(uid);
            user.addPendingSentRequest(friend_uid);
            userService.save(user);
            userService.save(friend);
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success!\"}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/getCommonFriends")
    public ResponseEntity<String> getCommonFriends(@RequestParam("uid") String target_uid, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            User target = userService.getByUid(target_uid);
            if(target == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Target UID Doesn't Exist!\"}", HttpStatus.OK);
            User me = userService.getByUid(uid);
            List<String> target_friends = target.getFriends();
            List<String> myFriends = me.getFriends();
            List<String> CommonFriends = new ArrayList<>();
            for(int i = 0;i < myFriends.size();i ++)
                if(target_friends.contains(myFriends.get(i)) && !myFriends.get(i).equals(uid) && !myFriends.get(i).equals(target_uid)) {
                    CommonFriends.add(myFriends.get(i));
                }
            String data = "[";
            if(CommonFriends.size() == 0)
                return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success.\",\"data\":[]}", HttpStatus.OK);
            for(int i = 0;i < CommonFriends.size();i ++) {
                String friend_uid = CommonFriends.get(i);
                String username = userService.getByUid(friend_uid).getUsername();
                data += "{\"uid\":\"" + friend_uid + "\",\"uname\":\"" + username + "\"},";
            }
            data = data.substring(0, data.length() - 1);
            data+="]";
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"success\",\"data\":" + data + "}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/deleteFriend")
    public ResponseEntity<String> deleteFriend(@RequestParam("uid") String friend_uid, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            User friend = userService.getByUid(friend_uid);
            if(friend == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Target UID Doesn't Exist!\"}", HttpStatus.OK);
            if(!userService.getFriends(uid).contains(friend_uid) || friend_uid.equals(uid))
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Not Friends!\"}", HttpStatus.OK);
            friend.getFriends().remove(uid);
            User user = userService.getByUid(uid);
            user.getFriends().remove(friend_uid);
            userService.save(user);
            userService.save(friend);
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success!\"}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/acceptFriend")
    public ResponseEntity<String> acceptFriend(@RequestParam("friend_uid") String friend_uid,@CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            User friend = userService.getByUid(friend_uid);
            if(friend == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Target UID Doesn't Exist!\"}", HttpStatus.OK);
            if(friend.getPendingSentRequest().contains(uid)) {
                friend.acceptPendingSentRequest(uid);
                User user = userService.getByUid(uid);
                user.acceptPendingReceivedRequest(friend_uid);
                userService.save(user);
                userService.save(friend);
                return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success!\"}", HttpStatus.OK);
            }
            return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Target Not in Applying List!\"}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/getFriendsApplicationsReceived")
    public ResponseEntity<String> getReceivedApplications(@CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            User user = userService.getByUid(uid);
            String data = "[";
            List<String> list = user.getPendingReceivedRequest();
            if(list.size() == 0)
                return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success.\",\"data\":[]}", HttpStatus.OK);
            for(int i = 0;i < list.size();i ++) {
                String apply_uid = list.get(i);
                String username = userService.getByUid(apply_uid).getUsername();
                data += "{\"uid\":\"" + apply_uid + "\",\"uname\":\"" + username + "\"},";
            }
            data = data.substring(0, data.length() - 1);
            data+="]";
            return new ResponseEntity<>("{\"code\":-1,\"msg\":\"success\",\"data\":" + data + "}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/debug")
    public ResponseEntity<String> Debug(@CookieValue(name = "uid") String uid) {
        User user = userService.getByUid(uid);
        user.init();
        userService.save(user);
        List<Post> posts = pRepository.findAll();
        for(int i = 0;i < posts.size();i ++) {
            pRepository.delete(posts.get(i));
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"success\"}", HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> Test() {
        List<Post> posts = pRepository.findAll();
        String result = "";
        for(int i = 0;i < posts.size();i ++) {
            result += posts.get(i).getId().toString() + ",";
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getFriendsApplicationsSent")
    public ResponseEntity<String> getSentApplications(@CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            User user = userService.getByUid(uid);
            String data = "[";
            List<String> list = user.getPendingSentRequest();
            if(list.size() == 0)
                return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success.\",\"data\":[]}", HttpStatus.OK);
            for(int i = 0;i < list.size();i ++) {
                String apply_uid = list.get(i);
                String username = userService.getByUid(apply_uid).getUsername();
                data += "{\"uid\":\"" + apply_uid + "\",\"uname\":\"" + username + "\"},";
            }
            data = data.substring(0, data.length() - 1);
            data+="]";
            return new ResponseEntity<>("{\"code\":-1,\"msg\":\"success\",\"data\":" + data + "}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/refuseFriend")
    public ResponseEntity<String> refuseFriend(@RequestParam("friend_uid") String friend_uid,@CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            User friend = userService.getByUid(friend_uid);
            if(friend == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Target UID Doesn't Exist!\"}", HttpStatus.OK);
            if(friend.getPendingSentRequest().contains(uid)) {
                friend.refusePendingSentRequest(uid);
                User user = userService.getByUid(uid);
                user.refusePendingReceivedRequest(friend_uid);
                userService.save(friend);
                userService.save(user);
                return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success.\",\"data\":[]}", HttpStatus.OK);
            }
            return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Target Not in Applying List!\"}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/getFriends")
    public ResponseEntity<String> getFriends(@CookieValue("uid") String uid, @CookieValue("token") String token) {
        if(userService.checkIdentity(uid, token)) {
            String uids = "[";
            String unames = "[";
            List<String> friends = userService.getFriends(uid);
            if(friends.size() == 0) {
                uids = "[]";
                unames = "[]";
            }
            for(int i = 0;i < friends.size();i ++) {
                uids += "\"" + friends.get(i) + "\",";
                unames += "\"" + userService.getByUid(friends.get(i)).getUsername() + "\",";
            }
            uids = uids.substring(0, uids.length() - 1) + "]";
            unames = unames.substring(0, unames.length() - 1) + "]";

            String apply_uids = "[";
            String apply_unames = "[";
            List<String> apply_friends = userService.getByUid(uid).getPendingReceivedRequest();
            if(apply_friends.size() == 0) {
                apply_uids ="[]";
                apply_unames = "[]";
            }
            for(int i = 0;i < apply_friends.size();i ++) {
                apply_uids += "\"" + apply_friends.get(i) + "\",";
                apply_unames += "\"" + userService.getByUid(apply_friends.get(i)).getUsername() + "\",";
            }
            apply_uids = apply_uids.substring(0, apply_uids.length() - 1) + "]";
            apply_unames = apply_unames.substring(0, apply_unames.length() - 1) + "]";

            return new ResponseEntity<>("{\"code\":0,\"msg\":\"Success!\", \"data\":{\"uids\":" + uids + ",\"unames\":" + unames + "},\"applications\":{\"uids\":" + apply_uids + ",\"unames\":" + apply_unames + "}}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    public void sortPostsByTimeDesc(List<String> posts) {
        Collections.sort(posts, new Comparator<String>() {
            @Override
            public int compare(String postId1, String postId2) {
                LocalDateTime time1 = pRepository.getById(postId1).getPostTime();
                LocalDateTime time2 = pRepository.getById(postId2).getPostTime();
                return time1.compareTo(time2);
            }
        });
    }

    @GetMapping("/searchPost")
    public ResponseEntity<String> searchPost(@RequestParam String keyword, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if (userService.checkIdentity(uid, token)) {
            List<Post> Posts = pRepository.findAll();
            List<String> posts = new ArrayList<>();

            for(int i = 0;i < Posts.size();i ++) {
                if(FuzzySearch.contains(Posts.get(i).getContent(), keyword))
                    posts.add(Posts.get(i).getId());
            }

            String data = "[";

            for (String postId : posts) {
                Post post = this.pRepository.getById(postId);
                post.setViews(post.getViews() + 1);
                pRepository.save(post);
                String content = post.getContent().replaceAll("(?<!\\\\)\"", "\\\\\"");
                int likes = post.getLikes();
                int views = post.getViews();
                int comments = post.getCommentsNumber();
                String post_uid = post.getUid();
                String post_useranme = userService.getByUid(post.getUid()).getUsername();
                int forwards = post.getForwards();
                LocalDateTime postTime = post.getPostTime();
                String posttime = Auxiliary.formatDateTime(postTime);

                data += "{\"postId\":\"" + postId + "\",\"uid\":\"" + post_uid + "\",\"post_username\":\"" + post_useranme + "\",\"forwards\":\"" + forwards + "\",\"posttime\":\"" + posttime + "\",\"content\":\"" + content + "\",\"likes\":" + likes + ",\"views\":" + views + ",\"comments\":" + comments + "},";
            }

            if (!posts.isEmpty()) {
                data = data.substring(0, data.length() - 1);
            }

            data += "]";
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"success!\",\"data\":" + data + "}", HttpStatus.OK);
        }

        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/getPosts")
    public ResponseEntity<String> getPosts(@RequestParam("uid") String target_uid, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if (userService.checkIdentity(uid, token)) {
            User user = userService.getByUid(target_uid);
            if(user == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Target UID Doesn't Exist!\"}", HttpStatus.OK);
            List<String> posts = user.getPosts();
            String data = "[";

            for (String postId : posts) {
                Post post = this.pRepository.getById(postId);
                post.setViews(post.getViews() + 1);
                pRepository.save(post);
                String content = post.getContent().replaceAll("(?<!\\\\)\"", "\\\\\"");
                int likes = post.getLikes();
                int views = post.getViews();
                int comments = post.getCommentsNumber();
                String post_uid = post.getUid();
                String post_useranme = userService.getByUid(post.getUid()).getUsername();
                int forwards = post.getForwards();
                LocalDateTime postTime = post.getPostTime();
                String posttime = Auxiliary.formatDateTime(postTime);

                data += "{\"postId\":\"" + postId + "\",\"uid\":\"" + post_uid + "\",\"post_username\":\"" + post_useranme + "\",\"forwards\":\"" + forwards + "\",\"posttime\":\"" + posttime + "\",\"content\":\"" + content + "\",\"likes\":" + likes + ",\"views\":" + views + ",\"comments\":" + comments + "},";
            }

            if (!posts.isEmpty()) {
                data = data.substring(0, data.length() - 1);
            }

            data += "]";
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"success!\",\"data\":" + data + "}", HttpStatus.OK);
        }

        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/getPostList")
    public ResponseEntity<String> viewPost(@CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if (userService.checkIdentity(uid, token)) {
            List<String> friendIds = userService.getFriends(uid);
            friendIds.add(uid);
            List<String> posts = new ArrayList<>();

            for (String friendId : friendIds) {
                User friend = userService.getByUid(friendId);
                if (friend != null) {
                    List<String> friendPosts = friend.getPosts();
                    posts.addAll(friendPosts);
                }
            }

            sortPostsByTimeDesc(posts);

            String data = "[";

            for (String postId : posts) {
                Post post = this.pRepository.getById(postId);
                post.setViews(post.getViews() + 1);
                pRepository.save(post);
                String content = post.getContent().replaceAll("(?<!\\\\)\"", "\\\\\"");
                int likes = post.getLikes();
                int views = post.getViews();
                int comments = post.getCommentsNumber();
                String post_uid = post.getUid();
                String post_useranme = userService.getByUid(post.getUid()).getUsername();
                int forwards = post.getForwards();
                LocalDateTime postTime = post.getPostTime();
                String posttime = Auxiliary.formatDateTime(postTime);

                data += "{\"postId\":\"" + postId + "\",\"uid\":\"" + post_uid + "\",\"post_username\":\"" + post_useranme + "\",\"forwards\":\"" + forwards + "\",\"posttime\":\"" + posttime + "\",\"content\":\"" + content + "\",\"likes\":" + likes + ",\"views\":" + views + ",\"comments\":" + comments + "},";
            }

            if (!posts.isEmpty()) {
                data = data.substring(0, data.length() - 1);
            }

            data += "]";
            return new ResponseEntity<>("{\"code\":0,\"msg\":\"success!\",\"data\":" + data + "}", HttpStatus.OK);
        }

        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }
    
    private String generatePostId() {
        // 实现你自己的逻辑来生成帖子的唯一ID
        return UUID.randomUUID().toString();
    }
    @PostMapping("/createPost")
    public ResponseEntity<String> createPost(@RequestBody String content, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if (userService.checkIdentity(uid, token)) {
            User user = userService.getByUid(uid);
            if (user != null) {

                Post newpost = new Post(content, uid);
                // 为新帖子生成ID
                String postId = generatePostId(); // 你可以根据自己的需求生成帖子的唯一ID

                // 设置帖子ID并保存到数据库
                newpost.setId(postId);
                
                this.pRepository.save(newpost);

                // 将帖子添加到用户的帖子列表
                user.createPost(postId);
                userService.save(user);

                return new ResponseEntity<>("{\"code\":0,\"msg\":\"Post created successfully!\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"User not found!\"}", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
        }
    }



    @GetMapping("/getPost")
    public ResponseEntity<String> getPost(@RequestParam String postID, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            Post post = pRepository.getById(postID);

            if(post == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"PostID doesn't Exists!\"}", HttpStatus.OK);
            String content = post.getContent().replaceAll("(?<!\\\\)\"", "\\\\\"");
            post.setViews(post.getViews() + 1);
            pRepository.save(post);
            int likes = post.getLikes();
            int views = post.getViews();
            int comments = post.getCommentsNumber();
            String post_uid = post.getUid();
            String post_useranme = userService.getByUid(post.getUid()).getUsername();
            int forwards = post.getForwards();
            LocalDateTime postTime = post.getPostTime();
            String posttime = Auxiliary.formatDateTime(postTime);

            String data = "{\"code\":0,\"postId\":\"" + post.getId() + "\",\"uid\":\"" + post_uid + "\",\"post_username\":\"" + post_useranme + "\",\"forwards\":\"" + forwards + "\",\"posttime\":\"" + posttime + "\",\"content\":\"" + content + "\",\"likes\":" + likes + ",\"views\":" + views + ",\"comments\":" + comments + "}";
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/forwardPost")
    public ResponseEntity<String> forwardPost(@RequestParam String postID, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
        if(userService.checkIdentity(uid, token)) {
            Post post = pRepository.getById(postID);
            if(post == null)
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"PostID doesn't Exists!\"}", HttpStatus.OK);
            post.increaseForwards();
            pRepository.save(post);
            return new ResponseEntity<>("{\"code\":0, \"msg\":\"success\"}", HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.OK);
    }

    @GetMapping("/deletePost/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable String postId, @CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) {
    if (userService.checkIdentity(uid, token)) {
        User user = userService.getByUid(uid);
        if (user != null) {
            // 检查帖子是否存在
            String post = user.getPostById(postId);
            if (post != null && userService.getByUid(uid).getPosts().contains(postId)) {
                user.deletePost(uid);
                // 从数据库中删除帖子
                this.pRepository.deleteById(postId);

                return new ResponseEntity<>("{\"code\":0,\"msg\":\"Post deleted successfully!\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Post not found!\"}", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("{\"code\":-1,\"msg\":\"User not found!\"}", HttpStatus.NOT_FOUND);
        }
    } else {
        return new ResponseEntity<>("{\"code\":-1,\"msg\":\"Authentication Failed!\"}", HttpStatus.UNAUTHORIZED);
    }
}

}
