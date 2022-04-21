package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String username;
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = ALL)
    private AppUserDetails user_details;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @ManyToOne(fetch = EAGER, cascade = MERGE)
    private UserRole role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @OneToOne(fetch = LAZY, mappedBy = "user", cascade = ALL)
    private VerificationToken token;
}
