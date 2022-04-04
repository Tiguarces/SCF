package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForumUserImagesDTO {
    private String avatarImageURL;
    private String backgroundImageURL;
    private Long forumUserId;
}
