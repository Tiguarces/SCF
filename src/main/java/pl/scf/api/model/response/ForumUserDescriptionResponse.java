package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.ForumUserDescriptionDTO;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ForumUserDescriptionResponse extends Response{
    private ForumUserDescriptionDTO forumUserDescription;
}
