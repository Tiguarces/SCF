package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtendedTopicDetailsDTO {
    private String topicName;
    private String description;
    private String subCategoryName;
    private String categoryName;
    private String createdDate;
    private Long topicId;
}
