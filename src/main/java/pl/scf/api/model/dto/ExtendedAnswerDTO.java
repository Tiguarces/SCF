package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtendedAnswerDTO {
    private AnswerDTO answer;
    private String topicName;
    private String subCategoryName;
    private String categoryName;
}
