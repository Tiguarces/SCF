package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExtendedAppUserDTO {
    private String nickName;
    private String email;
    private String createdDate;
    private String role;
    private String descriptionContent;
    private String title;
    private String avatarImage;
    private String backgroundImage;
    private Integer visitors;
    private Integer reputation;

    private List<ExtendedTopicDetailsDTO> topics;
    private List<ExtendedAnswerDTO> answers;
}
