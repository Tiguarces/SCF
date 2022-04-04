package pl.scf.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scf.api.model.dto.TopicDetailsDTO;
import pl.scf.api.model.request.TopicDetailsUpdateRequest;
import pl.scf.api.model.response.TopicDetailsResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.TopicDetails;
import pl.scf.model.services.TopicDetailsService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/topic/details")
public class TopicDetailsApi {
    private final TopicDetailsService detailsService;

    @PostMapping("/update")
    public final ResponseEntity<UniversalResponse> update(@RequestBody final TopicDetailsUpdateRequest request) {
        final UniversalResponse response = detailsService.update(request);
        return buildResponseEntity(
                response.getSuccess() ? OK : INTERNAL_SERVER_ERROR,
                response
        );
    }

    @GetMapping("/get/id/{id}")
    public final ResponseEntity<TopicDetailsResponse> getById(@PathVariable("id") final Long id) {
        return buildResponseEntity(OK, detailsService.getById(id));
    }

    @GetMapping("/get/all")
    public final ResponseEntity<List<TopicDetailsDTO>> getAll() {
        return buildResponseEntity(OK, detailsService.getAll());
    }

    @GetMapping("/get/all/last/{amount}")
    public final ResponseEntity<List<TopicDetailsDTO>> getALlLastTopics(@PathVariable("amount") final Long amount) {
        return buildResponseEntity(OK, detailsService.getAllLastTopics(amount));
    }
}
