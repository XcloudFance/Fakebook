package dsgp6.fakebook.web.controller;

import dsgp6.fakebook.repository.PRepository;
import dsgp6.fakebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RestController
public class IndexController {

    private final UserService userService;
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/login")
    public String getLogin() throws IOException {
        return getIndexContent("static/login/index.html");
    }

    @GetMapping("/admin")
    public String getAdmin(@CookieValue(name = "uid") String uid, @CookieValue(name = "token") String token) throws IOException {
        if(userService.checkIdentity(uid, token))
            if(uid.equals("Admin"))
                return getIndexContent("static/admin/index.html");
        return null;
    }

    @GetMapping("/user")
    public String getUser() throws IOException {
        return getIndexContent("static/user/index.html");
    }

    @GetMapping("/reset")
    public String getReset() throws IOException {
        return getIndexContent("static/reset/index.html");
    }

    @GetMapping("/register")
    public String getRegister() throws IOException {
        return getIndexContent("static/register/index.html");
    }

    @GetMapping("/profile")
    public String getProfile() throws IOException {
        return getIndexContent("static/profile/index.html");
    }

    @GetMapping("/moments")
    public String getMoments() throws IOException {
        return getIndexContent("static/moments/index.html");
    }

    @GetMapping("/discover")
    public String getDiscover() throws IOException {
        return getIndexContent("static/discover/index.html");
    }

    @GetMapping("/contacts")
    public String getContacts() throws IOException {
        return getIndexContent("static/contacts/index.html");
    }

    private String getIndexContent(String resourcePath) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourcePath);
        byte[] fileBytes = Files.readAllBytes(resource.getFile().toPath());
        return new String(fileBytes, StandardCharsets.UTF_8);
    }
}
