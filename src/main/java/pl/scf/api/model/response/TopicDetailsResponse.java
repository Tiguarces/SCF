package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.TopicDetailsDTO;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TopicDetailsResponse extends Response{
    private TopicDetailsDTO detailsDTO;
}
