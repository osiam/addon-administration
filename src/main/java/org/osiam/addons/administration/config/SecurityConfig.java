package org.osiam.addons.administration.config;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This class contains the configuration about the web security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private AdminAccessDecisionManager decisionManager;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .accessDecisionManager(decisionManager)
                .antMatchers(AdminController.CONTROLLER_PATH + "/**")
                .authenticated();
    }

}
