package com.telusko.secureapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{

    Logger logger = LoggerFactory.getLogger(AppSecurityConfig.class);
    @Autowired
    private UserDetailsService userDetailsService;

    /*
     Fetching data from the database
     */
    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        logger.debug("userDetailsService: " + this.userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    /*
     Tell spring security to not give you a login page,
     that you will provide yours
    */
@Override
    protected void configure(HttpSecurity http) throws  Exception{

        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logout-success").permitAll();
    }


    /* @Bean
    @Override
    protected UserDetailsService userDetailsService() {

        List<UserDetails> users = new ArrayList<>();
        users.add(User.withDefaultPasswordEncoder()
                .username("navin").password("1234").roles("User").build());

        return new InMemoryUserDetailsManager(users);
    }*/
}
