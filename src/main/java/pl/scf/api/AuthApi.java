package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.scf.api.model.dto.AppUserDTO;
import pl.scf.api.model.request.RegisterRequest;
import pl.scf.api.model.request.UpdateUserRequest;
import pl.scf.api.model.response.ActivateEmailResponse;
import pl.scf.api.model.response.AppUserResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.AppUser;
import pl.scf.model.property.ActivateAccountProperty;
import pl.scf.model.services.AppUserService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthApi {
    private final AppUserService userService;
    private final ActivateAccountProperty activateAccountProperty;

    @PostMapping("/register")
    public final ResponseEntity<UniversalResponse> registerUser(@RequestBody final RegisterRequest request) {
        final UniversalResponse response = userService.register(request);
        return buildResponseEntity(
                response.getSuccess() ? CREATED : OK,
                response
        );
    }

    @GetMapping("/email/sendAgain/{userId}")
    public final ResponseEntity<UniversalResponse> sendActivationEmailAgain(@PathVariable final Long userId) {
        return buildResponseEntity(OK, userService.sendEmailAgain(userId));
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
        model.addObject("activated", response.getMessage());
        return model;
    }

    @GetMapping("/user/id/{id}")
    public final ResponseEntity<AppUserResponse> getUserById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, userService.getById(id));
    }

    @GetMapping("/user/name/{username}")
    public final ResponseEntity<AppUserResponse> getUserByUsername(@PathVariable("username") final String username) {
        return buildResponseEntity(OK, userService.getByUsername(username));
    }

    @PutMapping("/user/update")
    public final ResponseEntity<UniversalResponse> updateUser(@RequestBody final UpdateUserRequest request) {
        final UniversalResponse response = userService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/user/delete/{id}")
    public final ResponseEntity<UniversalResponse> deleteById(@PathVariable("id") final Long id) {
        final UniversalResponse response = userService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/user/delete/all")
    public final ResponseEntity<UniversalResponse> deleteAll() {
        final UniversalResponse response = userService.deleteAll();
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/get/all")
    public final ResponseEntity<List<AppUserDTO>> getAll() {
        return buildResponseEntity(OK, userService.getAll());
    }
}

