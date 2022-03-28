package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.api.model.request.UserRoleUpdateRequest;
import pl.scf.model.UserRole;
import pl.scf.model.services.UserRoleService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
public class UserRoleApi {
    private final UserRoleService roleService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final String roleName) {
        final UniversalResponse response = roleService.save(roleName);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final UserRoleUpdateRequest request) {
        final UniversalResponse response = roleService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = roleService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<UserRole> getByRoleId(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, roleService.getById(id));
    }

    @GetMapping("/get/name/{name}")
    public final ResponseEntity<UserRole> getByRoleName(@PathVariable("name") final String name) {
        return buildResponseEntity(OK, roleService.getByName(name));
    }

    @GetMapping("/all")
    public final ResponseEntity<List<UserRole>> getAll() {
        return buildResponseEntity(OK, roleService.getAll());
    }
}
