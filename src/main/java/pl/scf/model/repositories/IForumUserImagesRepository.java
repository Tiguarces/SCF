package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.ForumUserImages;

import java.util.Optional;

@Repository
public interface IForumUserImagesRepository extends JpaRepository<ForumUserImages, Long> {
    Optional<ForumUserImages> findByUserId(Long userId);
}
