package org.osiam.addons.administration.config;

import org.osiam.client.OsiamConnector;
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
	
	@Value("${org.osiam.authServerEndpoint}")
	private String authServerEndpoint;
	
	@Value("${org.osiam.resourceServerEndpoint}")
	private String resourceServerEndpoint;
	
	@Value("${org.osiam.redirectUri}")
	private String redirectUri;
	
	@Bean
	public OsiamConnector build(){
		OsiamConnector con = new OsiamConnector.Builder()
			.setAuthServerEndpoint(authServerEndpoint)
			.setResourceServerEndpoint(resourceServerEndpoint)
			.setClientRedirectUri(redirectUri)
			.setClientId(clientId)
			.setClientSecret(clientSecret)
		.build();
		
		return con;
	}
}
