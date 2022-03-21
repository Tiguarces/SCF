package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.ForumUser;
import pl.scf.model.repositories.IForumUserRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.ApiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserService {
    private final IForumUserRepository userRepository;
    private final String toMessageForumUserWord = "ForumUser";

    private ForumUser userByUsername;
    public final ForumUser getByUsername(final String username) {
        userRepository.findByUserUsername(username).ifPresentOrElse(
                (foundUser) -> {
                    log.info(FETCHING_BY_STH_MESSAGE, toMessageForumUserWord, "Username", username);
                    userByUsername = foundUser;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_STH, toMessageForumUserWord, "Username", username);
                    userByUsername = new ForumUser();
                }
        ); return userByUsername;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final ForumUser forumUser) {
        userRepository.findById(forumUser.getId()).ifPresentOrElse(
                (foundUser) -> {
                    log.info(UPDATE_MESSAGE, toMessageForumUserWord, forumUser.getId());
                    userRepository.save(forumUser);

                    updateResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserWord, forumUser.getId());
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
        userRepository.findById(id).ifPresentOrElse(
                (foundUser) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserWord, id);
                    userRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .response(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageForumUserWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .response(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<ForumUser> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageForumUserWord);
        return userRepository.findAll();
    }
}
