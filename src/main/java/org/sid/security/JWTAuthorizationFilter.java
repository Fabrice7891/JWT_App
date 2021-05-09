package org.sid.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        httpServletResponse.addHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.addHeader("Access-Control-Allow-Headers","Origin, Accept, X-Request-With");

        String jwt=httpServletRequest.getHeader(SecurityParams.HEADER_NAME);
        System.out.println(jwt);
        if(jwt==null || !jwt.startsWith(SecurityParams.HEADER_PREFIX)) {
            filterChain.doFilter(httpServletRequest,httpServletResponse);
             return;
        }

    JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
        // decodage du jwt en retirant Bearer
        DecodedJWT decodedJWT= jwtVerifier.verify(jwt.substring(SecurityParams.HEADER_PREFIX.length()));

        String username= decodedJWT.getSubject();
        List<String> roles= decodedJWT.getClaims().get("roles").asList(String.class);
        System.out.println(username);
        System.out.println(roles);
        Collection<GrantedAuthority> authorities=new ArrayList<>();
        roles.forEach(r->{
            authorities.add(new SimpleGrantedAuthority(r));
        });
        UsernamePasswordAuthenticationToken user=new UsernamePasswordAuthenticationToken(username,null, authorities);
        SecurityContextHolder.getContext().setAuthentication(user);
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
