package pl.scf.api.model.request;

import lombok.Data;

@Data
public class TopicCategoryUpdateRequest {
    private Long topicId;
    private String imageURL;
    private String name;
}
