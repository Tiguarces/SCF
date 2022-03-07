package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.ForumUser;
import pl.scf.model.ForumUserTitle;

import java.util.List;

@Repository
public interface IForumUserTitleRepository extends JpaRepository<ForumUserTitle, Long> {
    List<ForumUser> findAllUsersByTitleName(String titleName);
}
