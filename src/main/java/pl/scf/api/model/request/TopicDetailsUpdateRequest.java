package pl.scf.api.model.request;

import lombok.Data;

@Data
public class TopicDetailsUpdateRequest {
    private String description;
    private Long detailsId;
}
