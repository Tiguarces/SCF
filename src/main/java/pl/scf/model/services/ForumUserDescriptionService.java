package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.ForumUserDescriptionDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.ForumUserDescriptionUpdateRequest;
import pl.scf.api.model.response.ForumUserDescriptionResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.repositories.IForumUserDescriptionRepository;

import java.time.Instant;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toForumUserDescription;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserDescriptionService {
    private final IForumUserDescriptionRepository descriptionRepository;
    private final String toMessageForumUserDescriptionWord = "ForumUserDescription";

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final ForumUserDescriptionUpdateRequest request) throws NotFoundException, IdentificationException{
        throwExceptionWhenIdZero(request.getUserId());

        descriptionRepository.findById(request.getUserId()).ifPresentOrElse(
                (foundDescription) -> {
                    log.info(UPDATE_MESSAGE, toMessageForumUserDescriptionWord, request.getUserId());
                    foundDescription.setContent(request.getContent());

                    descriptionRepository.save(foundDescription);
                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserDescription with specified User id");
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        descriptionRepository.findById(id).ifPresentOrElse(
                (foundDescription) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserDescriptionWord, id);
                    descriptionRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserDescription with specified id");
                }
        ); return deleteResponse;
    }

    private ForumUserDescriptionResponse descriptionByIdResponse;
    public final ForumUserDescriptionResponse getById(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        descriptionRepository.findById(id).ifPresentOrElse(
                (foundDescription) -> {
                    log.info(FETCH_BY_ID, toMessageForumUserDescriptionWord, id);
                    final ForumUserDescriptionDTO descriptionDTO = ForumUserDescriptionDTO.builder()
                            .content(foundDescription.getContent())
                            .forumUserId(foundDescription.getUser().getId())
                            .build();

                    descriptionByIdResponse = ForumUserDescriptionResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .forumUserDescription(descriptionDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserDescription with specified id");
                }
        ); return descriptionByIdResponse;
    }

    public final List<ForumUserDescriptionDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageForumUserDescriptionWord);
        return toForumUserDescription(descriptionRepository.findAll());
    }
}
