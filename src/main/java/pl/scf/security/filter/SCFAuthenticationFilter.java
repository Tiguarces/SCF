package pl.scf.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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

    public SCFAuthenticationFilter(AuthenticationManager authenticationManager, String secretPassword, Long jwtExpiredTime) {
        this.authenticationManager = authenticationManager;
        this.secretPassword = secretPassword;
        this.jwtExpiredTime = jwtExpiredTime;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        log.info("Successfully fetched username {} and password {}", username, password);

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final User user = (User) authResult.getPrincipal();
        final Algorithm algorithm = Algorithm.HMAC512(secretPassword.getBytes(UTF_8));
        final List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        final String issuer = "SCF";

        final String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiredTime))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(algorithm);

        final String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiredTime*2))
                .withIssuer(issuer)
                .sign(algorithm);

        final Map<String, String> tokensContentResponse = new HashMap<>();
        tokensContentResponse.put("accessToken", accessToken);
        tokensContentResponse.put("refreshToken", refreshToken);
        tokensContentResponse.put("username", user.getUsername());

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokensContentResponse);
    }
}
