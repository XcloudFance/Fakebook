package dsgp6.fakebook.web.configuration;

import dsgp6.fakebook.repository.UserRepository;
import dsgp6.fakebook.web.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;

    public WebMvcConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(userRepository))
                .addPathPatterns("/api/user/**") // Apply authentication to all endpoints under "/api/user"
                .excludePathPatterns("/api/user/register", "/api/user/login"); // Exclude "/register" and "/login" from authentication
    }

}
