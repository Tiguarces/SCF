package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.ForumUserDescriptionDTO;
import pl.scf.api.model.request.ForumUserDescriptionUpdateRequest;
import pl.scf.api.model.response.ForumUserDescriptionResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.services.ForumUserDescriptionService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/forumUser/description")
public class ForumUserDescriptionApi {
    private final ForumUserDescriptionService descriptionService;

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final ForumUserDescriptionUpdateRequest request) {
        final UniversalResponse response = descriptionService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = descriptionService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<ForumUserDescriptionResponse> getById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, descriptionService.getById(id));
    }

    @GetMapping("/all")
    public final ResponseEntity<List<ForumUserDescriptionDTO>> getAll() {
        return buildResponseEntity(OK, descriptionService.getAll());
    }
}
