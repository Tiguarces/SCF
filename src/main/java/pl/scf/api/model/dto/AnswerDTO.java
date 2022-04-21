package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AnswerDTO {
    private String content;
    private String createdDate;
    private Long forumUserId;
    private Long topicId;
}
