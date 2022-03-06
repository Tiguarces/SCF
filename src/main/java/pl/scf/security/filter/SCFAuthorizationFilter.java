package pl.scf.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SCFAuthorizationFilter extends OncePerRequestFilter {

    @Value("${JWT_SECRET_PASSWORD}")
    private String secretPassword;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String servletPath = request.getServletPath();
        if(servletPath.equals("/login") || servletPath.equals("/logout")) {
            filterChain.doFilter(request, response);
        } else {
            final String authorizationToken = request.getHeader(AUTHORIZATION);
            if(authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
                try {
                    final String token = authorizationToken.substring(7);
                    final Algorithm algorithm = Algorithm.HMAC512(secretPassword.getBytes(UTF_8));

                    final JWTVerifier verifier = JWT.require(algorithm).build();
                    final DecodedJWT decodedToken = verifier.verify(token);

                    final String[] roles = decodedToken.getClaim("roles").asArray(String.class);
                    final ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                    final String username = decodedToken.getSubject();
                    final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (final Exception exception) {
                    log.error("Error while parsing token | {}", exception.getMessage());

                    final Map<String, String> errorResponseContent = new HashMap<>();
                    errorResponseContent.put("Error Message", exception.getMessage());

                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setHeader("Error", exception.getMessage());
                    response.setStatus(SC_FORBIDDEN);
                    new ObjectMapper().writeValue(response.getOutputStream(), errorResponseContent);
                }
            } else{
                filterChain.doFilter(request, response);
            }
        }
    }
}
