package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicDTO {
    private Long userId;
    private Long detailsId;
    private String subCategoryName;
}
