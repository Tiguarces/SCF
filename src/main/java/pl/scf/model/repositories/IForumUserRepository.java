package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.ForumUser;

@Repository
public interface IForumUserRepository extends JpaRepository<ForumUser, Long> {
}
