package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForumUserDTO {
    private Integer reputation;
    private Integer visitors;
    private Long appUserId;
    private Long descriptionId;
    private Long imagesId;
    private String topicTitle;
}
