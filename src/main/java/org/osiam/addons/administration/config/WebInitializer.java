package org.osiam.addons.administration.config;

import org.osiam.addons.administration.Administration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * This class is responsible for telling spring to build a traditional webapplication (WAR-File).
 */
public class WebInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Administration.class);
    }
}
