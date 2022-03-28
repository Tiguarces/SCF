package pl.scf.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Collection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.scf.model.AppUser;
import pl.scf.model.property.JWTProperty;
import pl.scf.model.repositories.IAppUserRepository;
import pl.scf.security.filter.SCFAuthenticationFilter;
import pl.scf.security.filter.SCFAuthorizationFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private IAppUserRepository userRepository;
    private JWTProperty jwtProperty;

    private final String[] allowedRoutes = new String[] {
            "/login",
            "/logout",

            "/auth/register",
            "/auth/activate/**",
            "/auth/email/sendAgain/**"
    };

    private final String[] administrator_moderatorRoutes = new String[] {
            "/auth/**",
            "/topic/**",
            "/token/**",
            "/forum/**",
            "/roles/**",
            "/userDetails/**",
            "/topic/category/**",
            "/forumUser/title/**",
            "/forumUser/images/**",
            "/forumUser/description/**"
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

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf()
                .disable();

        http.sessionManagement()
                .sessionCreationPolicy(STATELESS);

        http.addFilter(new SCFAuthenticationFilter(authenticationManager(), jwtProperty.getSecret_password(), jwtProperty.getExpired_time(), jwtProperty.getIssuer()));
        http.addFilterBefore(new SCFAuthorizationFilter(jwtProperty.getSecret_password()), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers(allowedRoutes).permitAll()
                .antMatchers(administrator_moderatorRoutes).hasAnyRole("ADMIN", "MODERATOR")
                .antMatchers(forUserRoutes).hasRole("USER");

        http.logout()
                .logoutUrl("/logout")
                .permitAll();

        http.authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            final String notFound = String.format("User %s not found in the database", username);
            final AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(notFound));
            log.info("User {} found in the database", username);

            final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
            return new User(user.getUsername(), user.getPassword(), authorities);

        }).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
