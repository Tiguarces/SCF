package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AppUserDetailsDTO {
    private String email;
    private String nickname;
    private Date createdDate;
    private Long appUserId;
    private Long forumUserId;
}
