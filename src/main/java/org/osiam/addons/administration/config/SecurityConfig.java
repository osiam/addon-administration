package org.osiam.addons.administration.config;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * This class contains the configuration about the web security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private AdminAccessDecisionManager decisionManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // NOSONAR the authorizeRequests() throws it and can't be omitted
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http.addFilterBefore(filter, CsrfFilter.class);
        http.authorizeRequests()
                .accessDecisionManager(decisionManager)
                .antMatchers(AdminController.CONTROLLER_PATH + "/**")
                .authenticated();
    }

}
