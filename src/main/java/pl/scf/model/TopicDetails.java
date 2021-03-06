package pl.scf.model;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDetails {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String topicName;

    @OneToOne(mappedBy = "details", fetch = LAZY)
    private Topic topic;
}
