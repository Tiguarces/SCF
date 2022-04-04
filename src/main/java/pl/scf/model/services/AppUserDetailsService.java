package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.AppUserDetailsDTO;
import pl.scf.api.model.request.AppUserDetailsUpdateRequest;
import pl.scf.api.model.response.AppUserDetailsResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.repositories.IAppUserDetailsRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toDetails;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserDetailsService {
    private final IAppUserDetailsRepository detailsRepository;
    private final String toMessageDetailsWord = "AppUserDetails";

    private AppUserDetailsResponse detailsByIdResponse;
    public final AppUserDetailsResponse getById(final Long id) {
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
                            .date(new Date(System.currentTimeMillis()))
                            .success(true)
                            .message("Found UserDetails")
                            .details(detailsDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageDetailsWord, id);
                    detailsByIdResponse = AppUserDetailsResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(false)
                            .message(id != 0
                                    ? String.format(NOT_FOUND_MESSAGE, toMessageDetailsWord, "id", id)
                                    : ID_ERROR_MESSAGE)
                            .details(null)
                            .build();
                }
        ); return detailsByIdResponse;
    }

    private AppUserDetailsResponse detailsByUsernameResponse;
    public final AppUserDetailsResponse getByUsername(final String username) {
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
                            .date(new Date(System.currentTimeMillis()))
                            .success(true)
                            .message("Found UserDetails")
                            .details(detailsDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_STH, toMessageDetailsWord, "Username", username);
                    detailsByUsernameResponse = AppUserDetailsResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(false)
                            .message(String.format(NOT_FOUND_MESSAGE, toMessageDetailsWord, "username", username))
                            .details(null)
                            .build();
                }
        );  return detailsByUsernameResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final AppUserDetailsUpdateRequest userDetailsUpdateRequest) {
        final Long userId = userDetailsUpdateRequest.getAppUserDetailsUserId();
        detailsRepository.findById(userId).ifPresentOrElse(
                (foundUser) -> {
                    log.info(UPDATE_MESSAGE, toMessageDetailsWord, userId);

                    foundUser.setEmail(userDetailsUpdateRequest.getEmail());
                    detailsRepository.save(foundUser);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageDetailsWord, userId);
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
        detailsRepository.findById(id).ifPresentOrElse(
                (foundAppUser) -> {
                    log.info(DELETING_MESSAGE, toMessageDetailsWord,id);
                    detailsRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageDetailsWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<AppUserDetailsDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageDetailsWord);
        return toDetails(detailsRepository.findAll());
    }
}
