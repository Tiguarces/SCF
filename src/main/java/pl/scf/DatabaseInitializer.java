package pl.scf;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.scf.model.ForumUserTitle;
import pl.scf.model.UserRole;
import pl.scf.model.repositories.IForumUserTitleRepository;
import pl.scf.model.repositories.IUserRoleRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class DatabaseInitializer {
    private final IForumUserTitleRepository titleRepository;
    private final IUserRoleRepository roleRepository;

    @Bean
    public final void initialize() {
        if(titleRepository.count() == 0) {
            final Map<String, String> titles = new HashMap<>();

            titles.put("Świeżak", "0-50");
            titles.put("Wkraczający", "51-99");
            titles.put("Chętny do pomocy", "100-199");
            titles.put("Młodszy specjalista", "200-499");
            titles.put("Specjalista na 5+", "500-1499");
            titles.put("Prawdziwy szef", "2000-X");

            titleRepository.saveAll(getPreparedTitles(titles));
            log.info("Adding titles");
        } else
            log.info("All titles exists");

        if(roleRepository.count() == 0) {
            final List<UserRole> roles = List.of(
                    new UserRole(null, "ROLE_USER", null),
                    new UserRole(null, "ROLE_ADMIN", null),
                    new UserRole(null, "ROLE_MODERATOR", null)
            );

            roleRepository.saveAll(roles);
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
