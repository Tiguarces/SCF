package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.AnswerDTO;
import pl.scf.api.model.dto.TopicDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.TopicSaveRequest;
import pl.scf.api.model.request.TopicUpdateRequest;
import pl.scf.api.model.response.TopicResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.Answer;
import pl.scf.model.Topic;
import pl.scf.model.TopicDetails;
import pl.scf.model.repositories.IForumUserRepository;
import pl.scf.model.repositories.ITopicCategoryRepository;
import pl.scf.model.repositories.ITopicRepository;

import java.time.Instant;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {
    private final ITopicRepository topicRepository;
    private final IForumUserRepository userRepository;
    private final ITopicCategoryRepository categoryRepository;

    private final String toMessageTopicWord = "Topic";

    private UniversalResponse saveResponse;
    public final UniversalResponse save(final TopicSaveRequest request) throws NotFoundException {
        final var topicCategory = categoryRepository.findByName(request.getCategoryName());
        final var forumUser = userRepository.findById(request.getForumUserId());

        if(topicCategory.isEmpty())
            throw new NotFoundException("Not found TopicCategory");

        if(forumUser.isEmpty())
            throw new NotFoundException("Not found ForumUser");

        final var subCategory = topicCategory.get().getSubCategory();
        subCategory.parallelStream()
                .findAny()
                .ifPresentOrElse(
                        (foundSubCategory) -> {
                            final TopicDetails details = TopicDetails.builder()
                                    .description(request.getDescription())
                                    .topicName(request.getTopicName())
                                    .build();

                            foundSubCategory.setCategory(topicCategory.get());

                            final Topic topic = Topic.builder()
                                    .user(forumUser.get())
                                    .details(details)
                                    .subCategory(foundSubCategory)
                                    .build();

                            log.info(SAVING, toMessageTopicWord);
                            topicRepository.save(topic);

                            saveResponse = UniversalResponse.builder()
                                    .success(true)
                                    .date(Instant.now())
                                    .message(SUCCESS_SAVING)
                                    .build();
                        }, () -> {
                            throw new NotFoundException("Noc found TopicSubCategory with specified name");
                        }
                ); return saveResponse;
    }

    private TopicResponse topicByIdResponse;
    public final TopicResponse getById(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        topicRepository.findById(id).ifPresentOrElse(
                (foundTopic) -> {
                    log.info(FETCH_BY_ID, toMessageTopicWord, id);
                    final TopicDTO topicDTO = TopicDTO.builder()
                            .detailsId(foundTopic.getDetails().getId())
                            .subCategoryName(foundTopic.getSubCategory().getName())
                            .userId(foundTopic.getUser().getId())
                            .build();

                    topicByIdResponse = TopicResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .topic(topicDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found Topic with specified id");
                }
        ); return topicByIdResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final TopicUpdateRequest request) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(request.getTopicId());

        topicRepository.findById(request.getTopicId()).ifPresentOrElse(
                (foundTopic) -> {
                    foundTopic.getDetails().setDescription(request.getDescription());

                    log.info(UPDATE_MESSAGE, toMessageTopicWord, request.getTopicId());
                    topicRepository.save(foundTopic);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found Topic with specified id");
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        topicRepository.findById(id).ifPresentOrElse(
                (foundTopic) -> {
                    log.info(DELETING_MESSAGE, toMessageTopicWord, id);
                    topicRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found Topic with specified id");
                }
        ); return deleteResponse;
    }

    public final List<TopicDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicWord);
        return topicRepository.findAll()
                .parallelStream()
                .map(TopicService::toTopicDto)
                .toList();
    }

    public final List<TopicDTO> getAllByUserId(final Long userId) {
        log.info(FETCHING_BY_STH_MESSAGE, toMessageTopicWord, "id", userId);
        return topicRepository.findAllByUserId(userId)
                .parallelStream()
                .map(TopicService::toTopicDto)
                .toList();
    }

    private static TopicDTO toTopicDto(final Topic topic) {
        return TopicDTO.builder()
                .detailsId(topic.getDetails().getId())
                .subCategoryName(topic.getSubCategory().getName())
                .userId(topic.getUser().getId())
                .build();
    }

    public final List<AnswerDTO> getAllAnswers(final Long topicId) {
        log.info("Fetching all Answers from Topic with id: {}", topicId);
        return topicRepository.findAllAnswersById(topicId)
                .parallelStream()
                .map(TopicService::toAnswerDto)
                .toList();
    }

    private static AnswerDTO toAnswerDto(final Answer answer) {
        return AnswerDTO.builder()
                .content(answer.getContent())
                .createdDate(answer.getCreatedDate())
                .topicId(answer.getTopic().getId())
                .forumUserId(answer.getUser().getId())
                .build();
    }
}
