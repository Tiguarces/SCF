package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.api.model.dto.TopicCategoryDTO;
import pl.scf.model.TopicCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITopicCategoryRepository extends JpaRepository<TopicCategory, Long> {
    Optional<TopicCategory> findByName(String categoryName);
}
