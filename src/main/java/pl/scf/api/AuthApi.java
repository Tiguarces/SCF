package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.RegisterResponse;
import pl.scf.model.AppUser;
import pl.scf.model.requests.RegisterRequest;
import pl.scf.model.services.AppUserService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthApi {
    private final AppUserService userService;

    @PostMapping("/register")
    public final ResponseEntity<RegisterResponse> registerUser(@RequestBody final RegisterRequest request) {
        final RegisterResponse response = userService.register(request);
        return ResponseEntity
                .status(response.getCreated() ? CREATED: OK)
                .body(response);
    }

    @PostMapping("/activate/{token}")
    public final ResponseEntity<String> activateAccount(@PathVariable final String token) {
        userService.activateAccount(token);
        return ResponseEntity
                .status(OK)
                .body("Email activated");
    }

    @GetMapping("/all")
    public final List<AppUser> getAll() {
        return userService.getAll();
    }
}
