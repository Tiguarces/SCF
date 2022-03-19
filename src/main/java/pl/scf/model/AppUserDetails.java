package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

import static javax.persistence.CascadeType.ALL;
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

    private String email;
    private Date createdDate;
    private String nickname;

    @JsonBackReference
    @OneToOne(cascade = ALL)
    private AppUser user;

    @JsonManagedReference
    @OneToOne(cascade = ALL)
    private ForumUser forumUser;
}
