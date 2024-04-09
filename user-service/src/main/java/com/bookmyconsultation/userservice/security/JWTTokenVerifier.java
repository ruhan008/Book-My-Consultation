package com.bookmyconsultation.userservice.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JWTTokenVerifier extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")){
            log.info("Authorization header received: {}", authorizationHeader);
            filterChain.doFilter(request,response);
            return;
        }

        try{
            String key = "bmc-62b9a265bbe2af58b19ffa5c-secure";
            String token = authorizationHeader.replace("Bearer ","");
            log.info("Token Received: {}", token);
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(key.getBytes())).build()
                    .parseClaimsJws(token);
            String username = claimsJws.getBody().getSubject();
            var authorities = (List<Map<String,String>>)claimsJws.getBody().get("authorities");
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(a-> new SimpleGrantedAuthority(a.get("authority"))).collect(Collectors.toSet());
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,
                    simpleGrantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (JwtException e){
            throw new IllegalStateException("Token Invalid");
        }
        filterChain.doFilter(request,response);
    }
}
