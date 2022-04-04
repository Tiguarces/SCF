package pl.scf.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.TopicSubCategoryDTO;
import pl.scf.api.model.request.TopicSubCategorySaveRequest;
import pl.scf.api.model.request.TopicSubCategoryUpdateRequest;
import pl.scf.api.model.response.TopicSubCategoryResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.TopicSubCategory;
import pl.scf.model.services.TopicSubCategoryService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topic/sub/")
public class TopicSubCategoryApi {
    private final TopicSubCategoryService categoryService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final TopicSubCategorySaveRequest request) {
        final UniversalResponse response = categoryService.save(request);
        return buildResponseEntity(
                response.getSuccess() ? CREATED : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final TopicSubCategoryUpdateRequest request) {
        final UniversalResponse response = categoryService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> save(@PathVariable("id") final Long id) {
        final UniversalResponse response = categoryService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/all")
    public final ResponseEntity<List<TopicSubCategoryDTO>> getAll() {
        return buildResponseEntity(OK, categoryService.getAll());
    }

    @GetMapping("/all/names")
    public final ResponseEntity<List<String>> getAllNames() {
        return buildResponseEntity(OK, categoryService.getAllNames());
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<TopicSubCategoryResponse> getById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, categoryService.getById(id));
    }
}
