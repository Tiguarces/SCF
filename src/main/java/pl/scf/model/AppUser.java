package pl.scf.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
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

    @JsonManagedReference
    @OneToOne(fetch = EAGER, mappedBy = "user", cascade = ALL)
    private AppUserDetails user_details;

    @JsonManagedReference
    @ManyToOne(fetch = EAGER, cascade = ALL)
    private UserRole role;

    @JsonManagedReference
    @OneToOne(fetch = LAZY, mappedBy = "user", cascade = ALL)
    private VerificationToken token;
}
