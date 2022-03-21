package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.ForumUserImagesUpdateRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.ForumUserImages;
import pl.scf.model.services.ForumUserImagesService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/forumUser/images")
public class ForumUserImagesApi {
    private final ForumUserImagesService imagesService;

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final ForumUserImagesUpdateRequest request) {
        final UniversalResponse response = imagesService.update(request);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = imagesService.delete(id);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<ForumUserImages> getById(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(imagesService.getById(id));
    }

    @GetMapping("/all")
    public final ResponseEntity<List<ForumUserImages>> getAll() {
        return ResponseEntity
                .status(OK)
                .body(imagesService.getAll());
    }
}
