package dsgp6.fakebook.web.configuration.filter;

import dsgp6.fakebook.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public AuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // No token authentication needed for register and login
        if (request.getRequestURI().contains("/api/user/register") || request.getRequestURI().contains("/api/user/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if a token is present in the request's cookies
        String token = extractTokenFromCookies(request);
        if (token != null && isTokenValid(token)) {
            // Token is valid, endpoint access allowed
            System.out.println("I'm in auth filter before doFilter");
            filterChain.doFilter(request, response);
            System.out.println("Auth filter is performed");
        } else {
            // If token is invalid, return 401 Unauthorized response
            System.out.println("Token doesn't match with database");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
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
        return userRepository.existsByToken(token);
    }
}
