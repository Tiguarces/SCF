package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.model.ForumUserImages;
import pl.scf.model.repositories.IForumUserImagesRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ForumUserImagesSerivce {
    private final IForumUserImagesRepository imagesRepository;

    public final void save(final ForumUserImages userImages) {
        log.info("Saving new ForumUserImages");
        imagesRepository.save(userImages);
    }

    public final ForumUserImages getById(final Long id) {
        log.info("Fetching ForumUserImages with id: {}", id);
        return imagesRepository.findById(id).orElse(new ForumUserImages());
    }

    public final void update(final ForumUserImages userImages) {
        log.info("Updating ForumUserImages with id: {}", userImages.getId());
        imagesRepository.save(userImages);
    }

    public final void delete(final Long id) {
        log.info("Deleting ForumUserImages with id: {}", id);
        imagesRepository.deleteById(id);
    }

    public final List<ForumUserImages> getAll() {
        log.info("Fetching all ForumUserImages");
        return imagesRepository.findAll();
    }
}
