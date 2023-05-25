package com.example.MainProject.security;

import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.entities.token.Tokens;
import com.example.MainProject.repository.TokenRepo;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    CustomUserDetails customUserDetails;
    @Autowired
    TokenRepo tokenRepo;
    @Autowired
    TokenValidation tokenValidation;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token=getJWTFromRequest(request);

        String path=request.getRequestURI();
        if(path.startsWith("/api/admin")|| path.startsWith("/api/customer")
                ||path.startsWith("/api/seller")){
            if(StringUtils.hasText(token)&& tokenValidation.validToken(token)){
                String email=jwtGenerator.getUserNameFromJwt(token);

                UserDetails userDetails=customUserDetails.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            Tokens tokens=tokenRepo.findByToken(token).orElse(null);

            if(Objects.isNull(tokens)){
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                Gson gson =new Gson();

                GenerateResponse gg=new GenerateResponse("Invalid authorization Token");
                String jsonString= gson.toJson(gg);
                response.setContentType("application/json");
                response.getWriter().write(jsonString);

                return ;
            }
            if(tokens.getExpiration().before(new Date())){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                Gson gson=new Gson();

                GenerateResponse gg = new GenerateResponse("Your session is expired log in again " +
                        "to access");
                String jsonString=gson.toJson(gg);
                response.setContentType("application/json");
                response.getWriter().write(jsonString);

                return;
            }


            filterChain.doFilter(request,response);
            return;

        }
        if(StringUtils.hasText(token)&& jwtGenerator.validateToken(token)){
            String email=jwtGenerator.getUserNameFromJwt(token);

            UserDetails userDetails=customUserDetails.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
            filterChain.doFilter(request,response);
    }


    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken=request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }
}
