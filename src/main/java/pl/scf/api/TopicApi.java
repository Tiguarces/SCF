package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.AnswerDTO;
import pl.scf.api.model.dto.LastTopicDTO;
import pl.scf.api.model.dto.TopicDTO;
import pl.scf.api.model.request.TopicSaveRequest;
import pl.scf.api.model.request.TopicUpdateRequest;
import pl.scf.api.model.response.TopicResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.services.TopicService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/topic")
public class TopicApi {
    private final TopicService topicService;

    @PostMapping("/add")
    public final ResponseEntity<UniversalResponse> save(@RequestBody final TopicSaveRequest request) {
        final UniversalResponse response = topicService.save(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @PutMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final TopicUpdateRequest request) {
        final UniversalResponse response = topicService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @DeleteMapping("/delete/{id}")
    public final ResponseEntity<UniversalResponse> delete(@PathVariable("id") final Long id) {
        final UniversalResponse response = topicService.delete(id);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<TopicResponse> getByTopicId(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, topicService.getById(id));
    }

    @GetMapping("/get/name/{name}")
    public final ResponseEntity<TopicResponse> getByTopicName(@PathVariable("name") final String name) {
        return buildResponseEntity(OK, topicService.getByTopicName(name));
    }

    @GetMapping("/get/user/all/id/{id}")
    public final ResponseEntity<List<TopicDTO>> getAllByUserId(@PathVariable("id") final Long userId) {
        return buildResponseEntity(OK, topicService.getAllByUserId(userId));
    }

    @GetMapping("/all")
    public final ResponseEntity<List<TopicDTO>> getAll() {
        return buildResponseEntity(OK, topicService.getAll());
    }

    @GetMapping("/get/answers/id/{id}")
    public final ResponseEntity<List<AnswerDTO>> getAllAnswersByTopicId(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, topicService.getAllAnswers(id));
    }

    @GetMapping("/all/last/{amount}")
    public final ResponseEntity<List<LastTopicDTO>> getLastAnswers(@PathVariable("amount") final Long amount) {
        return buildResponseEntity(OK, topicService.getAllLastTopics(amount));
    }
}
