package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.TopicSaveRequest;
import pl.scf.api.model.TopicUpdateRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.Answer;
import pl.scf.model.Topic;
import pl.scf.model.services.TopicService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/topic")
public class TopicApi {
    private final TopicService topicService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final TopicSaveRequest request) {
        final UniversalResponse response = topicService.save(request);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final TopicUpdateRequest request) {
        final UniversalResponse response = topicService.update(request);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<Topic> getById(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(topicService.getById(id));
    }

    @GetMapping("/all")
    public final ResponseEntity<List<Topic>> getAll() {
        return ResponseEntity
                .status(OK)
                .body(topicService.getAll());
    }

    @GetMapping("/get/answers/id/{id}")
    public final ResponseEntity<List<Answer>> getAllAnswersByTopicId(@PathVariable("id") final Long id) {
        return ResponseEntity
                .status(OK)
                .body(topicService.getAllAnswers(id));
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = topicService.delete(id);
        return ResponseEntity
                .status(response.getSuccess() ? OK : INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
