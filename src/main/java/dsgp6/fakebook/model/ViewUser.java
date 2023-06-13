package dsgp6.fakebook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
}