package pl.scf.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.scf.model.AppUserDetails;

@Repository
public interface IAppUserDetailsRepository extends JpaRepository<AppUserDetails, Long> {
}
