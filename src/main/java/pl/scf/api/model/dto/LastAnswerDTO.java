package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LastAnswerDTO {
    private String content;
    private String topicName;
    private String topicCategory;
    private String topicSubCategory;
    private String userNickname;
    private String avatarImageURL;

    private Date createdDate;
    private Long topicId;
}
