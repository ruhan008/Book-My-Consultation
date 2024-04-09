package com.bookmyconsultation.ratingservice.security.configuration;

import com.bookmyconsultation.ratingservice.exception.CustomAccessDeniedHandler;
import com.bookmyconsultation.ratingservice.exception.RestAuthenticationEntryPoint;
import com.bookmyconsultation.ratingservice.security.ApplicationUserDetailsService;
import com.bookmyconsultation.ratingservice.security.JWTAuthenticationFilter;
import com.bookmyconsultation.ratingservice.security.JWTTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilterAfter(new JWTTokenVerifier(),JWTAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new RestAuthenticationEntryPoint()).and()
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(applicationUserDetailsService);
        return provider;
    }
}
