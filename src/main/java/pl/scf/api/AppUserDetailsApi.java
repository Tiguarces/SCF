package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.AppUserDetailsDTO;
import pl.scf.api.model.request.AppUserDetailsUpdateRequest;
import pl.scf.api.model.response.AppUserDetailsResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.AppUserDetails;
import pl.scf.model.services.AppUserDetailsService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/userDetails/")
public class AppUserDetailsApi {
    private final AppUserDetailsService detailsService;

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final AppUserDetailsUpdateRequest request) {
        final UniversalResponse response = detailsService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = detailsService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/all")
    public final ResponseEntity<List<AppUserDetailsDTO>> getAll() {
        return buildResponseEntity(OK, detailsService.getAll());
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<AppUserDetailsResponse> getById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, detailsService.getById(id));
    }

    @GetMapping("/get/username/{username}")
    public final ResponseEntity<AppUserDetailsResponse> getByUsername(@PathVariable("username") final String username) {
        return buildResponseEntity(OK, detailsService.getByUsername(username));
    }
}
