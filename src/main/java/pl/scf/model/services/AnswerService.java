package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.AnswerSaveRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.api.model.UpdateAnswerRequest;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.model.Answer;
import pl.scf.model.ForumUser;
import pl.scf.model.Topic;
import pl.scf.model.repositories.IAnswerRepository;
import pl.scf.model.repositories.IForumUserRepository;
import pl.scf.model.repositories.ITopicRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.ApiConstants.*;

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
                    .response(SUCCESS_SAVING)
                    .date(new Date(System.currentTimeMillis()))
                    .build();

        } catch (final Exception exception) {
            log.warn(EXCEPTION_MESSAGE, "saving", toMessageAnswerWord, exception.getMessage());

            return UniversalResponse.builder()
                    .success(false)
                    .response(FAIL_SAVING)
                    .date(new Date(System.currentTimeMillis()))
                    .build();
        }
    }

    private Answer answerById;
    public final Answer getById(final Long id) {
        answerRepository.findById(id).ifPresentOrElse(
                (foundAnswer) -> {
                    log.info(FETCH_BY_ID, toMessageAnswerWord, id);
                    answerById = foundAnswer;
                }, () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, id);
                    answerById = new Answer();
                }
        ); return  answerById;
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
                            .response(SUCCESS_UPDATE)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, request.getAnswerId());
                    updateResponse = UniversalResponse.builder()
                            .success(false)
                            .response(FAIL_UPDATE)
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
                            .response(SUCCESS_DELETE)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .response(FAIL_DELETE)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<Answer> getAllAnswersByTopicId(final Long topicId) {
        log.info("Fetching all Answers from Topic with id: {}", topicId);
        return answerRepository.findAllByTopicId(topicId);
    }

    public final List<Answer> getAllAnswersByUserId(final Long userId) {
        log.info("Fetching all Answers by User with id: {}", userId);
        return answerRepository.findAllByUserId(userId);
    }

    public final List<Answer> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageAnswerWord);
        return answerRepository.findAll();
    }
}
