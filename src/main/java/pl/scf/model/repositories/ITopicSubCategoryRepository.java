package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.TopicSubCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITopicSubCategoryRepository extends JpaRepository<TopicSubCategory, Long> {
    Optional<TopicSubCategory> findByName(String subCategoryName);

    List<TopicSubCategory> findAllByCategoryName(String categoryName);

    boolean existsByName(String subCategoryName);

    boolean existsByNameIn(String[] subCategoryNames);
}
