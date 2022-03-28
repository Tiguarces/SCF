package pl.scf.api.model.request;

import lombok.Data;

@Data
public class TopicSubCategorySaveRequest {
    private String name;
    private String categoryName;
}
