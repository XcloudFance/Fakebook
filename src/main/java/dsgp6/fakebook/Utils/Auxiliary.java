package dsgp6.fakebook.Utils;

public class Auxiliary {
    public static boolean isEmailValid(String email) {
        return email.matches("[a-z0-9A-Z.]+@[a-z]+\\.[a-z]{2,3}");
    }
}
