package pl.scf.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.scf.model.AppUser;
import pl.scf.model.repositories.IAppUserRepository;
import pl.scf.model.repositories.IUserRoleRepository;
import pl.scf.security.filter.SCFAuthenticationFilter;
import pl.scf.security.filter.SCFAuthorizationFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@NoArgsConstructor
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IAppUserRepository userRepository;

    @Autowired
    private IUserRoleRepository roleRepository;

    @Value("${JWT_SECRET_PASSWORD}")
    private String secretPassword;

    @Value("${JWT_EXPIRED_TIME}")
    private Long jwtExpiredTime;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();

        http.sessionManagement()
                .sessionCreationPolicy(STATELESS);

        http.addFilter(new SCFAuthenticationFilter(authenticationManager(), secretPassword, jwtExpiredTime));
        http.addFilterBefore(new SCFAuthorizationFilter(secretPassword), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                        .antMatchers("/login", "/auth/register", "/auth/logout").permitAll();

        http.authorizeRequests()
                        .anyRequest()
                        .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            final AppUser user = userRepository.findByUsername(username);
            final String notFound = String.format("User %s not found in the database", username);

            if(user == null) {
                log.error(notFound);
                throw new UsernameNotFoundException(notFound);
            } else {
                log.info("User {} found in the database", username);

                final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName()));
                return new User(user.getUsername(), user.getPassword(), authorities);
            }
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
