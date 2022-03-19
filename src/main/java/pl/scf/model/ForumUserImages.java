package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ForumUserImages {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "avatarURL")
    private String avatarImageURL;

    @Column(name = "backgroundURL")
    private String backgroundImageURL;

    @JsonBackReference
    @OneToOne(fetch = LAZY, mappedBy = "user")
    private ForumUser user;
}
