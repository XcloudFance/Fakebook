package dsgp6.fakebook.web.forms;

public class RegisterForm {

    private String uid;
    private String username;
    private String password;
    private String email;
    private String phone_number;

    // Constructors, getters, and setters
    public RegisterForm() {
    }

    public RegisterForm(String uid, String username, String password, String email, String phone_number) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

  

}
