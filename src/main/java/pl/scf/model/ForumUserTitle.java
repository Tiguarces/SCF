package pl.scf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ForumUserTitle {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String titleName;

    @Column(name = "title_interval")
    private String rangeIntervalPoints;

    @OneToMany(fetch = LAZY, mappedBy = "title")
    private List<ForumUser> users;
}
