package dsgp6.fakebook.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Auxiliary {
    public static boolean isEmailValid(String email) {
        return email.matches("[a-z0-9A-Z.]+@[a-z]+\\.[a-z]{2,3}");
    }
    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a · MMM d, yyyy ", Locale.ENGLISH);
        return dateTime.format(formatter);
    }
}
