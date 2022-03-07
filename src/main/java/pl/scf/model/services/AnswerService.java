package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.model.Answer;
import pl.scf.model.repositories.IAnswerRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AnswerService {
    private final IAnswerRepository answerRepository;

    public final void save(final Answer answer) {
        log.info("Saving new Answer");
        answerRepository.save(answer);
    }

    public final Answer getById(final Long id) {
        log.info("Fetching Answer with id: {}", id);
        return answerRepository.findById(id).orElse(new Answer());
    }

    public final void update(final Answer answer) {
        log.info("Updating Answer with id: {}", answer.getId());
        answerRepository.save(answer);
    }

    public final void delete(final Long id) {
        log.info("Deleting Answer with id: {}", id);
        answerRepository.deleteById(id);
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
        log.info("Fetching all Answers");
        return answerRepository.findAll();
    }
}
