package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForumUserDescriptionDTO {
    private String content;
    private Long forumUserId;
}
