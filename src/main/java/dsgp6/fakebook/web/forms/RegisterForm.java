
package dsgp6.fakebook.web.forms;
public class RegisterForm {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String passwordCheck;

    // Constructors, getters, and setters

    public RegisterForm() {
    }

    public RegisterForm(String username, String password, String email, String phone, String passwordCheck) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.passwordCheck = passwordCheck;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }
    

}
