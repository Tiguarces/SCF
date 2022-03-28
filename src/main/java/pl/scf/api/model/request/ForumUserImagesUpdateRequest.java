package pl.scf.api.model.request;

import lombok.Data;

@Data
public class ForumUserImagesUpdateRequest {
    private Long userImagesId;
    private String avatarImageURL;
    private String backgroundImageURL;
}
