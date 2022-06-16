//package com.quiz.security;
//
//import com.quiz.exception.GlobalExceptionHandler;
//import com.quiz.filter.AuthorizationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//
//    @Autowired
//    private AuthorizationFilter authorizationFilter;
//
//    @Autowired
//    private GlobalExceptionHandler globalExceptionHandler;
//
//
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web
//                .ignoring()
//                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**");
//
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/quiz/**").permitAll()
//               //.antMatchers( "/accounts/{id}").hasAuthority("ADMIN")
//                .and().exceptionHandling().accessDeniedHandler(((request, response, accessDeniedException) -> {
//                    globalExceptionHandler.handleConflict(response);
//                }))
//                .and()
//                .logout().
//                clearAuthentication(true).
//                invalidateHttpSession(true)
//                .and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//
//    @Bean
//    BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public CorsFilter corsFilter() {
////        UrlBasedCorsConfigurationSource source =
////                new UrlBasedCorsConfigurationSource();
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.addAllowedOrigin("*");
////        config.addAllowedHeader("*");
////        config.addAllowedMethod("*");
////        source.registerCorsConfiguration("/**", config);
////        return new CorsFilter((CorsConfigurationSource) source);
////    }
//
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//}
