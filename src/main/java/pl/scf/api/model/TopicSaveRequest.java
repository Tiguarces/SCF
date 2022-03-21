package pl.scf.api.model;

import lombok.Data;

@Data
public class TopicSaveRequest {
    private Long forumUserId;
    private String topicName;
    private String description;
}
