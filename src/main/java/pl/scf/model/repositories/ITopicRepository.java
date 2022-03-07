package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.Answer;
import pl.scf.model.Topic;

import java.util.List;

@Repository
public interface ITopicRepository extends JpaRepository<Topic, Long> {
    List<Answer> findAllAnswersById(Long topicId);
}
