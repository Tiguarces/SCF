package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.AppUserDetails;

import java.util.Optional;

@Repository
public interface IAppUserDetailsRepository extends JpaRepository<AppUserDetails, Long> {
    Optional<AppUserDetails> findByUserUsername(String username);
}
