package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.Answer;

@Repository
public interface IAnswerRepository extends JpaRepository<Answer, Long> {
}
