package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicCategory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String imageURL;

    @JsonManagedReference
    @OneToMany(fetch = LAZY, mappedBy = "category", cascade = { MERGE, REMOVE, DETACH })
    private List<TopicSubCategory> subCategory;
}
