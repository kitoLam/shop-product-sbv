package com.group1.productcatalogsystem.filters;

import com.group1.productcatalogsystem.exception.UnAuthenticatedRequestException;
import com.group1.productcatalogsystem.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.data.util.Pair;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{

    private String apiPrefix =  "/api/v1";
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isBypassToken(request)){
            filterChain.doFilter(request,response);
            System.out.println("Loix 1");
            return;
        }
        // B1: Lấy token ra trc:
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("token:"+authorizationHeader);

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            System.out.println("Loix 2");

            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.substring(7);
        try {
            System.out.println("pre::");
            String username = jwtTokenUtil.getUsernameFromToken(token);
            System.out.println("post::");
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println(userDetails.getUsername());
                // ta đã phải sử lí việc bắt exception tk này ko có trg db ở UserDetailService rồi nen userDetails se khong bao gio null
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // chứa thông tin ai đang tương tác
                        null, // để credentials = null de nói đã validate rồi, không cần password nữa
                        userDetails.getAuthorities() // Lấy các role của user
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (ExpiredJwtException expiredJwtException) {
            throw new UnAuthenticatedRequestException("Token is expired");
        } catch (Exception e) {
            System.out.println("JWT error type: " + e.getClass().getName());
            System.out.println("JWT error message: " + e.getMessage());
            throw new UnAuthenticatedRequestException("JWT error");
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request){
        List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/auth/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/auth/login", apiPrefix), "POST"),
                Pair.of("/swagger", "GET")
        );
        for(Pair<String, String> bypassToken : bypassTokens){
            String path = request.getServletPath();
            String method = request.getMethod();
            if(path.equals(bypassToken.getFirst()) && method.equals(bypassToken.getSecond())){
                return true;
            }
        }
        return false;
    }
}