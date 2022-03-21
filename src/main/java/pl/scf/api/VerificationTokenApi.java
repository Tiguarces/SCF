package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.VerificationToken;
import pl.scf.model.services.VerificationTokenService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class VerificationTokenApi {
    private final VerificationTokenService tokenService;

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final VerificationToken verificationToken) {
        final UniversalResponse response = tokenService.update(verificationToken);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = tokenService.delete(id);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<VerificationToken> getById(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(tokenService.getById(id));
    }

    @GetMapping("/get/name/{id}")
    public final ResponseEntity<VerificationToken> getByTokenName(@PathVariable("name") final String tokenName) {
        return ResponseEntity
                .status(OK)
                .body(tokenService.findByTokenName(tokenName));
    }

    @GetMapping("/all")
    public final ResponseEntity<List<VerificationToken>> getAll() {
        return ResponseEntity
                .status(OK)
                .body(tokenService.getAll());
    }
}
