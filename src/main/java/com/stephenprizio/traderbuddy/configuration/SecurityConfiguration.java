package com.stephenprizio.traderbuddy.configuration;

import com.stephenprizio.traderbuddy.security.jwt.filters.JwtRequestFilter;
import com.stephenprizio.traderbuddy.security.jwt.authentication.JwtAuthenticationEntryPoint;
import com.stephenprizio.traderbuddy.services.security.TraderBuddyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Security configuration
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource(name = "jwtAuthenticationEntryPoint")
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService userDetailsService;

    @Resource(name = "jwtRequestFilter")
    private JwtRequestFilter filter;


    //  METHODS

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .cors().disable() //  TODO: TEMP
                .authorizeRequests().antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(this.filter, UsernamePasswordAuthenticationFilter.class);
    }
}
