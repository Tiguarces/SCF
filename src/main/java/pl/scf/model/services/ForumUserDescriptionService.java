package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.model.ForumUserDescription;
import pl.scf.model.repositories.IForumUserDescriptionRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ForumUserDescriptionService {
    private final IForumUserDescriptionRepository descriptionRepository;

    public final void save(final ForumUserDescription forumUserDescription) {
        log.info("Saving new ForumUserDescription");
        descriptionRepository.save(forumUserDescription);
    }

    public final ForumUserDescription getById(final Long id) {
        log.info("Fetching ForumUserDescription with id: {}", id);
        return descriptionRepository.findById(id).orElse(new ForumUserDescription());
    }

    public final void update(final ForumUserDescription forumUser) {
        log.info("Updating ForumUserDescription with id: {}", forumUser.getId());
        descriptionRepository.save(forumUser);
    }

    public final void delete(final Long id) {
        log.info("Deleting ForumUserDescription with id: {}", id);
        descriptionRepository.deleteById(id);
    }

    public final List<ForumUserDescription> getAll() {
        log.info("Fetching all ForumUserDescription");
        return descriptionRepository.findAll();
    }
}
