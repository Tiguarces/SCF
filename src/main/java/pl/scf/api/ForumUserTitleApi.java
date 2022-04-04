package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.ForumUserDTO;
import pl.scf.api.model.dto.ForumUserTitleDTO;
import pl.scf.api.model.request.ForumUserTitleSaveRequest;
import pl.scf.api.model.request.ForumUserTitleUpdateRequest;
import pl.scf.api.model.response.ForumUserTitleResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.ForumUser;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.services.ForumUserTitleService;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/forumUser/title")
public class ForumUserTitleApi {
    private final ForumUserTitleService titleService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final ForumUserTitleSaveRequest request) {
        final UniversalResponse response = titleService.save(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final ForumUserTitleUpdateRequest request) {
        final UniversalResponse response = titleService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = titleService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<ForumUserTitleResponse> getById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, titleService.getById(id));
    }

    @GetMapping("/get/all")
    public final ResponseEntity<List<ForumUserTitleDTO>> getAll() {
        return buildResponseEntity(OK, titleService.getAll());
    }

    @GetMapping("/all/names/{name}")
    public final ResponseEntity<List<ForumUserDTO>> getAllUsersByTitleName(@PathVariable("name") final String name) {
        return buildResponseEntity(OK, titleService.getAllByTitleName(name));
    }

    @GetMapping("/all/intervals")
    public final ResponseEntity<Map<String, String>> getAllWithIntervals() {
        return buildResponseEntity(OK, titleService.getAllTitlesWithIntervals());
    }

}
