package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.model.AppUser;
import pl.scf.model.requests.RegisterRequest;
import pl.scf.model.services.AppUserService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthApi {
    private final AppUserService userService;

    @PostMapping("/register")
    public final ResponseEntity<String> registerUser(@RequestBody final RegisterRequest request) {
        userService.register(request);
        return ResponseEntity
                .status(CREATED)
                .body("Created");
    }

    @GetMapping("/all")
    public final List<AppUser> getAll() {
        return userService.getAll();
    }
}
