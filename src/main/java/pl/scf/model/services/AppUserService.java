package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.scf.api.model.ActivateEmailResponse;
import pl.scf.api.model.RegisterResponse;
import pl.scf.api.model.UniversalResponse;
import pl.scf.api.model.UpdateUserRequest;
import pl.scf.model.*;
import pl.scf.model.mail.MailNotification;
import pl.scf.model.mail.MailService;
import pl.scf.model.property.EmailProperty;
import pl.scf.model.repositories.*;
import pl.scf.model.requests.RegisterRequest;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static pl.scf.api.ApiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserService {
    private final IAppUserRepository userRepository;
    private final IUserRoleRepository roleRepository;
    private final IAppUserDetailsRepository detailsRepository;
    private final IForumUserTitleRepository titleRepository;
    private final IVerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JavaMailSender mailSender;
    private final EmailProperty emailProperty;
    private final String toMessageAppUserWord = "AppUser";

//    public AppUserService(IAppUserRepository userRepository, IUserRoleRepository roleRepository, IAppUserDetailsRepository detailsRepository, IForumUserTitleRepository titleRepository,
//                          IVerificationTokenRepository tokenRepository, PasswordEncoder passwordEncoder, MailService mailService, JavaMailSender mailSender, EmailProperty emailProperty) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.detailsRepository = detailsRepository;
//        this.titleRepository = titleRepository;
//        this.tokenRepository = tokenRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.mailService = mailService;
//        this.mailSender = mailSender;
//        this.emailProperty = emailProperty;
//    }

    public final RegisterResponse register(final RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("User with username {} exists, skipping adding", request.getUsername());
            return RegisterResponse.builder()
                    .created(false)
                    .date(new Date(System.currentTimeMillis()))
                    .serverResponse("Username exists")
                    .build();
        }

        if (detailsRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("User with email {} exists, skipping adding", request.getEmail());
            return RegisterResponse.builder()
                    .created(false)
                    .date(new Date(System.currentTimeMillis()))
                    .serverResponse("Email exists")
                    .build();
        }

        if (detailsRepository.findByNickname(request.getNickname()).isPresent()) {
            log.warn("User with nickname {} exists, skipping adding", request.getNickname());
            return RegisterResponse.builder()
                    .created(false)
                    .date(new Date(System.currentTimeMillis()))
                    .serverResponse("Nickname exists")
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
            return RegisterResponse.builder()
                    .created(false)
                    .date(new Date(System.currentTimeMillis()))
                    .serverResponse(SUCCESS_SEND_EMAIL)
                    .build();
        } else {
            log.warn(FAIL_SAVING);
            return RegisterResponse.builder()
                    .created(true)
                    .date(new Date(System.currentTimeMillis()))
                    .serverResponse(FAIL_SEND_EMAIL)
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
                            .nickname(nickname)
                            .response(SUCCESS_ACTIVATION_EMAIL)
                            .date(new Date(System.currentTimeMillis()))
                            .build();
                },
                () -> emailResponse = ActivateEmailResponse.builder()
                        .activated(false)
                        .response(FAIL_ACTIVATION_EMAIL)
                        .date(new Date(System.currentTimeMillis()))
                        .build()
        ); return emailResponse;
    }

    private AppUser appUserById;
    public final AppUser getById(final Long id) {
        userRepository.findById(id).ifPresentOrElse(
                (foundUser) -> {
                    log.info(FETCH_BY_ID, toMessageAppUserWord, id);
                    appUserById = foundUser;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAppUserWord, id);
                    appUserById = new AppUser();
                }
        ); return appUserById;
    }

    private AppUser appUserByUsername;
    public final AppUser getByUsername(final String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                (foundUser) -> {
                    log.info(FETCHING_BY_STH_MESSAGE, toMessageAppUserWord, "Username", username);
                    appUserByUsername = foundUser;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_STH, toMessageAppUserWord, "Username", username);
                    appUserByUsername = new AppUser();
                }
        ); return  appUserByUsername;
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
                            .response(SUCCESS_UPDATE)
                            .success(true)
                            .build();
                },
                () -> updateResponse = UniversalResponse.builder()
                        .date(new Date(System.currentTimeMillis()))
                        .response(FAIL_UPDATE)
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
                            .response(SUCCESS_DELETE)
                            .success(true)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageAppUserWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .response(FAIL_DELETE)
                            .success(false)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<AppUser> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageAppUserWord);
        return userRepository.findAll();
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
                            .response(SUCCESS_SEND_EMAIL_AGAIN)
                            .success(true)
                            .build();

                    log.info("Update verification email, sending email again");
                    mailService.sendEmail(new MailNotification(emailProperty.getEmail_subject(), userDetails.getEmail(), emailProperty.getEmail_from(), emailProperty.getEmail_content(),
                            verificationToken.getToken(), value.getUsername()), mailSender);
                },
                () -> response = UniversalResponse.builder()
                        .date(new Date(System.currentTimeMillis()))
                        .response(FAIL_SEND_EMAIL_AGAIN)
                        .success(false)
                        .build()
        ); return response;
    }
}
