package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    private String content;

    private Date createdDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @ManyToOne(fetch = LAZY)
    private Topic topic;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @ManyToOne(fetch = LAZY)
    private ForumUser user;
}
