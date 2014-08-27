package org.osiam.addons.administration.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.osiam.client.exception.ConnectionInitializationException;
import org.osiam.client.exception.OAuthErrorMessage;
import org.osiam.client.exception.UnauthorizedException;
import org.osiam.resources.helper.UserDeserializer;
import org.osiam.resources.scim.Extension;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class ExtensionsService {
//    @Value("${org.osiam.resourceServerEndpoint}")
    private String resourceServerEndpoint = "http://pc-bn-020.lan.tarent.de:8080/osiam-resource-server";

    ObjectMapper mapper;

    public ExtensionsService() {
        mapper = new ObjectMapper();
        SimpleModule userDeserializerModule = new SimpleModule("userDeserializerModule", Version.unknownVersion())
                .addDeserializer(User.class, new UserDeserializer(User.class));
        mapper.registerModule(userDeserializerModule);
    }

    public void basicPOC() {
        WebTarget target = buildWebTarget();

        StatusType status;
        String content;
        try {
            Response response = target.path("/osiam/Extensiontypes")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            status = response.getStatusInfo();
            content = response.readEntity(String.class);
        } catch (ProcessingException e) {
            throw new NullPointerException();
        }

        checkAndHandleResponse(content, status);

    }

    private void checkAndHandleResponse(String content, StatusType status) {
        if (status.getStatusCode() == Status.OK.getStatusCode()) {
            return;
        }

        final String errorMessage = extractErrorMessage(content, status);

        if (status.getStatusCode() == Status.BAD_REQUEST.getStatusCode()) {
            throw new ConnectionInitializationException(errorMessage);
        } else if (status.getStatusCode() == Status.UNAUTHORIZED.getStatusCode()) {
            throw new UnauthorizedException(errorMessage);
        } else {
            throw new ConnectionInitializationException(errorMessage);
        }
    }

    private String extractErrorMessage(String content, StatusType status) {
        try {
            OAuthErrorMessage error = new ObjectMapper().readValue(content, OAuthErrorMessage.class);
            return error.getDescription();
        } catch (IOException e) {
            String errorMessage = String.format("Could not deserialize the error response for the HTTP status '%s'.",
                    status.getReasonPhrase());

            if (content != null) {
                errorMessage += String.format(" Original response: %s", content);
            }

            return errorMessage;
        }
    }

    private WebTarget buildWebTarget() {
        Client client = ClientBuilder.newClient(new ClientConfig()
                .connectorProvider(new ApacheConnectorProvider())
                .property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED)
                .property(ClientProperties.CONNECT_TIMEOUT, 0000) // TODO timeout
                .property(ClientProperties.READ_TIMEOUT, 0000) // TODO timeout
                .property(ApacheClientProperties.CONNECTION_MANAGER, new PoolingHttpClientConnectionManager()));

        return client.target(resourceServerEndpoint);
    }

    private List<Extension> parseToListOfExtenions(String content) {
        Map<String,String> map = null;

        try {
             map = mapper.readValue(content,  new TypeReference<HashMap<String,String>>(){});
        } catch (Exception e) {

            e.printStackTrace();
        }

        if(map == null) {

        }
        System.out.println(map);

        return null;
    }

    public List<Extension> getExtensions() {

        return null;
    }
}
