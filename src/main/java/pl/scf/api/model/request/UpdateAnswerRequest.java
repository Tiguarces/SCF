package pl.scf.api.model.request;

import lombok.Data;

@Data
public class UpdateAnswerRequest {
    private Long answerId;
    private String content;
}
