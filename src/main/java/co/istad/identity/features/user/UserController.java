package co.istad.identity.features.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    @GetMapping
    public Map<?, ?> getUsers() {
        return Map.of("message", "User retrieve successfully");
    }
}
