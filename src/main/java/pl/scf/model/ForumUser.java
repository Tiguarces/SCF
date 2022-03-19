package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @JsonBackReference
    @OneToOne(fetch = LAZY, mappedBy = "user_details", cascade = ALL)
    private AppUser user;

    @JsonBackReference
    @OneToMany(fetch = EAGER, mappedBy = "user", cascade = ALL)
    private Set<Answer> answers;

    @JsonManagedReference
    @OneToOne(cascade = ALL)
    private ForumUserDescription description;

    @JsonBackReference
    @OneToMany(fetch = EAGER, mappedBy = "user", cascade = ALL)
    private Set<Topic> topics;

    @JsonBackReference
    @ManyToOne(fetch = EAGER, cascade = ALL)
    private ForumUserTitle title;

    @JsonManagedReference
    @OneToOne(cascade = ALL)
    private ForumUserImages images;
}
