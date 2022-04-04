package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForumUserTitleDTO {
    private String titleName;
    private String rangeIntervalPoints;
}
