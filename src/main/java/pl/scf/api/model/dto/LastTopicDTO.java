package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class LastTopicDTO {
    private String subCategoryName;
    private String avatarImageURL;
    private String userNickname;
    private String topicName;
    private String topicCategoryName;

    private String createdDate;
    private Long userId;

    private Integer numberOfAnswers;
}
