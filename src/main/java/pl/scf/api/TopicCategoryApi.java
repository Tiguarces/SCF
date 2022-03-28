package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.request.TopicCategorySaveRequest;
import pl.scf.api.model.request.TopicSubCategoryUpdateRequest;
import pl.scf.api.model.response.TopicCategoryResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.TopicCategory;
import pl.scf.model.services.TopicCategoryService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/topic/category")
public class TopicCategoryApi {
    private final TopicCategoryService categoryService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody TopicCategorySaveRequest request) {
        final UniversalResponse response = categoryService.save(request);
        return buildResponseEntity(
                response.getSuccess() ? CREATED : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody TopicSubCategoryUpdateRequest request) {
        final UniversalResponse response = categoryService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = categoryService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/all")
    public final ResponseEntity<List<TopicCategory>> getAll() {
        return buildResponseEntity(OK, categoryService.getAll());
    }

    @GetMapping("/all/names")
    public final ResponseEntity<List<String>> getAllCategoryNames() {
        return buildResponseEntity(OK, categoryService.getAllNames());
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<TopicCategoryResponse> getById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, categoryService.getById(id));
    }
}
