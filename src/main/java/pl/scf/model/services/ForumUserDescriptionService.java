package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.request.ForumUserDescriptionUpdateRequest;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.ForumUserDescription;
import pl.scf.model.repositories.IForumUserDescriptionRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserDescriptionService {
    private final IForumUserDescriptionRepository descriptionRepository;
    private final String toMessageForumUserDescriptionWord = "ForumUserDescription";

    private ForumUserDescription forumDescriptionById;
    public final ForumUserDescription getById(final Long id) {
        descriptionRepository.findById(id).ifPresentOrElse(
                (foundDescription) -> {
                    log.info(FETCH_BY_ID, toMessageForumUserDescriptionWord, id);
                    forumDescriptionById = foundDescription;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserDescriptionWord, id);
                    forumDescriptionById = new ForumUserDescription();
                }
        ); return forumDescriptionById;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final ForumUserDescriptionUpdateRequest request) {
        descriptionRepository.findById(request.getUserId()).ifPresentOrElse(
                (foundDescription) -> {
                    log.info(UPDATE_MESSAGE, toMessageForumUserDescriptionWord, request.getUserId());
                    foundDescription.setContent(request.getContent());

                    descriptionRepository.save(foundDescription);
                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserDescriptionWord, request.getUserId());
                    updateResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_UPDATE)
                            .build();
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        descriptionRepository.findById(id).ifPresentOrElse(
                (foundDescription) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserDescriptionWord, id);
                    descriptionRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserDescriptionWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<ForumUserDescription> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageForumUserDescriptionWord);
        return descriptionRepository.findAll();
    }
}
