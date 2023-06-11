package dsgp6.fakebook.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private String password;
    private String email;
    private ArrayList<String> hobbies= new ArrayList<>();
    private String phone_number;
    private LocalDate birthday;
    private int age;
    private String address;
    private String gender;
    private int numberOfFriends;
    private ArrayList<String> jobs=new ArrayList<>();
    private String token;

    private ArrayList<String> friends= new ArrayList<>();


    private boolean online;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }



    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(String friend) {
        friends.add(friend);
        numberOfFriends++;
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
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
        return numberOfFriends;
    }

    public void setNumberOfFriends(int numberOfFriends) {
        this.numberOfFriends = numberOfFriends;
    }

    public ArrayList<String> getJobs() {
        return jobs;
    }

    public void setJobs(String job) {
        jobs.add(job);
    }

    public User() {
    }
}
