package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.AppUserDetailsUpdateRequest;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.AppUserDetails;
import pl.scf.model.repositories.IAppUserDetailsRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.ApiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserDetailsService {
    private final IAppUserDetailsRepository detailsRepository;
    private final String toMessageAnswerWord = "AppUserDetails";

    private AppUserDetails detailsById;
    public final AppUserDetails getById(final Long id) {
        detailsRepository.findById(id).ifPresentOrElse(
                (foundDetails) -> {
                    log.info(FETCH_BY_ID, toMessageAnswerWord, id);
                    detailsById = foundDetails;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, id);
                    detailsById = new AppUserDetails();
                }
        ); return detailsById;
    }

    private AppUserDetails detailsByUsername;
    public final AppUserDetails getByUsername(final String username) {
        detailsRepository.findByUserUsername(username).ifPresentOrElse(
                (foundDetails) -> {
                    log.info(FETCHING_BY_STH_MESSAGE, toMessageAnswerWord, "Username", username);
                    detailsByUsername = foundDetails;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_STH, toMessageAnswerWord, "Username", username);
                    detailsByUsername = new AppUserDetails();
                }
        );  return detailsByUsername;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final AppUserDetailsUpdateRequest userDetailsUpdateRequest) {
        final Long userId = userDetailsUpdateRequest.getAppUserDetailsUserId();
        detailsRepository.findById(userId).ifPresentOrElse(
                (foundUser) -> {
                    log.info(UPDATE_MESSAGE, toMessageAnswerWord, userId);

                    foundUser.setEmail(userDetailsUpdateRequest.getEmail());
                    detailsRepository.save(foundUser);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, userId);
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
        detailsRepository.findById(id).ifPresentOrElse(
                (foundAppUser) -> {
                    log.info(DELETING_MESSAGE, toMessageAnswerWord ,id);
                    detailsRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAnswerWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<AppUserDetails> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageAnswerWord);
        return detailsRepository.findAll();
    }
}
