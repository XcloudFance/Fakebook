package dsgp6.fakebook.web.configuration;

import dsgp6.fakebook.repository.UserRepository;
import dsgp6.fakebook.web.configuration.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable()) // Disable CSRF protection for simplicity, you can enable it as per your requirements
                .addFilterBefore(new AuthenticationFilter(userRepository), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/user/**").permitAll()
                .anyRequest().authenticated()
                )
                ;
        return http.build();
    }
}

