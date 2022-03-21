package pl.scf.api.model;

import lombok.Data;

@Data
public class ForumUserTitleUpdateRequest {
    private Long titleId;
    private String titleName;
    private String rangeIntervalPoints;
}
