package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.TopicSaveRequest;
import pl.scf.api.model.request.TopicUpdateRequest;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.*;
import pl.scf.model.repositories.IForumUserRepository;
import pl.scf.model.repositories.ITopicCategoryRepository;
import pl.scf.model.repositories.ITopicRepository;
import pl.scf.model.repositories.ITopicSubCategoryRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {
    private final ITopicRepository topicRepository;
    private final IForumUserRepository userRepository;
    private final ITopicCategoryRepository categoryRepository;
    private final ITopicSubCategoryRepository subCategoryRepository;

    private final String toMessageTopicWord = "Topic";

    public final UniversalResponse save(final TopicSaveRequest request) {
        try {
            final TopicSubCategory subCategory = subCategoryRepository.findByName(request.getSubCategoryName())
                                                                                    .orElseThrow(NotFoundException::new);

            final TopicCategory topicCategory = categoryRepository.findByName(request.getCategoryName())
                                                                                    .orElseThrow(NotFoundException::new);

            final ForumUser forumUser = userRepository.findById(request.getForumUserId())
                                                                                    .orElseThrow(NotFoundException::new);

            final TopicDetails details = TopicDetails.builder()
                    .description(request.getDescription())
                    .topicName(request.getTopicName())
                    .build();

            subCategory.setCategory(topicCategory);

            final Topic topic = Topic.builder()
                    .user(forumUser)
                    .details(details)
                    .subCategory(subCategory)
                    .build();

            log.info(SAVING, toMessageTopicWord);
            topicRepository.save(topic);

            return UniversalResponse.builder()
                    .success(true)
                    .date(new Date(System.currentTimeMillis()))
                    .message(SUCCESS_SAVING)
                    .build();

        } catch (final IllegalArgumentException | NullPointerException | NotFoundException exception) {
            log.warn(EXCEPTION_MESSAGE, "saving", toMessageTopicWord, exception.getMessage());
            return UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(FAIL_SAVING)
                    .build();
        }
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
        topicRepository.findById(request.getTopicId()).ifPresentOrElse(
                (foundTopic) -> {
                    foundTopic.getDetails().setDescription(request.getDescription());

                    log.info(UPDATE_MESSAGE, toMessageTopicWord, request.getTopicId());
                    topicRepository.save(foundTopic);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicWord, request.getTopicId());
                    updateResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_UPDATE)
                            .build();
                }
        ); return updateResponse;
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
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<Topic> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicWord);
        return topicRepository.findAll();
    }

    public final List<Topic> getAllByUserId(final Long userId) {
        log.info(FETCHING_BY_STH_MESSAGE, toMessageTopicWord, "id", userId);
        return topicRepository.findAllByUserId(userId);
    }

    public final List<Answer> getAllAnswers(final Long topicId) {
        log.info("Fetching all Answers from Topic with id: {}", topicId);
        return topicRepository.findAllAnswersById(topicId);
    }
}
