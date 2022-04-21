package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @OneToOne(fetch = LAZY, mappedBy = "user")
    private ForumUser user;
}
