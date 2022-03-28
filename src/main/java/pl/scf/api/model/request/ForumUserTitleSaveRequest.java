package pl.scf.api.model.request;

import lombok.Data;

@Data
public class ForumUserTitleSaveRequest {
    private String titleName;
    private String rangeIntervalPoints;
}
