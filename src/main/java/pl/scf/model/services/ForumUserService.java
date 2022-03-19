package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.ForumUser;
import pl.scf.model.repositories.IForumUserRepository;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ForumUserService {
    private final IForumUserRepository userRepository;
    private final String EXCEPTION_MESSAGE = "Error while deleting ForumUser with id: {} | Message: {}";

    public final ForumUser getByUsername(final String username) {
        log.info("Fetching ForumUser with username: {}", username);
        return userRepository.findByUserUsername(username).orElse(new ForumUser());
    }

    public final UniversalResponse update(final ForumUser forumUser) {
        try {
            log.info("Updating ForumUser with id: {}", forumUser.getId());
            userRepository.save(forumUser);

            return UniversalResponse.builder()
                    .success(true)
                    .date(new Date(System.currentTimeMillis()))
                    .response("Successful update")
                    .build();

        } catch (final Exception exception) {
            log.error(EXCEPTION_MESSAGE, forumUser.getId(), exception.getMessage());

        } return UniversalResponse.builder()
                .success(false)
                .date(new Date(System.currentTimeMillis()))
                .response("Fail while update")
                .build();
    }

    public final UniversalResponse delete(final Long id) {
        try {
            log.info("Deleting ForumUser with id: {}", id);
            userRepository.deleteById(id);

            return UniversalResponse.builder()
                    .success(true)
                    .date(new Date(System.currentTimeMillis()))
                    .response("Successfully deleting")
                    .build();

        } catch (final Exception exception) {
            log.error(EXCEPTION_MESSAGE, id, exception.getMessage());

        } return UniversalResponse.builder()
                .success(false)
                .date(new Date(System.currentTimeMillis()))
                .response("Fail while deleting")
                .build();
    }

    public final List<ForumUser> getAll() {
        log.info("Fetching all ForumUser");
        return userRepository.findAll();
    }
}
