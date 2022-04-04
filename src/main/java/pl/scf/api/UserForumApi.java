package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.ForumUserDTO;
import pl.scf.api.model.response.ForumUserResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.ForumUser;
import pl.scf.model.services.ForumUserService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/forum/user")
public class UserForumApi {
    private final ForumUserService forumUserService;

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> deleteForumUser(@PathVariable("id") final Long id) {
        final UniversalResponse response = forumUserService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/all")
    public final ResponseEntity<List<ForumUserDTO>> getAllUsers() {
        return buildResponseEntity(OK, forumUserService.getAll());
    }

    @GetMapping("/username/{username}")
    public final ResponseEntity<ForumUserResponse> getByUsername(@PathVariable("username") final String username) {
        return buildResponseEntity(OK, forumUserService.getByUsername(username));
    }
}
