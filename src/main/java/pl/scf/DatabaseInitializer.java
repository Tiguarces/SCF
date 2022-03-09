package pl.scf;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.UserRole;
import pl.scf.model.repositories.IForumUserTitleRepository;
import pl.scf.model.repositories.IUserRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "user")
public class DatabaseInitializer {

    private IForumUserTitleRepository titleRepository;
    private IUserRoleRepository roleRepository;

    @Setter
    private Map<String, String> titles;

    @Setter
    private List<String> roles;

    @Bean
    public final void initialize() {
        log.info(String.valueOf(titles));

        if(titleRepository.count() == 0) {
            titleRepository.saveAll(getPreparedTitles(titles));
            log.info("Adding titles");
        } else
            log.info("All titles exists");

        if(roleRepository.count() == 0) {
            final List<UserRole> userRoles = new ArrayList<>();
            roles.forEach(role -> userRoles.add(new UserRole(null, role, null)));

            roleRepository.saveAll(userRoles);
            log.info("Adding roles");
        } else
            log.info("All Roles exists");
    }

    private List<ForumUserTitle> getPreparedTitles(final Map<String, String> data) {
        final List<ForumUserTitle> titles = new ArrayList<>();
        data.forEach((key, value) -> {
            final ForumUserTitle title = ForumUserTitle.builder()
                                                        .titleName(value)
                                                        .rangeIntervalPoints(value)
                                                        .build();
            titles.add(title);
        }); return titles;
    }
}
