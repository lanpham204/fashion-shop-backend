package com.shop.filters;

import com.shop.models.User;
import com.shop.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request
            , @NotNull HttpServletResponse response
            , @NotNull FilterChain filterChain) throws ServletException, IOException {
        if(isByPassToken(request)) {
            filterChain.doFilter(request,response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
            return;
        }

        final  String token = authHeader.substring(7);
        final  String email = jwtTokenUtils.extractEmail(token);
        if(email!= null && SecurityContextHolder.getContext().getAuthentication() == null){
            User userDetails = (User) userDetailsService.loadUserByUsername(email);
            if(jwtTokenUtils.validateToken(token,userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
    private boolean isByPassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String,String>> bypassTokens = Arrays.asList(
                Pair.of(apiPrefix+"/products","GET"),
                Pair.of("/swagger-ui/index.html","GET"),
                Pair.of(apiPrefix+"/categories", "GET"),
                Pair.of(apiPrefix+"/users/login", "POST"),
                Pair.of(apiPrefix+"/users/register", "POST"),
                Pair.of(apiPrefix+"/orders/payment-callback","GET")
        );
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        if(requestPath.equals(apiPrefix+"/orders/")
                && requestMethod.equals("GET")) {
            return true;
        }
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (requestPath.contains(bypassToken.getLeft()) &&
                    requestMethod.equals(bypassToken.getRight())) {
                return true;
            }

        }
        return false;
    }
}
