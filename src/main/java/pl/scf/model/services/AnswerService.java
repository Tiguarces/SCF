package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.AnswerDTO;
import pl.scf.api.model.dto.LastAnswerDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.AnswerSaveRequest;
import pl.scf.api.model.request.UpdateAnswerRequest;
import pl.scf.api.model.response.AnswerResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.Answer;
import pl.scf.model.repositories.IAnswerRepository;
import pl.scf.model.repositories.IForumUserRepository;
import pl.scf.model.repositories.ITopicRepository;

import java.time.Instant;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toAnswers;
import static pl.scf.api.model.utils.DTOMapper.toLastAnswers;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final ITopicRepository topicRepository;
    private final IAnswerRepository answerRepository;
    private final IForumUserRepository forumUserRepository;
    private final String toMessageAnswerWord = "Answer";

    public final UniversalResponse save(final AnswerSaveRequest request) throws NotFoundException {
        final var forumUser = forumUserRepository.findById(request.getUserId());
        final var topic = topicRepository.findById(request.getTopicId());

        if(forumUser.isEmpty())
            throw new NotFoundException("Not found ForumUser, fail saving");

        if(topic.isEmpty())
            throw new NotFoundException("Not found Topic, fail saving");

        final Answer answer = Answer.builder()
                .content(request.getContent())
                .createdDate(request.getDate())
                .user(forumUser.get())
                .topic(topic.get())
                .build();

        answerRepository.save(answer);
        log.info(SAVING, toMessageAnswerWord);

        return UniversalResponse.builder()
                .success(true)
                .message(SUCCESS_SAVING)
                .date(Instant.now())
                .build();
    }

    private AnswerResponse answerByIdResponse;
    public final AnswerResponse getById(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        answerRepository.findById(id).ifPresentOrElse(
                (foundAnswer) -> {
                    log.info(FETCH_BY_ID, toMessageAnswerWord, id);
                    final AnswerDTO answerDTO = AnswerDTO.builder()
                            .forumUserId(foundAnswer.getUser().getId())
                            .topicId(foundAnswer.getTopic().getId())
                            .createdDate(foundAnswer.getCreatedDate().toString()) // TODO: change to Instant
                            .content(foundAnswer.getContent())
                            .build();

                    answerByIdResponse = AnswerResponse.builder()
                            .success(true)
                            .message(SUCCESS_FETCHING)
                            .date(Instant.now())
                            .answer(answerDTO)
                            .build();
                }, () -> {
                    throw new NotFoundException("Not found Answer with specified id, fail fetching");
                }
        ); return answerByIdResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final UpdateAnswerRequest request) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(request.getAnswerId());

        answerRepository.findById(request.getAnswerId()).ifPresentOrElse(
                (foundAnswer) -> {
                    log.info(UPDATE_MESSAGE, toMessageAnswerWord, request.getAnswerId());
                    foundAnswer.setContent(request.getContent());
                    answerRepository.save(foundAnswer);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .message(SUCCESS_UPDATE)
                            .date(Instant.now())
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found Answer, fail updating");
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        answerRepository.findById(id).ifPresentOrElse(
                (foundAnswer) -> {
                    log.info(DELETING_MESSAGE, toMessageAnswerWord, id);
                    answerRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .message(SUCCESS_DELETE)
                            .date(Instant.now())
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found Answer with specified id, fail deleting");
                }
        ); return deleteResponse;
    }

    public final List<AnswerDTO> getAllAnswersByTopicId(final Long topicId) {
        log.info("Fetching all Answers from Topic with id: {}", topicId);
        return toAnswers(answerRepository.findAllByTopicId(topicId));
    }

    public final List<AnswerDTO> getAllAnswersByUserId(final Long userId) {
        log.info("Fetching all Answers by User with id: {}", userId);
        return toAnswers(answerRepository.findAllByUserId(userId));
    }

    public final List<AnswerDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageAnswerWord);
        return toAnswers(answerRepository.findAll());
    }

    public List<LastAnswerDTO> getAllLastAnswers(final Long amount) {
        log.info(FETCHING_ALL_MESSAGE, toMessageAnswerWord);
        return toLastAnswers(answerRepository
                        .findAllTopByOrderByIdDesc()
                        .stream()
                        .limit(amount)
                        .toList());
    }
}
