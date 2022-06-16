//package com.quiz.filter;
//
//import com.quiz.exception.ResourceForbiddenRequestException;
//import com.quiz.service.MyUserDetailsService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//import java.util.function.Function;
//
//@Component
//public class AuthorizationFilter extends OncePerRequestFilter {
//    private String secret = "secret";
//
//
//    @Autowired
//    private MyUserDetailsService myUserDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        final String authorizationHeader = request.getHeader("Authorization");
//        String username = null;
//        String jwtToken = null;
//        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwtToken = authorizationHeader.substring(7);
//            username = getUsernameFromToken(jwtToken);
//        }
//
//        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);
//            if(validateToken(jwtToken, userDetails)){
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities()
//                );
//                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//
//    }
//
//    //check if the token has expired
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//    //validate token
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//    public String getUsernameFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    //retrieve expiration date from jwt token
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//    //for retrieveing any information from token we will need the secret key
//    private Claims getAllClaimsFromToken(String token) throws ResourceForbiddenRequestException {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//}
