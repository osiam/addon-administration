package org.osiam.addons.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * This class is the entry point for spring.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource(value = "classpath:addon-administration.properties", ignoreResourceNotFound = true)
public class Administration {

    public static void main(String[] args) {
        SpringApplication.run(Administration.class, args);
    }
}
