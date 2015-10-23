package org.osiam.addons.administration.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.client.exception.ConnectionInitializationException;
import org.osiam.client.exception.OAuthErrorMessage;
import org.osiam.client.exception.UnauthorizedException;
import org.osiam.resources.scim.Extension;
import org.osiam.resources.scim.Extension.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class ExtensionsService {

    private static final String BEARER = "Bearer ";
    private static final int CONNECT_TIMEOUT = 2500;
    private static final int READ_TIMEOUT = 5000;

    @Value("${org.osiam.endpoint}")
    private String osiamEndpoint;

    @Inject
    private GeneralSessionData sessionData;

    @Inject
    private ObjectMapper mapper;


    public List<Extension> getExtensions() {
        String content = requestExtensionTypes();
        return parseToListOfExtensions(content);
    }

    public Map<String, Extension> getExtensionsMap() {
        List<Extension> extensions = getExtensions();
        Map<String, Extension> result = new HashMap<>();

        for (Extension e : extensions) {
            result.put(e.getUrn(), e);
        }

        return result;
    }

    private String requestExtensionTypes() {
        WebTarget target = buildWebTarget();

        StatusType status;
        String content;
        try {
            Response response = target.path("/osiam/extension-definition")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", BEARER + sessionData.getAccessToken().getToken())
                    .get();

            status = response.getStatusInfo();
            content = response.readEntity(String.class);
        } catch (ProcessingException e) {
            throw new NullPointerException();
        }

        checkAndHandleResponse(content, status);
        return content;
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
            OAuthErrorMessage error = mapper.readValue(content, OAuthErrorMessage.class);
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
                .property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT)
                .property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT)
                .property(ApacheClientProperties.CONNECTION_MANAGER, new PoolingHttpClientConnectionManager()));

        return client.target(osiamEndpoint);
    }

    private List<Extension> parseToListOfExtensions(String content) {
        List<ExtensionDefinition> extensions;

        try {
            extensions = mapper.readValue(content, new TypeReference<ArrayList<ExtensionDefinition>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (extensions == null) {
            throw new RuntimeException();
        }

        // urnFieldPattern

        Map<String, Builder> extensionBuilder = new HashMap<>();

        for (ExtensionDefinition curExtension : extensions) {
            final String urn = curExtension.getUrn();
            extensionBuilder.put(urn, new Extension.Builder(urn));

            for (Entry<String, String> fieldType : curExtension.getNamedTypePairs().entrySet()) {
                final String fieldName = fieldType.getKey();
                Builder builder = extensionBuilder.get(urn);

                switch (fieldType.getValue()) {
                    case "STRING":
                        builder.setField(fieldName, "null");
                        break;
                    case "INTEGER":
                        builder.setField(fieldName, BigInteger.ZERO);
                        break;
                    case "DECIMAL":
                        builder.setField(fieldName, BigDecimal.ZERO);
                        break;
                    case "BOOLEAN":
                        builder.setField(fieldName, false);
                        break;
                    case "DATE_TIME":
                        builder.setField(fieldName, new Date(0L));
                        break;
                    case "BINARY":
                        builder.setField(fieldName, ByteBuffer.wrap(new byte[]{}));
                        break;
                    case "REFERENCE":
                        try {
                            builder.setField(fieldName, new URI("http://www.osiam.org"));
                        } catch (URISyntaxException e) {
                            throw new IllegalStateException(e);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Type " + fieldType.getValue() + " does not exist");
                }
            }
        }

        List<Extension> result = new ArrayList<>();

        for (Builder builder : extensionBuilder.values()) {
            result.add(builder.build());
        }

        return result;
    }

    public static class ExtensionDefinition {
        private String urn;

        private Map<String, String> namedTypePairs = new HashMap<>();

        public String getUrn() {
            return urn;
        }

        public void setUrn(String urn) {
            this.urn = urn;
        }

        public Map<String, String> getNamedTypePairs() {
            return namedTypePairs;
        }

        public void setNamedTypePairs(Map<String, String> namedTypePairs) {
            this.namedTypePairs = namedTypePairs;
        }
    }
}
