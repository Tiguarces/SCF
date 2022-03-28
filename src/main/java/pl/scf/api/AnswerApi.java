package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.request.AnswerSaveRequest;
import pl.scf.api.model.request.UpdateAnswerRequest;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.Answer;
import pl.scf.model.services.AnswerService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/answer")
public class AnswerApi {
    private final AnswerService answerService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final AnswerSaveRequest request) {
        final UniversalResponse response = answerService.save(request);
        return buildResponseEntity(
                response.getSuccess() ? CREATED : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final UpdateAnswerRequest request) {
        final UniversalResponse response = answerService.update(request);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = answerService.delete(id);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/get/topic/{id}")
    public final ResponseEntity<List<Answer>> getAllByTopicId(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, answerService.getAllAnswersByTopicId(id));
    }

    @GetMapping("/get/user/{id}")
    public final ResponseEntity<List<Answer>> getAllByUserId(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, answerService.getAllAnswersByUserId(id));
    }
}
