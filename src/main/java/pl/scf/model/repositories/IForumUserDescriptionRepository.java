package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.ForumUserDescription;

@Repository
public interface IForumUserDescriptionRepository extends JpaRepository<ForumUserDescription, Long> {
}
