package pl.scf.model.services;

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

@Slf4j
@Service
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

    public AppUserService(IAppUserRepository userRepository, IUserRoleRepository roleRepository, IAppUserDetailsRepository detailsRepository, IForumUserTitleRepository titleRepository,
                          IVerificationTokenRepository tokenRepository, PasswordEncoder passwordEncoder, MailService mailService, JavaMailSender mailSender, EmailProperty emailProperty) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.detailsRepository = detailsRepository;
        this.titleRepository = titleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.mailSender = mailSender;
        this.emailProperty = emailProperty;
    }

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
            log.info("Saving new AppUser");
            userRepository.save(appUser);
            return RegisterResponse.builder()
                    .created(false)
                    .date(new Date(System.currentTimeMillis()))
                    .serverResponse("User created successfully, sending activation email")
                    .build();
        } else {
            return RegisterResponse.builder()
                    .created(true)
                    .date(new Date(System.currentTimeMillis()))
                    .serverResponse("User not created, problem with sending activation email")
                    .build();
        }
    }

    private String generateVerificationToken(final String username) {
        final String password = emailProperty.getSecret_password().concat(username);
        return UUID.nameUUIDFromBytes(password.getBytes(UTF_8)).toString();
    }

    public final ActivateEmailResponse activateAccount(final String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if(verificationToken == null) {
            return ActivateEmailResponse.builder()
                    .activated(false)
                    .response("Fail while activating account")
                    .date(new Date(System.currentTimeMillis()))
                    .build();
        }

        String response = "Email activated successfully";
        boolean activated = false;

        if(verificationToken.getActivated() == 0) {
            verificationToken.setActivated(1);
            log.info("Account {} activated", verificationToken.getUser().getUsername());

            activated = true;
            tokenRepository.save(verificationToken);
        } else {
            log.info("Account {} has activated yet", verificationToken.getUser().getUsername());
            response = "Account is active";
        }

        final String nickname = verificationToken.getUser()
                .getUser_details().getNickname();

        return ActivateEmailResponse.builder()
                .activated(activated)
                .nickname(nickname)
                .response(response)
                .date(new Date(System.currentTimeMillis()))
                .build();
    }

    public final AppUser getById(final Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id).orElse(new AppUser());
    }

    public final AppUser getByUsername(final String username) {
        log.info("Fetching user with username: {}", username);
        return userRepository.findByUsername(username).orElse(new AppUser());
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final UpdateUserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        log.info("Encoding new AppUser password");

        userRepository.findByUsername(request.getUsername()).ifPresentOrElse(
                (value) -> {
                    log.info("Updating AppUser with id: {}", value.getId());
                    userRepository.save(value);

                    response = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .response("User updated successfully")
                            .success(true)
                            .build();
                },
                () -> updateResponse = UniversalResponse.builder()
                        .date(new Date(System.currentTimeMillis()))
                        .response("User updated failed")
                        .success(false)
                        .build()
        ); return updateResponse;
    }

    public final UniversalResponse delete(final Long id) {
        try {
            log.info("Deleting AppUser with id: {}", id);
            userRepository.deleteById(id);

            return UniversalResponse.builder()
                    .date(new Date(System.currentTimeMillis()))
                    .response("User deleted successfully")
                    .success(true)
                    .build();

        } catch (final Exception exception) {
            log.warn("User delete fail, message: {}", exception.getMessage());
            return UniversalResponse.builder()
                    .date(new Date(System.currentTimeMillis()))
                    .response("User deleted failed")
                    .success(false)
                    .build();
        }
    }

    public final List<AppUser> getAll() {
        log.info("Fetching all AppUsers");
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
                            .response("Email sent again successfully")
                            .success(true)
                            .build();

                    log.info("Update verification email, sending email again");
                    mailService.sendEmail(new MailNotification(emailProperty.getEmail_subject(), userDetails.getEmail(), emailProperty.getEmail_from(), emailProperty.getEmail_content(),
                            verificationToken.getToken(), value.getUsername()), mailSender);
                },
                () -> response = UniversalResponse.builder()
                        .date(new Date(System.currentTimeMillis()))
                        .response("Email not send again, fail")
                        .success(false)
                        .build()
        ); return response;
    }
}
