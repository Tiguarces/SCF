package pl.scf.model.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.scf.model.*;
import pl.scf.model.repositories.IAppUserRepository;
import pl.scf.model.repositories.IForumUserTitleRepository;
import pl.scf.model.repositories.IUserRoleRepository;
import pl.scf.model.requests.RegisterRequest;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AppUserService {
    private final IAppUserRepository userRepository;
    private final IUserRoleRepository roleRepository;
    private final IForumUserTitleRepository titleRepository;

    private final PasswordEncoder passwordEncoder;

    public final void register(final RegisterRequest request) {
        final UserRole role = roleRepository.findById(request.getRoleId()).orElse(new UserRole(null, "ROLE_USER", null));
        final AppUser appUser = AppUser.builder()
                                                .username(request.getUsername())
                                                .password(passwordEncoder.encode(request.getPassword()))
                                                .role(role)
                                                .build();

        final ForumUserTitle beginnerTitle = titleRepository.findById(1L).orElse(new ForumUserTitle(null, "0-0" ,"Nie określone", null));
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
                                                                .enabled(0)
                                                                .build();

        appUser.setUser_details(userDetails);

        log.info("Saving new AppUser");
        userRepository.save(appUser);
    }

    public final AppUser getById(final Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id).orElse(new AppUser());
    }

    public final AppUser getByUsername(final String username) {
        log.info("Fetching user with username: {}", username);
        return userRepository.findByUsername(username);
    }

    public final void update(final AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        log.info("Encoding new AppUser password");

        log.info("Updating AppUser with id: {}", appUser.getId());
        userRepository.save(appUser);
    }

    public final void delete(final Long id) {
        log.info("Deleting AppUser with id: {}", id);
        userRepository.deleteById(id);
    }

    public final List<AppUser> getAll() {
        log.info("Fetching all AppUsers");
        return userRepository.findAll();
    }
}
