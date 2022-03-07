package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.model.ForumUser;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.repositories.IForumUserTitleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ForumUserTitleService {
    private final IForumUserTitleRepository titleRepository;

    public final void save(final ForumUserTitle forumUserTitle) {
        log.info("Saving new ForumUser");
        titleRepository.save(forumUserTitle);
    }

    public final ForumUserTitle getById(final Long id) {
        log.info("Fetching ForumUserTitle with id: {}", id);
        return titleRepository.findById(id).orElse(new ForumUserTitle());
    }

    public final void update(final ForumUserTitle forumUserTitle) {
        log.info("Updating ForumUserTitle with id: {}", forumUserTitle.getId());
        titleRepository.save(forumUserTitle);
    }

    public final void delete(final Long id) {
        log.info("Deleting ForumUser with id: {}", id);
        titleRepository.deleteById(id);
    }

    public final List<ForumUserTitle> getAll() {
        log.info("Fetching all ForumUserTitle");
        return titleRepository.findAll();
    }

    public final List<ForumUser> getAllByTitleName(final String titleName) {
        log.info("Fetching all ForumUsers by TitleName: {}", titleName);
        return titleRepository.findAllUsersByTitleName(titleName);
    }

    public final Map<String, String> getAllTitlesWithIntervals() {
        log.info("Fetching all Titles with Intervals");
        final Map<String, String> data = new HashMap<>();

        titleRepository.findAll().forEach(title -> data.put(title.getTitleName(), title.getRangeIntervalPoints()));
        return data;
    }
}
