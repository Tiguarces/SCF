package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.ForumUser;
import pl.scf.model.services.ForumUserService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/forum/user")
public class UserForumApi {
    private final ForumUserService service;

    @DeleteMapping("/delete/{id}")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_MODERATOR" })
    public final ResponseEntity<UniversalResponse> deleteForumUser(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(service.delete(id));
    }

    @PutMapping("/update")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER" })
    public final ResponseEntity<UniversalResponse> updateForumUser(@RequestBody final ForumUser user) {
        return ResponseEntity
                .status(OK)
                .body(service.update(user));
    }

    @GetMapping("/all")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_MODERATOR" })
    public final ResponseEntity<List<ForumUser>> getAllUsers() {
        return ResponseEntity
                .status(OK)
                .body(service.getAll());
    }

    @GetMapping("/username/{username}")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER" })
    public final ResponseEntity<ForumUser> getByUsername(@PathVariable("username") final String username) {
        return ResponseEntity
                .status(OK)
                .body(service.getByUsername(username));
    }
}