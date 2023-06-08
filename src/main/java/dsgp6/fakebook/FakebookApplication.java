package dsgp6.fakebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FakebookApplication {

	public static void main(String[] args) {
            SpringApplication.run(FakebookApplication.class, args);
	}
  
}
