package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.ForumUserTitleSaveRequest;
import pl.scf.api.model.ForumUserTitleUpdateRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.ForumUser;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.services.ForumUserTitleService;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/forumUser/title")
public class ForumUserTitleApi {
    private final ForumUserTitleService titleService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final ForumUserTitleSaveRequest request) {
        final UniversalResponse response = titleService.save(request);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final ForumUserTitleUpdateRequest request) {
        final UniversalResponse response = titleService.update(request);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<ForumUserTitle> getById(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(titleService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = titleService.delete(id);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/all")
    public final ResponseEntity<List<ForumUserTitle>> getAll() {
        return ResponseEntity
                .status(OK)
                .body(titleService.getAll());
    }

    @GetMapping("/all/names/{name}")
    public final ResponseEntity<List<ForumUser>> getAllUsersByTitleName(@PathVariable("name") final String name) {
        return ResponseEntity
                .status(OK)
                .body(titleService.getAllByTitleName(name));
    }

    @GetMapping("/all/intervals")
    public final ResponseEntity<Map<String, String>> getAllWithIntervals() {
        return ResponseEntity
                .status(OK)
                .body(titleService.getAllTitlesWithIntervals());
    }

}
