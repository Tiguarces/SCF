package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.AnswerDTO;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AnswerResponse extends Response{
    private AnswerDTO answer;
}
