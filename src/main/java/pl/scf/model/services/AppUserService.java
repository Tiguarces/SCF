package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.scf.model.AppUser;
import pl.scf.model.repositories.IAppUserRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AppUserService {
    private final IAppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public final void save(final AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        log.info("Encoding AppUser password");

        log.info("Saving new AppUser");
        userRepository.save(appUser);
    }

    public final AppUser getById(final Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id).orElse(new AppUser());
    }

    public final AppUser getByUsername(final String username) {
        log.info("Fetching user with username: {}", username);
        return userRepository.findByUsername(username);
    }

    public final void update(final AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        log.info("Encoding new AppUser password");

        log.info("Updating AppUser with id: {}", appUser.getId());
        userRepository.save(appUser);
    }

    public final void delete(final Long id) {
        log.info("Deleting AppUser with id: {}", id);
        userRepository.deleteById(id);
    }

    public final List<AppUser> getAll() {
        log.info("Fetching all AppUsers");
        return userRepository.findAll();
    }
}
