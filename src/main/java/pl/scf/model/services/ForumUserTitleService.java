package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.ForumUserDTO;
import pl.scf.api.model.dto.ForumUserTitleDTO;
import pl.scf.api.model.request.ForumUserTitleSaveRequest;
import pl.scf.api.model.request.ForumUserTitleUpdateRequest;
import pl.scf.api.model.response.ForumUserTitleResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.ForumUser;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.repositories.IForumUserTitleRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toForumUser;
import static pl.scf.api.model.utils.DTOMapper.toForumUserTitle;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserTitleService {
    private final IForumUserTitleRepository titleRepository;
    private final String toMessageForumUserTitleWord = "ForumUserTitle";

    public final UniversalResponse save(final ForumUserTitleSaveRequest request) {
        try {
            final ForumUserTitle title = ForumUserTitle.builder()
                    .titleName(request.getTitleName())
                    .rangeIntervalPoints(request.getRangeIntervalPoints())
                    .build();

            log.info(SAVING, toMessageForumUserTitleWord);
            titleRepository.save(title);

            return UniversalResponse.builder()
                    .success(true)
                    .date(new Date(System.currentTimeMillis()))
                    .message(SUCCESS_SAVING)
                    .build();

        } catch (final Exception exception) {
            log.warn(NULLABLE_MESSAGE, "Request");
            return UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(FAIL_SAVING)
                    .build();
        }
    }

    private ForumUserTitleResponse userByIdResponse;
    public final ForumUserTitleResponse getById(final Long id) {
        titleRepository.findById(id).ifPresentOrElse(
                (foundTitle) -> {
                    log.info(FETCH_BY_ID, toMessageForumUserTitleWord, id);
                    final ForumUserTitleDTO titleDTO = ForumUserTitleDTO.builder()
                            .titleName(foundTitle.getTitleName())
                            .rangeIntervalPoints(foundTitle.getRangeIntervalPoints())
                            .build();

                    userByIdResponse = ForumUserTitleResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_FETCHING)
                            .title(titleDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserTitleWord, id);
                    userByIdResponse = ForumUserTitleResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_FETCHING)
                            .title(null)
                            .build();
                }
        ); return userByIdResponse;
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
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserTitleWord, request.getTitleId());
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
        titleRepository.findById(id).ifPresentOrElse(
                (foundTitle) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserTitleWord, id);
                    titleRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserTitleWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<ForumUserTitleDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageForumUserTitleWord);
        return toForumUserTitle(titleRepository.findAll());
    }

    public final List<ForumUserDTO> getAllByTitleName(final String titleName) {
        log.info(FETCHING_BY_STH_MESSAGE, toMessageForumUserTitleWord, "TitleName", titleName);
        return toForumUser(titleRepository.findAllUsersByTitleName(titleName));
    }

    public final Map<String, String> getAllTitlesWithIntervals() {
        log.info("Fetching all Titles with Intervals");
        final Map<String, String> data = new HashMap<>();

        titleRepository.findAll()
                .forEach(title -> data.put(title.getTitleName(), title.getRangeIntervalPoints()));
        return data;
    }
}
