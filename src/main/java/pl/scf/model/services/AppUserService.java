package pl.scf.model.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.AppUserDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.JwtException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.LoginRequest;
import pl.scf.api.model.request.RefreshTokenRequest;
import pl.scf.api.model.request.RegisterRequest;
import pl.scf.api.model.request.UpdateUserRequest;
import pl.scf.api.model.response.*;
import pl.scf.model.*;
import pl.scf.model.mail.MailNotification;
import pl.scf.model.mail.MailService;
import pl.scf.model.property.EmailProperty;
import pl.scf.model.property.JWTProperty;
import pl.scf.model.repositories.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toAppUser;
import static pl.scf.api.model.utils.DTOMapper.toUserRoles;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

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

    public LoginResponse login(final LoginRequest request) throws AuthenticationException{
        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        final User user = (User) authentication.getPrincipal();
        final Algorithm algorithm = Algorithm.HMAC512(jwtProperty.getSecret_password().getBytes(UTF_8));
        final List<String> roles = toUserRoles(user.getAuthorities());

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

        log.info(AUTHENTICATE_USER_SUCCESS);
        return LoginResponse.builder()
                .success(true)
                .date(Instant.now())
                .message(LOGIN_SUCCESS)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .build();
    }

    private RefreshTokenResponse refreshTokenResponse;
    public final RefreshTokenResponse refreshToken(final RefreshTokenRequest request) throws JwtException, JWTVerificationException {
        if (request == null)
            throw new JWTVerificationException("JWT Token cannot be null");

        final Algorithm algorithm = Algorithm.HMAC512(jwtProperty.getSecret_password().getBytes(UTF_8));
        final JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        final DecodedJWT decodedJWT = jwtVerifier.verify(request.getRefreshToken());

        if (decodedJWT.getExpiresAt().before(new Date()))
            throw new JwtException("Refresh Token expired");
        else
            log.info("Refresh token is valid");

        final String userUsername = decodedJWT.getSubject();
        userRepository.findByUsername(userUsername).ifPresentOrElse(
                (foundUser) -> {
                    final String role = "ROLE_" + foundUser.getRole().getName();

                    String accessToken = JWT.create()
                            .withSubject(foundUser.getUsername())
                            .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperty.getExpired_time()))
                            .withIssuer(jwtProperty.getIssuer())
                            .withClaim("roles", List.of(role))
                            .sign(algorithm);

                    String refreshToken = JWT.create()
                            .withSubject(foundUser.getUsername())
                            .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperty.getExpired_time() * 2))
                            .withIssuer(jwtProperty.getIssuer())
                            .sign(algorithm);

                    log.info(AUTHENTICATE_USER_SUCCESS);
                    refreshTokenResponse = RefreshTokenResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(REFRESH_SUCCESS)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .username(foundUser.getUsername())
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUser with specified User username");
                }); return refreshTokenResponse;
    }

    public final UniversalResponse logoutUser() {
        SecurityContextHolder.clearContext();
        return UniversalResponse.builder()
                .success(true)
                .date(Instant.now())
                .message(LOGOUT_USER)
                .build();
    }

    public final UniversalResponse register(final RegisterRequest request) throws NotFoundException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("User with username {} exists, skipping adding", request.getUsername());
            return UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
                    .message(USER_EXISTS)
                    .build();
        }

        if (detailsRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("User with email {} exists, skipping adding", request.getEmail());
            return UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
                    .message(EMAIL_IS_USED)
                    .build();
        }

        if (detailsRepository.findByNickname(request.getNickname()).isPresent()) {
            log.warn("User with nickname {} exists, skipping adding", request.getNickname());
            return UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
                    .message(NICKNAME_IS_USED)
                    .build();
        }

        final var role = roleRepository.findById(request.getRoleId());
        if(role.isEmpty())
            throw new NotFoundException("Not found specified User Role");

        final AppUser appUser = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role.get())
                .build();

        final var beginnerTitle = titleRepository.findById(1L);
        if(beginnerTitle.isEmpty())
            throw new NotFoundException("Not found beginner Title");

        final ForumUser forumUser = ForumUser.builder()
                .user(appUser)
                .description(new ForumUserDescription())
                .images(new ForumUserImages())
                .reputation(0)
                .title(beginnerTitle.get())
                .visitors(0)
                .build();

        final AppUserDetails userDetails = AppUserDetails.builder()
                .user(appUser)
                .forumUser(forumUser)
                .createdDate(Instant.now())
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

        final boolean isSent = mailService.sendEmail(new MailNotification(emailProperty.getEmail_subject(),
                userDetails.getEmail(), emailProperty.getEmail_from(), emailProperty.getEmail_content(),
                verificationToken.getToken(), appUser.getUsername()), mailSender);

        if(isSent) {
            log.info(SUCCESS_SAVING);
            userRepository.save(appUser);
            return UniversalResponse.builder()
                    .success(true)
                    .date(Instant.now())
                    .message(SUCCESS_SEND_EMAIL)
                    .build();
        } else {
            log.warn(FAIL_SAVING);
            return UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
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
                            .date(Instant.now())
                            .build();
                },
                () -> emailResponse = ActivateEmailResponse.builder()
                        .activated(false)
                        .success(false)
                        .message(FAIL_ACTIVATION_EMAIL)
                        .date(Instant.now())
                        .build()
        ); return emailResponse;
    }

    private AppUserResponse userByIdResponse;
    public final AppUserResponse getById(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

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
                            .date(Instant.now())
                            .success(true)
                            .message("Found AppUser")
                            .user(userDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUser with specified id");
                }
        ); return userByIdResponse;
    }

    private AppUserResponse userByUsernameResponse;
    public final AppUserResponse getByUsername(final String username) throws NotFoundException{
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
                            .date(Instant.now())
                            .success(true)
                            .message("Found AppUser")
                            .user(userDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUser with specified username");
                }
        ); return  userByUsernameResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final UpdateUserRequest request) throws NotFoundException{
        userRepository.findByUsername(request.getUsername()).ifPresentOrElse(
                (value) -> {
                    request.setPassword(passwordEncoder.encode(request.getPassword()));
                    log.info("Encoding new AppUser password");

                    log.info(UPDATE_MESSAGE, toMessageAppUserWord, value.getId());
                    value.setPassword(request.getPassword());
                    userRepository.save(value);

                    updateResponse = UniversalResponse.builder()
                            .date(Instant.now())
                            .message(SUCCESS_UPDATE)
                            .success(true)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUser with specified User username");
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        userRepository.findById(id).ifPresentOrElse(
                (foundUser) -> {
                    log.info(DELETING_MESSAGE, toMessageAppUserWord, id);
                    userRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .success(true)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found AppUser with specified id");
                }
        ); return deleteResponse;
    }

    private UniversalResponse response;
    public final UniversalResponse sendEmailAgain(final Long userId) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(userId);

        userRepository.findById(userId).ifPresentOrElse(
                (value) -> {
                    final int randomValue = new Random().nextInt(value.getUsername().length()*2);
                    final String newToken = generateVerificationToken(value.getUsername().concat("sendAgain" + randomValue));

                    final AppUserDetails userDetails = value.getUser_details();
                    final VerificationToken verificationToken = value.getToken();

                    verificationToken.setToken(newToken);
                    tokenRepository.save(verificationToken);
                    response = UniversalResponse.builder()
                            .date(Instant.now())
                            .message(SUCCESS_SEND_EMAIL_AGAIN)
                            .success(true)
                            .build();

                    log.info("Update verification email, sending email again");
                    mailService.sendEmail(new MailNotification(emailProperty.getEmail_subject(), userDetails.getEmail(),
                            emailProperty.getEmail_from(), emailProperty.getEmail_content(),
                            verificationToken.getToken(), value.getUsername()), mailSender);
                },
                () -> {
                    throw new NotFoundException("Not found AppUser with specified id");
                }
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
                .date(Instant.now())
                .message(SUCCESS_DELETE)
                .build();
    }
}
