package pl.scf.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class SCFAuthorizationFilter extends OncePerRequestFilter {

    public SCFAuthorizationFilter(String notMatter) {
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final String servletPath = request.getServletPath();
        if(servletPath.equals("/auth/login") || servletPath.equals("/auth/logout") || servletPath.equals("/auth/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            final String authorizationToken = request.getHeader(AUTHORIZATION);
            if(authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
                try {
                    final String token = authorizationToken.substring(7);
                    final DecodedJWT decodedToken = JWT.decode(token);

                    if(decodedToken.getExpiresAt().before(new Date()))
                        throw new TokenExpiredException("JWT Token expired");

                    final String[] roles = decodedToken.getClaim("roles").asArray(String.class);
                    final ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles)
                            .forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                    final String username = decodedToken.getSubject();
                    final UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);

                } catch (final Exception exception) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(APPLICATION_JSON_VALUE);

                    new ObjectMapper().writeValue(response.getOutputStream(), Map.of(
                            "Success", "false",
                            "Date", Instant.now().toString(),
                            "Message", exception.getMessage()
                    ));
                }
            } else{
                filterChain.doFilter(request, response);
            }
        }
    }
}
