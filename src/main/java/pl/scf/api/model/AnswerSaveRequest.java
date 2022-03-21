package pl.scf.api.model;

import lombok.Data;

import java.util.Date;

@Data
public class AnswerSaveRequest {
    private Date date;
    private Long userId;
    private Long topicId;
    private String content;
}
