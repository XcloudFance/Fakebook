package dsgp6.fakebook.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@CompoundIndex(def = "{'uid': 1}", unique = true)
public class User {
    @Id
    private String id;

    private String uid;
    private String username;
    private String salt;
    protected String password;
    protected String email;
    private String hobbies = "To be Edited...";
    protected String phone_number;
    private LocalDate birthday = LocalDate.from(LocalDateTime.now());
    protected String address = "To be Edited...";
    private String gender = "To be Edited...";
    private String jobs = "To be Edited...";
    protected String token;
    private ArrayList<String> friends= new ArrayList<>();
    private ArrayList<String> posts = new ArrayList<>();
    private boolean online;
    private ArrayList<String> PendingSentRequest = new ArrayList<>();
    private ArrayList<String> PendingReceivedRequest = new ArrayList<>();

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void addFriend(String uid) {
        friends.add(uid);
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobby) {
        hobbies = hobby;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.birthday = LocalDate.parse(birthday, formatter);
    }

    public int getAge() {
        LocalDate today = LocalDate.now();
        Period age = birthday.until(today);
        return age.getYears();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getNumberOfFriends() {
        return friends.size();
    }
    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    public User() {
    }

    public ArrayList<String> getPendingSentRequest() {
        return PendingSentRequest;
    }

    public void addPendingSentRequest(String newRequest) {
        PendingSentRequest.add(newRequest);
    }

    public void acceptPendingSentRequest(String uid) {
        friends.add(uid);
        PendingSentRequest.remove(uid);
    }
    public void refusePendingSentRequest(String uid) {
        PendingSentRequest.remove(uid);
    }

    public ArrayList<String> getPendingReceivedRequest() {
        return PendingReceivedRequest;
    }

    public void addPendingReceivedRequest(String newRequest) {
        PendingReceivedRequest.add(newRequest);
    }

    public void acceptPendingReceivedRequest(String uid) {
        friends.add(uid);
        PendingReceivedRequest.remove(uid);
    }
    public void refusePendingReceivedRequest(String uid) {
        getPendingReceivedRequest().remove(uid);
    }

    public List<String> getPosts() {
        return posts;
    }

    public void createPost(String postId) {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(postId);
    }
    public String getPostById(String postId) {
        for (String id : posts) {
            if(id == postId){
                return id;
            }
        }
        return null; // 如果未找到对应的帖子，则返回null
    }

    public void deletePost(String postId) {
        posts.remove(postId);
    }

    public void init() {
        posts.clear();
        friends.clear();
        PendingSentRequest.clear();
        PendingReceivedRequest.clear();
    }

}