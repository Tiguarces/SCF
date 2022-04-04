package pl.scf;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.scf.model.*;
import pl.scf.model.property.AdministratorAccountProperty;
import pl.scf.model.property.InitializerProperty;
import pl.scf.model.repositories.IAppUserRepository;
import pl.scf.model.repositories.IForumUserTitleRepository;
import pl.scf.model.repositories.IUserRoleRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static pl.scf.api.model.utils.ApiConstants.*;

@Slf4j
@Component
@AllArgsConstructor
public class DatabaseInitializer {
    private IForumUserTitleRepository titleRepository;
    private IUserRoleRepository roleRepository;
    private IAppUserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private InitializerProperty initializerProperty;
    private AdministratorAccountProperty administratorAccountProperty;

    @Bean
    public final void initialize() {
        final var roles = initializerProperty.getRoles();
        final var titles = initializerProperty.getTitles();

        if(titleRepository.count() == 0) {
            titleRepository.saveAll(getPreparedTitles(titles));
            log.info(USER_TITLES_ADDING);
        } else
            log.info(USER_TITLES_EXISTS);


        if(roleRepository.count() == 0) {
            final List<UserRole> userRoles = new ArrayList<>();
            roles.forEach(role -> userRoles.add(new UserRole(null, role, null)));

            roleRepository.saveAll(userRoles);
            log.info(USER_ROLES_ADDING);
        } else
            log.info(USER_ROLES_EXISTS);


        final UserRole adminRole = roleRepository.findByName("ADMIN");
        final Long adminId = adminRole.getId();

        userRepository.findByRoleId(adminId).ifPresentOrElse(
                (admin) -> log.info(ADMIN_ACCOUNT_EXISTS),
                () -> {
                    final AppUser admin = AppUser.builder()
                            .role(adminRole)
                            .password(passwordEncoder.encode(administratorAccountProperty.getPassword()))
                            .username(administratorAccountProperty.getUsername())
                            .build();

                    final AppUserDetails adminDetails = AppUserDetails.builder()
                            .createdDate(new Date(System.currentTimeMillis()))
                            .email(administratorAccountProperty.getEmail())
                            .nickname(administratorAccountProperty.getNickname())
                            .user(admin)
                            .forumUser(new ForumUser())
                            .build();

                    final VerificationToken verificationToken = VerificationToken.builder()
                            .user(admin)
                            .token("ADMINISTRATOR_TOKEN_NOT_REQUIRED")
                            .activated(1)
                            .build();

                    admin.setToken(verificationToken);
                    admin.setUser_details(adminDetails);

                    userRepository.save(admin);
                    log.info(ADMIN_ACCOUNT_CREATE);
                }
        );
    }

    private List<ForumUserTitle> getPreparedTitles(final Map<String, String> data) {
        final List<ForumUserTitle> titles = new ArrayList<>();
        data.forEach((key, value) -> {
            final ForumUserTitle title = ForumUserTitle.builder()
                                                        .titleName(key.substring(2))
                                                        .rangeIntervalPoints(value)
                                                        .build();
            titles.add(title);
        }); return titles;
    }
}
