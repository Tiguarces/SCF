package pl.scf;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.UserRole;
import pl.scf.model.property.InitializerProperty;
import pl.scf.model.repositories.IForumUserTitleRepository;
import pl.scf.model.repositories.IUserRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class DatabaseInitializer {
    private IForumUserTitleRepository titleRepository;
    private IUserRoleRepository roleRepository;
    private InitializerProperty initializerProperty;

    @Bean
    public final void initialize() {
        final var roles = initializerProperty.getRoles();
        final var titles = initializerProperty.getTitles();

        if(titleRepository.count() == 0) {
            titleRepository.saveAll(getPreparedTitles(titles));
            log.info("Adding User titles");
        } else
            log.info("All User titles exists");

        if(roleRepository.count() == 0) {
            final List<UserRole> userRoles = new ArrayList<>();
            roles.forEach(role -> userRoles.add(new UserRole(null, role, null)));

            roleRepository.saveAll(userRoles);
            log.info("Adding user roles");
        } else
            log.info("All User Roles exists");
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
