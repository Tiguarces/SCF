package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.ForumUserDTO;
import pl.scf.api.model.dto.ForumUserTitleDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.ForumUserTitleSaveRequest;
import pl.scf.api.model.request.ForumUserTitleUpdateRequest;
import pl.scf.api.model.response.ForumUserTitleResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.repositories.IForumUserTitleRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toForumUser;
import static pl.scf.api.model.utils.DTOMapper.toForumUserTitle;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserTitleService {
    private final IForumUserTitleRepository titleRepository;
    private final String toMessageForumUserTitleWord = "ForumUserTitle";

    public final UniversalResponse save(final ForumUserTitleSaveRequest request) {
        final ForumUserTitle title = ForumUserTitle.builder()
                .titleName(request.getTitleName())
                .rangeIntervalPoints(request.getRangeIntervalPoints())
                .build();

        log.info(SAVING, toMessageForumUserTitleWord);
        titleRepository.save(title);

        return UniversalResponse.builder()
                .success(true)
                .date(Instant.now())
                .message(SUCCESS_SAVING)
                .build();

    }

    private ForumUserTitleResponse userByIdResponse;
    public final ForumUserTitleResponse getById(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        titleRepository.findById(id).ifPresentOrElse(
                (foundTitle) -> {
                    log.info(FETCH_BY_ID, toMessageForumUserTitleWord, id);
                    final ForumUserTitleDTO titleDTO = ForumUserTitleDTO.builder()
                            .titleName(foundTitle.getTitleName())
                            .rangeIntervalPoints(foundTitle.getRangeIntervalPoints())
                            .build();

                    userByIdResponse = ForumUserTitleResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .title(titleDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserTitle with specified id");
                }
        ); return userByIdResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final ForumUserTitleUpdateRequest request) throws NotFoundException, IdentificationException{
        throwExceptionWhenIdZero(request.getTitleId());

        titleRepository.findById(request.getTitleId()).ifPresentOrElse(
                (foundTitle) -> {
                    log.info(UPDATE_MESSAGE, toMessageForumUserTitleWord, foundTitle.getId());

                    foundTitle.setTitleName(request.getTitleName());
                    foundTitle.setRangeIntervalPoints(request.getRangeIntervalPoints());
                    titleRepository.save(foundTitle);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserTitle with specified id");
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        titleRepository.findById(id).ifPresentOrElse(
                (foundTitle) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserTitleWord, id);
                    titleRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserTitle with specified id");
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
