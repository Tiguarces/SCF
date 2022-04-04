package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.api.model.dto.TopicDetailsDTO;
import pl.scf.model.TopicDetails;

import java.util.List;

@Repository
public interface ITopicDetailsRepository extends JpaRepository<TopicDetails, Long> {
    List<TopicDetailsDTO> findTopByOrderByIdDesc();
}
