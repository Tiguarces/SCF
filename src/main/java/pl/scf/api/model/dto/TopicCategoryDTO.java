package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicCategoryDTO {
    private String categoryName;
    private String[] subCategoryNames;
}
