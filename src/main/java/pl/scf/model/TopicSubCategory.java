package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicSubCategory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonBackReference
    @ManyToOne(fetch = LAZY, cascade = { MERGE, REMOVE, DETACH })
    private TopicCategory category;

    @OneToMany(fetch = LAZY, mappedBy = "subCategory")
    private List<Topic> topic;
}
