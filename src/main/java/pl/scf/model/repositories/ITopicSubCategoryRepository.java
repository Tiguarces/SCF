package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.TopicSubCategory;

import java.util.Optional;

@Repository
public interface ITopicSubCategoryRepository extends JpaRepository<TopicSubCategory, Long> {
    Optional<TopicSubCategory> findByName(String subCategoryName);

    boolean existsByName(String subCategoryName);
}
