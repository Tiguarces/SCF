package pl.scf.api.model.request;

import lombok.Data;

@Data
public class TopicSaveRequest {
    private Long forumUserId;
    private String topicName;
    private String description;
    private String categoryName;
    private String subCategoryName;
}
