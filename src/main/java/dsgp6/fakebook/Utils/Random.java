package dsgp6.fakebook.Utils;

public class Random {
    public static String generateToken(int length) {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder token = new StringBuilder();
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = r.nextInt(characters.length());
            token.append(characters.charAt(randomIndex));
        }
        String tokenString = token.toString();
        return tokenString;
    }
}
