package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.ForumUserImagesDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.ForumUserImagesUpdateRequest;
import pl.scf.api.model.response.ForumUserImagesResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.repositories.IForumUserImagesRepository;

import java.time.Instant;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toForumUserImages;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserImagesService {
    private final IForumUserImagesRepository imagesRepository;
    private final String toMessageForumUserImagesWord = "ForumUserImages";

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final ForumUserImagesUpdateRequest request) throws NotFoundException, IdentificationException{
        throwExceptionWhenIdZero(request.getUserImagesId());

        imagesRepository.findById(request.getUserImagesId()).ifPresentOrElse(
                (foundImages) -> {
                    foundImages.setAvatarImageURL(request.getAvatarImageURL());
                    foundImages.setBackgroundImageURL(request.getBackgroundImageURL());

                    log.info(UPDATE_MESSAGE, toMessageForumUserImagesWord, request.getUserImagesId());
                    imagesRepository.save(foundImages);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserImages with specified id");
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        imagesRepository.findById(id).ifPresentOrElse(
                (foundImages) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserImagesWord, id);
                    imagesRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserImage with specified id");
                }
        ); return deleteResponse;
    }

    private ForumUserImagesResponse imagesByIdResponse;
    public final ForumUserImagesResponse getById(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        imagesRepository.findById(id).ifPresentOrElse(
                (foundImages) -> {
                    log.info(FETCH_BY_ID, toMessageForumUserImagesWord, id);
                    final ForumUserImagesDTO imagesDTO = ForumUserImagesDTO.builder()
                            .forumUserId(foundImages.getUser().getId())
                            .avatarImageURL(foundImages.getAvatarImageURL())
                            .backgroundImageURL(foundImages.getBackgroundImageURL())
                            .build();

                    imagesByIdResponse = ForumUserImagesResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .forumUserImages(imagesDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUserImage with specified id");
                }
        ); return imagesByIdResponse;
    }

    public final List<ForumUserImagesDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageForumUserImagesWord);
        return toForumUserImages(imagesRepository.findAll());
    }
}
