package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.ForumUserImages;

@Repository
public interface IForumUserImagesRepository extends JpaRepository<ForumUserImages, Long> {
}
