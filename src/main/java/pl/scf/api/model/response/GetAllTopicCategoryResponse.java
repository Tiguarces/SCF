package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.TopicCategoryDTO;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GetAllTopicCategoryResponse extends Response{
    private List<TopicCategoryDTO> categories;
}
