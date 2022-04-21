package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.ForumUserDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.response.ForumUserResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.repositories.IForumUserRepository;

import java.time.Instant;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toForumUsers;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumUserService {
    private final IForumUserRepository userRepository;
    private final String toMessageForumUserWord = "ForumUser";

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        userRepository.findById(id).ifPresentOrElse(
                (foundUser) -> {
                    log.info(DELETING_MESSAGE, toMessageForumUserWord, id);
                    userRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUser with specified id");
                }
        ); return deleteResponse;
    }

    private ForumUserResponse userByUsernameResponse;
    public final ForumUserResponse getByUsername(final String username) throws NotFoundException{
        userRepository.findByUserUsername(username).ifPresentOrElse(
                (foundUser) -> {
                    log.info(FETCHING_BY_STH_MESSAGE, toMessageForumUserWord, "Username", username);
                    final ForumUserDTO forumUserDTO = ForumUserDTO.builder()
                            .appUserId(foundUser.getUser().getId())
                            .descriptionId(foundUser.getDescription().getId())
                            .imagesId(foundUser.getImages().getId())
                            .reputation(foundUser.getReputation())
                            .visitors(foundUser.getVisitors())
                            .build();

                    userByUsernameResponse = ForumUserResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .forumUser(forumUserDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found ForumUser with specified username");
                }
        ); return userByUsernameResponse;
    }

    public final List<ForumUserDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageForumUserWord);
        return toForumUsers(userRepository.findAll());
    }
}
