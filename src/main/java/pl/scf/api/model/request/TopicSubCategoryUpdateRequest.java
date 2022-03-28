package pl.scf.api.model.request;

import lombok.Data;

@Data
public class TopicSubCategoryUpdateRequest {
    private Long topicCategoryId;
    private String name;
}
