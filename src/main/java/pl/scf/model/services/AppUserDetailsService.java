package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.AppUserDetailsDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.AppUserDetailsUpdateRequest;
import pl.scf.api.model.response.AppUserDetailsResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.repositories.IAppUserDetailsRepository;

import java.time.Instant;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toAppUserDetails;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserDetailsService {
    private final IAppUserDetailsRepository detailsRepository;
    private final String toMessageDetailsWord = "AppUserDetails";

    private AppUserDetailsResponse detailsByIdResponse;
    public final AppUserDetailsResponse getById(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        detailsRepository.findById(id).ifPresentOrElse(
                (foundDetails) -> {
                    log.info(FETCH_BY_ID, toMessageDetailsWord, id);
                    final AppUserDetailsDTO detailsDTO = AppUserDetailsDTO.builder()
                            .appUserId(foundDetails.getUser().getId())
                            .email(foundDetails.getEmail())
                            .nickname(foundDetails.getNickname())
                            .createdDate(foundDetails.getCreatedDate())
                            .forumUserId(foundDetails.getForumUser().getId())
                            .build();

                    detailsByIdResponse = AppUserDetailsResponse.builder()
                            .date(Instant.now())
                            .success(true)
                            .message("Found UserDetails")
                            .details(detailsDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUserDetails with specified id");
                }
        ); return detailsByIdResponse;
    }

    private AppUserDetailsResponse detailsByUsernameResponse;
    public final AppUserDetailsResponse getByUsername(final String username) throws NotFoundException {
        detailsRepository.findByUserUsername(username).ifPresentOrElse(
                (foundDetails) -> {
                    log.info(FETCHING_BY_STH_MESSAGE, toMessageDetailsWord, "Username", username);
                    final AppUserDetailsDTO detailsDTO = AppUserDetailsDTO.builder()
                            .appUserId(foundDetails.getUser().getId())
                            .email(foundDetails.getEmail())
                            .nickname(foundDetails.getNickname())
                            .createdDate(foundDetails.getCreatedDate())
                            .forumUserId(foundDetails.getForumUser().getId())
                            .build();

                    detailsByUsernameResponse = AppUserDetailsResponse.builder()
                            .date(Instant.now())
                            .success(true)
                            .message("Found UserDetails")
                            .details(detailsDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUser with specified name");
                }
        );  return detailsByUsernameResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final AppUserDetailsUpdateRequest userDetailsUpdateRequest) throws NotFoundException, IdentificationException {
        final Long userId = userDetailsUpdateRequest.getAppUserDetailsUserId();
        throwExceptionWhenIdZero(userId);

        detailsRepository.findById(userId).ifPresentOrElse(
                (foundUser) -> {
                    log.info(UPDATE_MESSAGE, toMessageDetailsWord, userId);

                    foundUser.setEmail(userDetailsUpdateRequest.getEmail());
                    detailsRepository.save(foundUser);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUserDetails with specified id");
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        detailsRepository.findById(id).ifPresentOrElse(
                (foundAppUser) -> {
                    log.info(DELETING_MESSAGE, toMessageDetailsWord,id);
                    detailsRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUserDetails with specified id");
                }
        ); return deleteResponse;
    }

    public final List<AppUserDetailsDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageDetailsWord);
        return toAppUserDetails(detailsRepository.findAll());
    }
}
