package pl.scf.security;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class Routes {
    private final String[] allowedRoutes = new String[] {
            "/auth/login",
            "/auth/logout",
            "/auth/refresh",

            "/auth/token/isExpired/**",
            "/roles/get/user/id",
            "/auth/register",
            "/auth/activate/**",
            "/auth/email/sendAgain/**",

            "/topic/all/last/**",
            "/answer/all/last/**",
            "/topic/category/all/**"
    };

    private final String[] administrator_moderatorRoutes = new String[] {
            "/auth/**",
            "/topic/**",
            "/token/**",
            "/forum/**",
            "/roles/**",
            "/answer/**",
            "/userDetails/**",
            "/topic/category/**",
            "/forumUser/title/**",
            "/forumUser/images/**",
            "/forumUser/description/**",
    };

    private final String[] forUserRoutes = new String[] {
            "/answer/add",
            "/answer/update",
            "/answer/delete/{id}",
            "/answer/get/topic/{id}",
            "/answer/get/user/{id}",

            "/forum/user/update",
            "/forum/user/username/{username}",

            "/auth/auth/user/update",
            "/auth/user/delete/{id}",

            "/token/update",

            "/userDetails/update",
            "/userDetails/get/username/{username}",

            "/forumUser/description/update",

            "/forumUser/images/all",
            "/forumUser/images/update",

            "/topic/add",
            "/topic/update",
            "/topic/delete/{id}",
            "/topic/get/answers/id/{id}",
            "/topic/get/user/id/{id}"
    };
}
