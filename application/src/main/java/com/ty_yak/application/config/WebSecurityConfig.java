package com.ty_yak.application.config;

import com.google.common.collect.ImmutableList;
import com.ty_yak.auth.config.JwtAuthenticationFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.google.api.client.http.HttpMethods.*;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .addFilter(new JwtAuthenticationFilter(authenticationManagerBean(), jwtSecret))
                .exceptionHandling()
                .authenticationEntryPoint((request, response, exception) ->
                        response.sendError(SC_FORBIDDEN, "You're not authorized to perform such action."))
                .and()
                .authorizeRequests()
                // UserController
                .antMatchers("/api/**").permitAll()
                /*
                .antMatchers("api/users/v1/password/recovery").permitAll()
                .antMatchers(POST, "/api/users/v1/registration/**").permitAll()
                .antMatchers(POST, "/api/users/v1/check/username").permitAll()
                .antMatchers(POST, "/api/users/v1/password-code/confirm").hasRole("USER")
                .antMatchers(POST, "/api/users/v1/email-code/generate").hasRole("USER")
                .antMatchers(POST, "/api/users/v1/email-code/confirm").hasRole("USER")
                .antMatchers(GET, "/api/users/v1/info").hasRole("USER")
                .antMatchers(POST, "/api/users/v1/avatar").hasRole("USER")
                .antMatchers(PUT, "/api/users/v1/device-token").hasRole("USER")
                .antMatchers(PUT, "/api/users/v1/device-token/reset").hasRole("USER")
                .antMatchers( "/api/users/v1/upload").permitAll()
                .antMatchers(DELETE, "api/users/v1").hasRole("USER")
                // LoginController
                .antMatchers(POST, "/api/v1/login/**").permitAll()
                .antMatchers(POST, "/api/v1/token/refresh").hasRole("USER")
                .antMatchers(POST, "/api/v1/code/generate").hasRole("USER")
                .antMatchers(POST, "/api/v1/logout").hasRole("USER")
                // ReportController
                .antMatchers(GET, "/api/reports/v1/**").permitAll()
                 */
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .cors(AbstractHttpConfigurer::disable)
                .csrf().disable()
                .logout().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(ImmutableList.of("Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
