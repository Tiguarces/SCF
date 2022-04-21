package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY, cascade = ALL)
    private TopicDetails details;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToMany(fetch = LAZY, mappedBy = "topic")
    private Set<Answer> answers;

    private Instant createdDate;

    @JsonManagedReference
    @ManyToOne(fetch = LAZY)
    private ForumUser user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private TopicSubCategory subCategory;
}
