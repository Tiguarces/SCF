package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicSubCategoryDTO {
    private String name;
    private String topicNameCategory;
}
