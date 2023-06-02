//This is currently not used. So I commented everything
//package dsgp6.fakebook.web.configuration.filter;
//
//import dsgp6.fakebook.repository.UserRepository;
//import jakarta.servlet.*;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class AuthenticationFilter implements Filter {
//
//    private final UserRepository userRepository;
//
//    public AuthenticationFilter(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        // No token authentication needed for register and login
//        if (request.getRequestURI().equals("/api/user/register") || request.getRequestURI().equals("/api/user/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // Check if a token is present in the request's cookies
//        String token = extractTokenFromCookies(request);
//        if (token != null && isTokenValid(token)) {
//            // Token is valid, endpoint access allowed
//            filterChain.doFilter(request, response);
//            System.out.println("Auth filter is performed");
//        } else {
//            // If token is invalid, return 401 Unauthorized response
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        }
//    }
//
//    private String extractTokenFromCookies(HttpServletRequest request) {
//        // Extract the token from the request's cookies
//        Cookie[] cookies = request.getCookies();
//        // Check if cookies are present
//        if (cookies != null) {
//            // Loop through cookies to find the token
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("token")) {
//                    String token = cookie.getValue();
//                    System.out.println("Token found in cookies: " + token);
//                    return token;
//                }
//            }
//        }
//        System.out.println("Token not found in cookies.");
//        //Token not found or cookies is null
//        return null;
//    }
//
//    private boolean isTokenValid(String token) {
//        return userRepository.existsByToken(token);
//    }
//}
