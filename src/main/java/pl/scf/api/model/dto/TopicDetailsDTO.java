package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicDetailsDTO {
    private String topicName;
    private String description;
    private Long topicId;
}
