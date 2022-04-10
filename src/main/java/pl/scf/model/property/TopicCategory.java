package pl.scf.model.property;

import lombok.Data;

@Data
public class TopicCategory {
    private String categoryName;
    private String imageURL;
    private String[] subCategoryNames;
}
