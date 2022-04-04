package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.AnswerDTO;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.AnswerSaveRequest;
import pl.scf.api.model.request.UpdateAnswerRequest;
import pl.scf.api.model.response.AnswerResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.Answer;
import pl.scf.model.ForumUser;
import pl.scf.model.Topic;
import pl.scf.model.repositories.IAnswerRepository;
import pl.scf.model.repositories.IForumUserRepository;
import pl.scf.model.repositories.ITopicRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toAnswer;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final ITopicRepository topicRepository;
    private final IAnswerRepository answerRepository;
    private final IForumUserRepository forumUserRepository;
    private final String toMessageAnswerWord = "Answer";

    public final UniversalResponse save(final AnswerSaveRequest request) {
        try {
            final ForumUser forumUser = forumUserRepository.findById(request.getUserId())
                                                    .orElseThrow(NotFoundException::new);

            final Topic topic = topicRepository.findById(request.getTopicId())
                                                    .orElseThrow(NotFoundException::new);

            final Answer answer = Answer.builder()
                            .content(request.getContent())
                            .createdDate(request.getDate())
                            .user(forumUser)
                            .topic(topic)
                            .build();

            answerRepository.save(answer);
            log.info(SAVING, toMessageAnswerWord);

            return UniversalResponse.builder()
                    .success(true)
                    .message(SUCCESS_SAVING)
                    .date(new Date(System.currentTimeMillis()))
                    .build();

        } catch (final Exception exception) {
            log.warn(EXCEPTION_MESSAGE, "saving", toMessageAnswerWord, exception.getMessage());

            return UniversalResponse.builder()
                    .success(false)
                    .message(FAIL_SAVING)
                    .date(new Date(System.currentTimeMillis()))
                    .build();
        }
    }

    private AnswerResponse answerByIdResponse;
    public final AnswerResponse getById(final Long id) {
        answerRepository.findById(id).ifPresentOrElse(
                (foundAnswer) -> {
                    log.info(FETCH_BY_ID, toMessageAnswerWord, id);
                    final AnswerDTO answerDTO = AnswerDTO.builder()
                            .forumUserId(foundAnswer.getUser().getId())
                            .topicId(foundAnswer.getTopic().getId())
                            .createdDate(foundAnswer.getCreatedDate())
                            .content(foundAnswer.getContent())
                            .build();

                    answerByIdResponse = AnswerResponse.builder()
                            .success(true)
                            .message(SUCCESS_FETCHING)
                            .date(new Date(System.currentTimeMillis()))
                            .answer(answerDTO)
                            .build();
                }, () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, id);
                    answerByIdResponse = AnswerResponse.builder()
                            .success(false)
                            .message(FAIL_FETCHING)
                            .date(new Date(System.currentTimeMillis()))
                            .answer(null)
                            .build();
                }
        ); return answerByIdResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final UpdateAnswerRequest request) {
        answerRepository.findById(request.getAnswerId()).ifPresentOrElse(
                (foundAnswer) -> {
                    log.info(UPDATE_MESSAGE, toMessageAnswerWord, request.getAnswerId());
                    foundAnswer.setContent(request.getContent());
                    answerRepository.save(foundAnswer);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .message(SUCCESS_UPDATE)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, request.getAnswerId());
                    updateResponse = UniversalResponse.builder()
                            .success(false)
                            .message(FAIL_UPDATE)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        answerRepository.findById(id).ifPresentOrElse(
                (foundAnswer) -> {
                    log.info(DELETING_MESSAGE, toMessageAnswerWord, id);
                    answerRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .message(SUCCESS_DELETE)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .message(FAIL_DELETE)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<AnswerDTO> getAllAnswersByTopicId(final Long topicId) {
        log.info("Fetching all Answers from Topic with id: {}", topicId);
        return toAnswer(answerRepository.findAllByTopicId(topicId));
    }

    public final List<AnswerDTO> getAllAnswersByUserId(final Long userId) {
        log.info("Fetching all Answers by User with id: {}", userId);
        return toAnswer(answerRepository.findAllByUserId(userId));
    }

    public final List<AnswerDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageAnswerWord);
        return toAnswer(answerRepository.findAll());
    }

    public List<AnswerDTO> getAllLastAnswers(final Long amount) {
        log.info(FETCHING_ALL_MESSAGE, toMessageAnswerWord);
        return toAnswer(answerRepository
                        .findTopByOrderByIdDesc()
                        .stream()
                        .limit(amount)
                        .toList());
    }
}
