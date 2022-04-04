package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.ForumUserDTO;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ForumUserResponse extends Response{
    private ForumUserDTO forumUser;
}
