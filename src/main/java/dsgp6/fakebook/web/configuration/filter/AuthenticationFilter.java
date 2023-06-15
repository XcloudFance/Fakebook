package dsgp6.fakebook.web.configuration.filter;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public AuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // No token authentication needed for register and login
        if (request.getRequestURI().startsWith("/api/") || request.getRequestURI().startsWith("/login") || request.getRequestURI().startsWith("/css") || request.getRequestURI().startsWith("/js") || request.getRequestURI().startsWith("/img") || request.getRequestURI().startsWith("/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if a token is present in the request's cookies
        String token = extractTokenFromCookies(request);
        String uid = extractUIDFromCookies(request);
        if (token != null && uid != null) {
            // Token is valid, endpoint access allowed
            User user = userRepository.getByUid(uid);
            if(user != null && user.getToken().equals(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            response.sendRedirect("/login");
        } else {
            // If token is invalid, return 401 Unauthorized response
            response.sendRedirect("/login");
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
                    return token;
                }
            }
        }
        //Token not found or cookies is null
        return null;
    }

    private String extractUIDFromCookies(HttpServletRequest request) {
        // Extract the token from the request's cookies
        Cookie[] cookies = request.getCookies();
        // Check if cookies are present
        if (cookies != null) {
            // Loop through cookies to find the token
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("uid")) {
                    String token = cookie.getValue();
                    return token;
                }
            }
        }
        //Token not found or cookies is null
        return null;
    }
}
