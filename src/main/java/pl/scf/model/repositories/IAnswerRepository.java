package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.Answer;

import java.util.Collection;
import java.util.List;

@Repository
public interface IAnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByTopicId(Long topicId);

    List<Answer> findAllByUserId(Long userId);

    List<Answer> findAllTopByOrderByIdDesc();

    List<Answer> findAllByTopicIdIn(final List<Long> topicsId);

}
