package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserRoles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = LAZY, mappedBy = "role", cascade = MERGE)
    private Set<AppUser> user;
}
