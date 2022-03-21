package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.AnswerSaveRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.api.model.UpdateAnswerRequest;
import pl.scf.model.Answer;
import pl.scf.model.services.AnswerService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("/answer")
public class AnswerApi {
    private final AnswerService answerService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final AnswerSaveRequest request) {
        final UniversalResponse response = answerService.save(request);
        return ResponseEntity
                .status(response.getSuccess() ? CREATED : INTERNAL_SERVER_ERROR)
                .body(response);
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
        return ResponseEntity
                .status(OK)
                .body(answerService.getAllAnswersByTopicId(id));
    }

    @GetMapping("/get/user/{id}")
    public final ResponseEntity<List<Answer>> getAllByUserId(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(answerService.getAllAnswersByUserId(id));
    }
}
