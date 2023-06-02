package dsgp6.fakebook.web.interceptor;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    public AuthenticationInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // No authentication needed for /register and /login
        if (request.getRequestURI().equals("/api/user/register") || request.getRequestURI().equals("/api/user/login")) {
            return true;
        }
        // Check if a token is present in request's cookies
        String token = extractTokenFromCookies(request);
        if (token != null && isTokenValid(token)) {
            System.out.println("Token found: "+token);
            //Token is valid, endpoint access allowed
            return true;
        }
        //If token is invalid, 401 Unauthorized response is returned
        System.out.println("Token invalid");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        // Extract the token from the request's cookies
        Cookie[] cookies = request.getCookies();
        // Check if cookies are present
        if (cookies != null) {
            // Loop through cookies to find the token
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    System.out.println("Token found in cookies: " + token);
                    return token;
                }
            }
        }
        System.out.println("Token not found in cookies.");
        //Token not found or cookies is null
        return null;
    }

    private boolean isTokenValid(String token) {
        User user = userRepository.findByToken(token);
        return user != null;
    }
}
