package pl.scf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumUser {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Integer reputation;
    private Integer visitors;

    @OneToOne(fetch = LAZY, mappedBy = "user_details", cascade = ALL)
    private AppUser user;

    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = ALL)
    private Set<Answer> answers;

    @OneToOne(fetch = LAZY, cascade = ALL)
    private ForumUserDescription description;

    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = ALL)
    private Set<Topic> topics;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private ForumUserTitle title;

    @OneToOne(fetch = LAZY, cascade = ALL)
    private ForumUserImages images;
}
