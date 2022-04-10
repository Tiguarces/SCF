package pl.scf.api.model.request;

import lombok.Data;

@Data
public class TopicCategorySaveRequest {
    private String name;
    private String imageURL;
    private String subCategoryName;
}
