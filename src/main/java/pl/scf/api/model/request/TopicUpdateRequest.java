package pl.scf.api.model.request;

import lombok.Data;

@Data
public class TopicUpdateRequest {
    private Long topicId;
    private String description;
}
