package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.ForumUserDescriptionUpdateRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.ForumUserDescription;
import pl.scf.model.services.ForumUserDescriptionService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/forumUser/description")
public class ForumUserDescriptionApi {
    private final ForumUserDescriptionService descriptionService;

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final ForumUserDescriptionUpdateRequest request) {
        final UniversalResponse response = descriptionService.update(request);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = descriptionService.delete(id);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<ForumUserDescription> getById(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(descriptionService.getById(id));
    }

    @GetMapping("/all")
    public final ResponseEntity<List<ForumUserDescription>> getAll() {
        return ResponseEntity
                .status(OK)
                .body(descriptionService.getAll());
    }
}
