package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.Topic;

@Repository
public interface ITopicRepository extends JpaRepository<Topic, Long> {
}
