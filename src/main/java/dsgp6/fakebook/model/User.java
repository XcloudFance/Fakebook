package dsgp6.fakebook.model;

import java.time.LocalDate;
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
    private ArrayList<String> hobbies= new ArrayList<>();
    protected String phone_number;
    private LocalDate birthday;
    protected String address;
    private String gender;
    private ArrayList<String> jobs=new ArrayList<>();
    protected String token;
    private ArrayList<String> friends= new ArrayList<>();

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
        friends.add(friend);
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

    public ArrayList<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobby) {
        hobbies.add(hobby);
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
    public ArrayList<String> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<String> jobs) {
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

    public void acceptPendingReceivedRequest(String uid, int opt) {
        friends.add(uid);
        PendingSentRequest.remove(uid);
    }
    public void refusePendingReceivedRequest(String uid, int opt) {
        PendingSentRequest.remove(uid);
    }
}