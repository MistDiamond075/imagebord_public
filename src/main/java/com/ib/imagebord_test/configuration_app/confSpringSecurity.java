package com.ib.imagebord_test.configuration_app;

import com.ib.imagebord_test.configuration_app.filter.CookieFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class confSpringSecurity {
    private final confPropsCookies propsCookies;

    public confSpringSecurity(confPropsCookies propsCookies) {
        this.propsCookies = propsCookies;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpsec) throws Exception {
        httpsec
                .headers(headers -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable))
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(request -> (request.getRequestURI().startsWith("¯\_(ツ)_/¯"))
                        && (!request.getMethod().equals("GET")
                )))
                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("%%mod_access%%").hasAnyRole("¯\_(ツ)_/¯","¯\_(ツ)_/¯")
                        .requestMatchers("%%admin_access%%").hasRole("¯\_(ツ)_/¯")
                        .anyRequest().permitAll()
                ).formLogin(formLogin ->
                        formLogin
                                .loginPage("¯\_(ツ)_/¯")
                                .permitAll()
                                .defaultSuccessUrl("¯\_(ツ)_/¯",true)
                ).
                logout(logout ->
                        logout
                                .logoutUrl("¯\_(ツ)_/¯")
                                .logoutSuccessUrl("/")
                                .permitAll()
                );
        httpsec.addFilterBefore(new CookieFilter(propsCookies), UsernamePasswordAuthenticationFilter.class);
        return httpsec.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
