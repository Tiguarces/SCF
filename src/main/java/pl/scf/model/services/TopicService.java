package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.scf.api.model.TopicSaveRequest;
import pl.scf.api.model.TopicUpdateRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.Answer;
import pl.scf.model.Topic;
import pl.scf.model.TopicDetails;
import pl.scf.model.repositories.IForumUserRepository;
import pl.scf.model.repositories.ITopicRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.ApiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {
    private final ITopicRepository topicRepository;
    private final IForumUserRepository userRepository;

    private final String toMessageTopicWord = "Topic";

    private UniversalResponse saveResponse;
    public final UniversalResponse save(final TopicSaveRequest request) {
        if(request == null) {
            saveResponse = UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .response("Request is null")
                    .build();
        } else {
            userRepository.findById(request.getForumUserId()).ifPresentOrElse(
                    (foundUser) -> {
                        final TopicDetails details = TopicDetails.builder()
                                .description(request.getDescription())
                                .topicName(request.getTopicName())
                                .build();

                        final Topic topic = Topic.builder()
                                .user(foundUser)
                                .details(details)
                                .build();

                        log.info(SAVING, toMessageTopicWord);
                        topicRepository.save(topic);

                        saveResponse = UniversalResponse.builder()
                                .success(true)
                                .date(new Date(System.currentTimeMillis()))
                                .response(SUCCESS_SAVING)
                                .build();
                    },
                    () -> {
                        log.warn(NOT_FOUND_BY_ID, toMessageTopicWord, request.getForumUserId());
                        saveResponse = UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
                                .response(FAIL_SAVING)
                                .build();
                    }
            );
        } return saveResponse;
    }

    private Topic getByIdTopic;
    public final Topic getById(final Long id) {
        topicRepository.findById(id).ifPresentOrElse(
                (foundTopic) -> {
                    log.info(FETCH_BY_ID, toMessageTopicWord, id);
                    getByIdTopic = foundTopic;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicWord, id);
                    getByIdTopic = new Topic();
                }
        ); return getByIdTopic;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final TopicUpdateRequest request) {
        if(request == null) {
            updateResponse = UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .response("Request is null")
                    .build();
        } else {
            topicRepository.findById(request.getTopicId()).ifPresentOrElse(
                    (foundTopic) -> {
                        foundTopic.getDetails().setDescription(request.getDescription());

                        log.info(UPDATE_MESSAGE, toMessageTopicWord, request.getTopicId());
                        topicRepository.save(foundTopic);

                        updateResponse = UniversalResponse.builder()
                                .success(true)
                                .date(new Date(System.currentTimeMillis()))
                                .response(SUCCESS_UPDATE)
                                .build();
                    },
                    () -> {
                        log.warn(NOT_FOUND_BY_ID, toMessageTopicWord, request.getTopicId());
                        updateResponse = UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
                                .response(FAIL_UPDATE)
                                .build();
                    }
            );
        } return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        topicRepository.findById(id).ifPresentOrElse(
                (foundTopic) -> {
                    log.info(DELETING_MESSAGE, toMessageTopicWord, id);
                    topicRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .response(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<Topic> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicWord);
        return topicRepository.findAll();
    }

    @GetMapping("/get/user/id/{id}")
    public final List<Topic> getAllByUserId(@PathVariable("id") final Long userId) {
        log.info(FETCHING_BY_STH_MESSAGE, toMessageTopicWord, "id", userId);
        return topicRepository.findAllByUserId(userId);
    }

    public final List<Answer> getAllAnswers(final Long topicId) {
        log.info("Fetching all Answers from Topic with id: {}", topicId);
        return topicRepository.findAllAnswersById(topicId);
    }
}
