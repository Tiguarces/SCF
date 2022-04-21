package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @OneToOne(fetch = LAZY, mappedBy = "user_details", cascade = ALL)
    private AppUser user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @OneToMany(fetch = EAGER, mappedBy = "user", cascade = ALL)
    private Set<Answer> answers;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToOne(cascade = ALL)
    private ForumUserDescription description;

    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = EAGER, mappedBy = "user", cascade = ALL)
    private Set<Topic> topics;

    @JsonBackReference
    @ManyToOne(fetch = EAGER, cascade = ALL)
    private ForumUserTitle title;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToOne(cascade = ALL)
    private ForumUserImages images;
}
