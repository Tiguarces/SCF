package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AppUserDetailsDTO {
    private String email;
    private String nickname;
    private Instant createdDate;
    private Long appUserId;
    private Long forumUserId;
}
