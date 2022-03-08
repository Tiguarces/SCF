package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer enabled;

    private String email;
    private Date createdDate;

    @JsonManagedReference
    @OneToOne(fetch = LAZY, cascade = ALL)
    private AppUser user;

    @OneToOne(fetch = LAZY, cascade = ALL)
    private ForumUser forumUser;
}
