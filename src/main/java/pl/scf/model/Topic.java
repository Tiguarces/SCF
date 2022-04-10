package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Topic {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY, cascade = ALL)
    private TopicDetails details;

    @JsonManagedReference
    @OneToMany(fetch = LAZY, mappedBy = "topic")
    private Set<Answer> answers;

    @JsonManagedReference
    @ManyToOne(fetch = LAZY)
    private ForumUser user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private TopicSubCategory subCategory;
}
