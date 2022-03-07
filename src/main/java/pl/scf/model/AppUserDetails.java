package pl.scf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
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

    @OneToOne(fetch = LAZY)
    private AppUser user;

    @OneToOne(fetch = LAZY)
    private ForumUser forumUser;
}
