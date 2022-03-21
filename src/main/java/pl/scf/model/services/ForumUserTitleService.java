package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.ForumUserTitleSaveRequest;
import pl.scf.api.model.ForumUserTitleUpdateRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.ForumUser;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.repositories.IForumUserTitleRepository;

import java.util.*;

import static pl.scf.api.ApiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserTitleService {
    private final IForumUserTitleRepository titleRepository;
    private final String toMessageForumUserTitleWord = "ForumUserTitle";

    private UniversalResponse saveResponse;
    public final UniversalResponse save(final ForumUserTitleSaveRequest request) {
        Optional.ofNullable(request).ifPresentOrElse(
                (validRequest) -> {
                    final ForumUserTitle title = ForumUserTitle.builder()
                            .titleName(validRequest.getTitleName())
                            .rangeIntervalPoints(validRequest.getRangeIntervalPoints())
                            .build();

                    log.info(SAVING, toMessageForumUserTitleWord);
                    titleRepository.save(title);

                    saveResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(SUCCESS_SAVING)
                            .build();
                },
                () -> {
                    log.warn(NULLABLE_MESSAGE, "Request");
                    saveResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .response(FAIL_SAVING)
                            .build();
                }
        ); return saveResponse;
    }

    private ForumUserTitle forumUserTitleById;
    public final ForumUserTitle getById(final Long id) {
        titleRepository.findById(id).ifPresentOrElse(
                (foundTitle) -> {
                    log.info(FETCH_BY_ID, toMessageForumUserTitleWord, id);
                    forumUserTitleById = foundTitle;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserTitleWord, id);
                    forumUserTitleById = new ForumUserTitle();
                }
        ); return forumUserTitleById;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final ForumUserTitleUpdateRequest request) {
        titleRepository.findById(request.getTitleId()).ifPresentOrElse(
                (foundTitle) -> {
                    log.info(UPDATE_MESSAGE, toMessageForumUserTitleWord, foundTitle.getId());

                    foundTitle.setTitleName(request.getTitleName());
                    foundTitle.setRangeIntervalPoints(request.getRangeIntervalPoints());
                    titleRepository.save(foundTitle);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserTitleWord, request.getTitleId());
                    updateResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .response(FAIL_UPDATE)
                            .build();
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        titleRepository.findById(id).ifPresentOrElse(
                (foundTitle) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserTitleWord, id);
                    titleRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserTitleWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .response(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<ForumUserTitle> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageForumUserTitleWord);
        return titleRepository.findAll();
    }

    public final List<ForumUser> getAllByTitleName(final String titleName) {
        log.info(FETCHING_BY_STH_MESSAGE, toMessageForumUserTitleWord, "TitleName", titleName);
        return titleRepository.findAllUsersByTitleName(titleName);
    }

    public final Map<String, String> getAllTitlesWithIntervals() {
        log.info("Fetching all Titles with Intervals");
        final Map<String, String> data = new HashMap<>();

        titleRepository
                .findAll()
                .forEach(title -> data.put(title.getTitleName(), title.getRangeIntervalPoints()));
        return data;
    }
}
