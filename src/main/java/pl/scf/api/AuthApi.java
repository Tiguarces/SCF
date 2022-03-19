package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.scf.api.model.ActivateEmailResponse;
import pl.scf.api.model.RegisterResponse;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.AppUser;
import pl.scf.model.property.ActivateAccountProperty;
import pl.scf.model.requests.RegisterRequest;
import pl.scf.model.services.AppUserService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthApi {
    private final AppUserService userService;
    private final ActivateAccountProperty activateAccountProperty;

    @PostMapping("/register")
    public final ResponseEntity<RegisterResponse> registerUser(@RequestBody final RegisterRequest request) {
        final RegisterResponse response = userService.register(request);
        return ResponseEntity
                .status(response.getCreated() ? CREATED: OK)
                .body(response);
    }

    @GetMapping("/email/sendAgain/{userId}")
    public final ResponseEntity<UniversalResponse> sendActivationEmailAgain(@PathVariable final Long userId) {
        return ResponseEntity
                .status(OK)
                .body(userService.sendEmailAgain(userId));
    }

    @GetMapping("/activate/{token}")
    public final ModelAndView activateAccount(@PathVariable final String token) {
        final ActivateEmailResponse response = userService.activateAccount(token);
        final ModelAndView model = new ModelAndView("Verification");

        if (response.getActivated())
            model.addObject("message", activateAccountProperty.getPositive());
        else
            model.addObject("message", activateAccountProperty.getExists());

        model.addObject("date", response.getDate());
        model.addObject("nickname", response.getNickname());
        model.addObject("activated", response.getResponse());
        return model;
    }

    @GetMapping("/user/{id}")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR" })
    public final ResponseEntity<AppUser> getUserById(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(userService.getById(id));
    }

    @GetMapping("/user/{username}")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR" })
    public final ResponseEntity<AppUser> getUserByUsername(@PathVariable("username") final String username) {
        return ResponseEntity
                .status(OK)
                .body(userService.getByUsername(username));
    }

    @PostMapping("/user/update")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR" })
    public final ResponseEntity<UniversalResponse> updateUser(@RequestBody final AppUser user) {
        return ResponseEntity
                .status(OK)
                .body(userService.update(user));
    }

    @GetMapping("/user/delete/{id}")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_MODERATOR" })
    public final ResponseEntity<UniversalResponse> getUserByUsername(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(userService.delete(id));
    }

    @GetMapping("/all")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR" })
    public final List<AppUser> getAll() {
        return userService.getAll();
    }
}

