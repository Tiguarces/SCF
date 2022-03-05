package pl.scf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ForumUser {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Integer reputation;
    private Integer visitors;

    @OneToOne(fetch = LAZY, mappedBy = "user_details")
    private AppUser user;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private Set<Answer> answers;

    @OneToOne(fetch = LAZY)
    private ForumUserDescription description;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private Set<Topic> topics;

    @ManyToOne(fetch = LAZY)
    private ForumUserTitle title;

    @OneToOne(fetch = LAZY)
    private ForumUserImages images;
}
