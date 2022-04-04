package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.VerificationTokenDTO;
import pl.scf.api.model.request.VerificationTokenSaveRequest;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.api.model.response.VerificationTokenResponse;
import pl.scf.model.services.VerificationTokenService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class VerificationTokenApi {
    private final VerificationTokenService tokenService;

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final VerificationTokenSaveRequest request) {
        final UniversalResponse response = tokenService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = tokenService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<VerificationTokenResponse> getById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, tokenService.getById(id));
    }

    @GetMapping("/get/name/{id}")
    public final ResponseEntity<VerificationTokenResponse> getByTokenName(@PathVariable("name") final String tokenName) {
        return buildResponseEntity(OK, tokenService.getByTokenName(tokenName));
    }

    @GetMapping("/get/all")
    public final ResponseEntity<List<VerificationTokenDTO>> getAll() {
        return buildResponseEntity(OK, tokenService.getAll());
    }
}
