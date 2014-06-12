package org.osiam.addons.administration.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This class is the entry point for spring.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.osiam.addons.administration")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}