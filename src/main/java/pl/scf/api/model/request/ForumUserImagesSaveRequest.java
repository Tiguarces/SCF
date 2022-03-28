package pl.scf.api.model.request;

import lombok.Data;

@Data
public class ForumUserImagesSaveRequest {
    private String avatarImageURL;
    private String backgroundImageURL;
    private Long userId;
}
