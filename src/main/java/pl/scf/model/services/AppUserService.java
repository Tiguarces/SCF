package pl.scf.model.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.AppUserDTO;
import pl.scf.api.model.request.LoginRequest;
import pl.scf.api.model.request.RegisterRequest;
import pl.scf.api.model.request.UpdateUserRequest;
import pl.scf.api.model.response.ActivateEmailResponse;
import pl.scf.api.model.response.AppUserResponse;
import pl.scf.api.model.response.LoginResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.*;
import pl.scf.model.mail.MailNotification;
import pl.scf.model.mail.MailService;
import pl.scf.model.property.EmailProperty;
import pl.scf.model.property.JWTProperty;
import pl.scf.model.repositories.*;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toAppUser;
import static pl.scf.api.model.utils.ResponseUtil.messageByIdError;
import static pl.scf.api.model.utils.ResponseUtil.messageBySthError;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserService {
    private final IAppUserRepository userRepository;
    private final IUserRoleRepository roleRepository;
    private final IAppUserDetailsRepository detailsRepository;
    private final IForumUserTitleRepository titleRepository;
    private final IVerificationTokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JavaMailSender mailSender;
    private final EmailProperty emailProperty;
    private final String toMessageAppUserWord = "AppUser";
    private final JWTProperty jwtProperty;

    public LoginResponse login(LoginRequest request) {
        try {
            final UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            final Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            final User user = (User) authentication.getPrincipal();
            final Algorithm algorithm = Algorithm.HMAC512(jwtProperty.getSecret_password().getBytes(UTF_8));
            final List<String> roles = user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority).toList();

            String accessToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperty.getExpired_time()))
                    .withIssuer(jwtProperty.getIssuer())
                    .withClaim("roles", roles)
                    .sign(algorithm);

            String refreshToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperty.getExpired_time() * 2))
                    .withIssuer(jwtProperty.getIssuer())
                    .sign(algorithm);

            log.info("Success authenticate user");
            return LoginResponse.builder()
                    .success(true)
                    .date(new Date(System.currentTimeMillis()))
                    .message("Success logging user")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .username(user.getUsername())
                    .build();

        } catch (final AuthenticationException exception) {
            log.warn("Fail authenticated user");
            return LoginResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message("Failure logging user")
                    .accessToken("")
                    .refreshToken("")
                    .build();
        }
    }

    public final UniversalResponse logoutUser() {
        SecurityContextHolder.clearContext();
        return UniversalResponse.builder()
                .success(true)
                .date(new Date(System.currentTimeMillis()))
                .message("Success logout user")
                .build();
    }

    public final UniversalResponse register(final RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("User with username {} exists, skipping adding", request.getUsername());
            return UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message("Użytkownik już istnieje")
                    .build();
        }

        if (detailsRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("User with email {} exists, skipping adding", request.getEmail());
            return UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message("Email został już wykorzystany")
                    .build();
        }

        if (detailsRepository.findByNickname(request.getNickname()).isPresent()) {
            log.warn("User with nickname {} exists, skipping adding", request.getNickname());
            return UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message("Nick zajęty")
                    .build();
        }

        final UserRole role = roleRepository.findById(request.getRoleId()).orElse(new UserRole(null, "ROLE_USER", null));
        final AppUser appUser = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        final ForumUserTitle beginnerTitle = titleRepository.findById(1L).orElse(new ForumUserTitle(null, "0-0", "Nie określone", null));
        final ForumUser forumUser = ForumUser.builder()
                .user(appUser)
                .description(new ForumUserDescription())
                .images(new ForumUserImages())
                .reputation(0)
                .title(beginnerTitle)
                .visitors(0)
                .build();

        final AppUserDetails userDetails = AppUserDetails.builder()
                .user(appUser)
                .forumUser(forumUser)
                .createdDate(new Date(System.currentTimeMillis()))
                .email(request.getEmail())
                .nickname(request.getNickname())
                .build();

        final VerificationToken verificationToken = VerificationToken.builder()
                .user(appUser)
                .token(generateVerificationToken(appUser.getUsername()))
                .activated(0)
                .build();

        appUser.setUser_details(userDetails);
        appUser.setToken(verificationToken);

        final boolean isSent = mailService.sendEmail(new MailNotification(emailProperty.getEmail_subject(), userDetails.getEmail(), emailProperty.getEmail_from(), emailProperty.getEmail_content(),
                verificationToken.getToken(), appUser.getUsername()), mailSender);

        if(isSent) {
            log.info(SUCCESS_SAVING);
            userRepository.save(appUser);
            return UniversalResponse.builder()
                    .success(true)
                    .date(new Date(System.currentTimeMillis()))
                    .message(SUCCESS_SEND_EMAIL)
                    .build();
        } else {
            log.warn(FAIL_SAVING);
            return UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(FAIL_SEND_EMAIL)
                    .build();
        }
    }

    private String generateVerificationToken(final String username) {
        final String password = emailProperty.getSecret_password().concat(username);
        return UUID.nameUUIDFromBytes(password.getBytes(UTF_8)).toString();
    }

    private ActivateEmailResponse emailResponse;
    public final ActivateEmailResponse activateAccount(final String token) {
        tokenRepository.findByToken(token).ifPresentOrElse(
                (foundToken) -> {
                    boolean activated = false;

                    if(foundToken.getActivated() == 0) {
                        foundToken.setActivated(1);
                        log.info("Account {} activated", foundToken.getUser().getUsername());

                        activated = true;
                        tokenRepository.save(foundToken);
                    } else {
                        log.info("Account {} has activated yet", foundToken.getUser().getUsername());
                    }

                    final String nickname = foundToken.getUser()
                            .getUser_details().getNickname();

                    emailResponse = ActivateEmailResponse.builder()
                            .activated(activated)
                            .success(true)
                            .nickname(nickname)
                            .message(SUCCESS_ACTIVATION_EMAIL)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                },
                () -> emailResponse = ActivateEmailResponse.builder()
                        .activated(false)
                        .success(false)
                        .message(FAIL_ACTIVATION_EMAIL)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        ); return emailResponse;
    }

    private AppUserResponse userByIdResponse;
    public final AppUserResponse getById(final Long id) {
        userRepository.findById(id).ifPresentOrElse(
                (foundUser) -> {
                    log.info(FETCH_BY_ID, toMessageAppUserWord, id);
                    final AppUserDTO userDTO = AppUserDTO.builder()
                            .detailsId(foundUser.getUser_details().getId())
                            .username(foundUser.getUsername())
                            .roleName(foundUser.getRole().getName())
                            .verTokenId(foundUser.getToken().getId())
                            .build();

                    userByIdResponse = AppUserResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(true)
                            .message("Found AppUser")
                            .user(userDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAppUserWord, id);
                    userByIdResponse = AppUserResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(false)
                            .message(messageByIdError(id, toMessageAppUserWord))
                            .user(null)
                            .build();
                }
        ); return userByIdResponse;
    }

    private AppUserResponse userByUsernameResponse;
    public final AppUserResponse getByUsername(final String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                (foundUser) -> {
                    log.info(FETCHING_BY_STH_MESSAGE, toMessageAppUserWord, "Username", username);
                    final AppUserDTO userDTO = AppUserDTO.builder()
                            .detailsId(foundUser.getUser_details().getId())
                            .username(foundUser.getUsername())
                            .roleName(foundUser.getRole().getName())
                            .verTokenId(foundUser.getToken().getId())
                            .build();

                    userByUsernameResponse = AppUserResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(true)
                            .message("Found AppUser")
                            .user(userDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_STH, toMessageAppUserWord, "Username", username);
                    userByUsernameResponse = AppUserResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(false)
                            .message(messageBySthError("username", username, toMessageAppUserWord))
                            .user(null)
                            .build();
                }
        ); return  userByUsernameResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final UpdateUserRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresentOrElse(
                (value) -> {
                    request.setPassword(passwordEncoder.encode(request.getPassword()));
                    log.info("Encoding new AppUser password");

                    log.info(UPDATE_MESSAGE, toMessageAppUserWord, value.getId());
                    value.setPassword(request.getPassword());
                    userRepository.save(value);

                    response = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_UPDATE)
                            .success(true)
                            .build();
                },
                () -> updateResponse = UniversalResponse.builder()
                        .date(new Date(System.currentTimeMillis()))
                        .message(FAIL_UPDATE)
                        .success(false)
                        .build()
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        userRepository.findById(id).ifPresentOrElse(
                (foundUser) -> {
                    log.info(DELETING_MESSAGE, toMessageAppUserWord, id);
                    userRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_DELETE)
                            .success(true)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAppUserWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_DELETE)
                            .success(false)
                            .build();
                }
        ); return deleteResponse;
    }

    private UniversalResponse response;
    public final UniversalResponse sendEmailAgain(final Long userId) {
        userRepository.findById(userId).ifPresentOrElse(
                (value) -> {
                    final int randomValue = new Random().nextInt(value.getUsername().length()*2);
                    final String newToken = generateVerificationToken(value.getUsername().concat("sendAgain" + randomValue));

                    final AppUserDetails userDetails = value.getUser_details();
                    final VerificationToken verificationToken = value.getToken();

                    verificationToken.setToken(newToken);
                    tokenRepository.save(verificationToken);
                    response = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_SEND_EMAIL_AGAIN)
                            .success(true)
                            .build();

                    log.info("Update verification email, sending email again");
                    mailService.sendEmail(new MailNotification(emailProperty.getEmail_subject(), userDetails.getEmail(), emailProperty.getEmail_from(), emailProperty.getEmail_content(),
                            verificationToken.getToken(), value.getUsername()), mailSender);
                },
                () -> response = UniversalResponse.builder()
                        .date(new Date(System.currentTimeMillis()))
                        .message(FAIL_SEND_EMAIL_AGAIN)
                        .success(false)
                        .build()
        ); return response;
    }

    public final List<AppUserDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageAppUserWord);
        return toAppUser(userRepository.findAll());
    }

    public UniversalResponse deleteAll() {
        log.info(DELETE_ALL, toMessageAppUserWord);
        userRepository.deleteAll();
        return UniversalResponse.builder()
                .success(true)
                .date(new Date(System.currentTimeMillis()))
                .message(SUCCESS_DELETE)
                .build();
    }
}
