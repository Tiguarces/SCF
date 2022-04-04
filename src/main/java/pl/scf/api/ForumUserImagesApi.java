package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.ForumUserImagesDTO;
import pl.scf.api.model.request.ForumUserImagesUpdateRequest;
import pl.scf.api.model.response.ForumUserImagesResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.ForumUserImages;
import pl.scf.model.services.ForumUserImagesService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/forumUser/images")
public class ForumUserImagesApi {
    private final ForumUserImagesService imagesService;

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final ForumUserImagesUpdateRequest request) {
        final UniversalResponse response = imagesService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = imagesService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<ForumUserImagesResponse> getById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, imagesService.getById(id));
    }

    @GetMapping("/all")
    public final ResponseEntity<List<ForumUserImagesDTO>> getAll() {
        return buildResponseEntity(OK, imagesService.getAll());
    }
}
