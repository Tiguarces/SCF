package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.ForumUserImagesDTO;
import pl.scf.api.model.request.ForumUserImagesUpdateRequest;
import pl.scf.api.model.response.ForumUserImagesResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.ForumUserImages;
import pl.scf.model.repositories.IForumUserImagesRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toForumUserImages;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserImagesService {
    private final IForumUserImagesRepository imagesRepository;
    private final String toMessageForumUserImagesWord = "ForumUserImages";

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final ForumUserImagesUpdateRequest request) {
        imagesRepository.findById(request.getUserImagesId()).ifPresentOrElse(
                (foundImages) -> {
                    foundImages.setAvatarImageURL(request.getAvatarImageURL());
                    foundImages.setBackgroundImageURL(request.getBackgroundImageURL());

                    log.info(UPDATE_MESSAGE, toMessageForumUserImagesWord, request.getUserImagesId());
                    imagesRepository.save(foundImages);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserImagesWord, request.getUserImagesId());
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
        imagesRepository.findById(id).ifPresentOrElse(
                (foundImages) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserImagesWord, id);
                    imagesRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserImagesWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    private ForumUserImagesResponse imagesByIdResponse;
    public final ForumUserImagesResponse getById(final Long id) {
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
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_FETCHING)
                            .forumUserImages(imagesDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserImagesWord, id);
                    imagesByIdResponse = ForumUserImagesResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_FETCHING)
                            .forumUserImages(null)
                            .build();
                }
        ); return imagesByIdResponse;
    }

    public final List<ForumUserImagesDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageForumUserImagesWord);
        return toForumUserImages(imagesRepository.findAll());
    }
}
