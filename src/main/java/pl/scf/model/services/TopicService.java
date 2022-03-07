package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.model.Answer;
import pl.scf.model.Topic;
import pl.scf.model.repositories.ITopicRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TopicService {
    private final ITopicRepository topicRepository;

    public final void save(final Topic topic) {
        log.info("Saving new topic");
        topicRepository.save(topic);
    }

    public final Topic getById(final Long id) {
        log.info("Fetching Topic with id: {}", id);
        return topicRepository.findById(id).orElse(new Topic());
    }

    public final void update(final Topic topic) {
        log.info("Updating Topic with id: {}", topic.getId());
        topicRepository.save(topic);
    }

    public final void delete(final Long id) {
        log.info("Deleting Topic with id: {}", id);
        topicRepository.deleteById(id);
    }

    public final List<Topic> getAll() {
        log.info("Fetching all Topics");
        return topicRepository.findAll();
    }

    public final List<Answer> getAllAnswers(final Long topicId) {
        log.info("Fetching all Answers from Topic with id: {}", topicId);
        return topicRepository.findAllAnswersById(topicId);
    }
}
