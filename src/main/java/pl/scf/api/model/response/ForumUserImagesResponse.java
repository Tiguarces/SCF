package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.ForumUserImagesDTO;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ForumUserImagesResponse extends Response{
    private ForumUserImagesDTO forumUserImages;
}
