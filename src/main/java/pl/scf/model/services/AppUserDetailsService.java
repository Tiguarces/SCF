package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.model.AppUserDetails;
import pl.scf.model.repositories.IAppUserDetailsRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AppUserDetailsService {
    private final IAppUserDetailsRepository detailsRepository;

    public final void save(final AppUserDetails userDetails) {
        log.info("Saving new userDetails");
        detailsRepository.save(userDetails);
    }

    public final AppUserDetails getById(final Long id) {
        log.info("Fetching AppUserDetails with id: {}", id);
        return detailsRepository.findById(id).orElse(new AppUserDetails());
    }

    public final AppUserDetails getByUsername(final String username) {
        log.info("Fetching AppUserDetails with user username: {}", username);
        return detailsRepository.findByUserUsername(username).orElse(new AppUserDetails());
    }

    public final void update(final AppUserDetails userDetails) {
        log.info("Updating AppUserDetails with id: {}", userDetails.getId());
        detailsRepository.save(userDetails);
    }

    public final void delete(final Long id) {
        log.info("Deleting AppUserDetails with id: {}", id);
        detailsRepository.deleteById(id);
    }

    public final List<AppUserDetails> getAll() {
        log.info("Fetching all AppUserDetails");
        return detailsRepository.findAll();
    }
}
