package dsgp6.fakebook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class ViewUser extends User {
    @Override @JsonIgnore
    public String getEmail() {
        return email;
    }
    @Override @JsonIgnore
    public String getToken() {
        return token;
    }
    @Override @JsonIgnore
    public String getPhone_number() {
        return phone_number;
    }
    @Override @JsonIgnore
    public String getPassword() {
        return password;
    }
    @Override @JsonIgnore
    public String getAddress() {
        return address;
    }
    @Override @JsonIgnore
    public List<String> getPendingSentRequest() {
        return PendingSentRequest;
    }
    @Override @JsonIgnore
    public List<String> getPendingReceivedRequest() {
        return PendingReceivedRequest;
    }
    @Override @JsonIgnore
    public String getId() {
        return id;
    }
}
