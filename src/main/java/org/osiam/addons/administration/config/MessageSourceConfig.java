package org.osiam.addons.administration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    @Bean
    public ResourceBundleMessageSource messageSource(){ //don't change the method name!
        ResourceBundleMessageSource resource = new ResourceBundleMessageSource();
        
        resource.setBasenames("l10n/general");
        
        return resource;
    }
}
