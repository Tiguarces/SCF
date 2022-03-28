package pl.scf.api.model.request;

import lombok.Data;

@Data
public class ForumUserDescriptionUpdateRequest {
    private Long userId;
    private String content;
}
