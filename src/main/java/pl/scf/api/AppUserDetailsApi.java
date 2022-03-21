package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.AppUserDetailsUpdateRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.AppUserDetails;
import pl.scf.model.services.AppUserDetailsService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/userDetails/")
public class AppUserDetailsApi {
    private final AppUserDetailsService detailsService;

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final AppUserDetailsUpdateRequest request) {
        final UniversalResponse response = detailsService.update(request);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = detailsService.delete(id);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/all")
    public final ResponseEntity<List<AppUserDetails>> getAll() {
        return ResponseEntity
                .status(OK)
                .body(detailsService.getAll());
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<AppUserDetails> getById(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(detailsService.getById(id));
    }

    @GetMapping("/get/username/{username}")
    public final ResponseEntity<AppUserDetails> getByUsername(@PathVariable("username") final String username) {
        return ResponseEntity
                .status(OK)
                .body(detailsService.getByUsername(username));
    }
}
