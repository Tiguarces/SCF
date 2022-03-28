package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.model.TopicSubCategory;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TopicSubCategoryResponse extends Response{
    private TopicSubCategory subCategory;
}
