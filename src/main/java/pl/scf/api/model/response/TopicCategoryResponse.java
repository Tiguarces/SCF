package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.model.TopicCategory;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TopicCategoryResponse extends Response{
    private TopicCategory category;
}
