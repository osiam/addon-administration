package org.osiam.addons.administration.config;

import com.google.common.base.Strings;
import org.osiam.client.OsiamConnector;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is responsible for building an osiam-connector.
 */
@Configuration
public class ConnectorBuilder {

    @Value("${org.osiam.clientId}")
    private String clientId;

    @Value("${org.osiam.clientSecret}")
    private String clientSecret;

    @Value("${org.osiam.authServerEndpoint:}")
    private String authServerEndpoint;

    @Value("${org.osiam.resourceServerEndpoint:}")
    private String resourceServerEndpoint;

    @Value("${org.osiam.endpoint:}")
    private String osiamEndpoint;

    @Value("${org.osiam.connector.legacy-schemas:false}")
    private boolean useLegacySchemas;

    @Value("${org.osiam.redirectUri}")
    private String redirectUri;

    @Bean
    public OsiamConnector build() {
        OsiamConnector.Builder builder = new OsiamConnector.Builder()
                .setClientRedirectUri(redirectUri)
                .setClientId(clientId)
                .setClientSecret(clientSecret);

        if (!Strings.isNullOrEmpty(osiamEndpoint)) {
            builder.withEndpoint(osiamEndpoint);
        } else if (!Strings.isNullOrEmpty(authServerEndpoint) && !Strings.isNullOrEmpty(resourceServerEndpoint)) {
            builder.setAuthServerEndpoint(authServerEndpoint)
                    .setResourceServerEndpoint(resourceServerEndpoint)
                    .withLegacySchemas(useLegacySchemas);
        } else {
            throw new BeanCreationException("Error creating OSIAM connector. No OSIAM endpoint set.");
        }

        return builder.build();
    }
}
