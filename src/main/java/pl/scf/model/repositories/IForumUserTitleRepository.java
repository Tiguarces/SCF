package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.ForumUserTitle;

@Repository
public interface IForumUserTitleRepository extends JpaRepository<ForumUserTitle, Long> {
}
