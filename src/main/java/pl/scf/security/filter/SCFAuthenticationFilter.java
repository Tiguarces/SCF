package pl.scf.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class SCFAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final String secretPassword;
    private final Long jwtExpiredTime;
    private final String jwtIssuer;

    public SCFAuthenticationFilter(AuthenticationManager authenticationManager, String secretPassword, Long jwtExpiredTime, String jwtIssuer) {
        this.authenticationManager = authenticationManager;
        this.secretPassword = secretPassword;
        this.jwtExpiredTime = jwtExpiredTime;
        this.jwtIssuer = jwtIssuer;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        log.info("Successfully fetched username {} and password", username);

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Getter
    private String accessToken;

    @Getter
    private String refreshToken;

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException {
        final User user = (User) authResult.getPrincipal();
        final Algorithm algorithm = Algorithm.HMAC512(secretPassword.getBytes(UTF_8));
        final List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiredTime))
                .withIssuer(jwtIssuer)
                .withClaim("roles", roles)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiredTime*2))
                .withIssuer(jwtIssuer)
                .sign(algorithm);

        final Map<String, String> tokensContentResponse = new HashMap<>();
        tokensContentResponse.put("accessToken", accessToken);
        tokensContentResponse.put("refreshToken", refreshToken);
        tokensContentResponse.put("username", user.getUsername());

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokensContentResponse);
    }
}
