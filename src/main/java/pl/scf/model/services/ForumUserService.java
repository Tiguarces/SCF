package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.model.ForumUser;
import pl.scf.model.repositories.IForumUserRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ForumUserService {
    private final IForumUserRepository userRepository;

    public final void save(final ForumUser forumUser) {
        log.info("Saving new ForumUser");
        userRepository.save(forumUser);
    }

    public final ForumUser getById(final Long id) {
        log.info("Fetching ForumUser with id: {}", id);
        return userRepository.findById(id).orElse(new ForumUser());
    }

    public final ForumUser getByUsername(final String username) {
        log.info("Fetching ForumUser with username: {}", username);
        return userRepository.findByUserUsername(username).orElse(new ForumUser());
    }

    public final void update(final ForumUser forumUser) {
        log.info("Updating ForumUser with id: {}", forumUser.getId());
        userRepository.save(forumUser);
    }

    public final void delete(final Long id) {
        log.info("Deleting ForumUser with id: {}", id);
        userRepository.deleteById(id);
    }

    public final List<ForumUser> getAll() {
        log.info("Fetching all ForumUser");
        return userRepository.findAll();
    }
}
