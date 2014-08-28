package org.osiam.addons.administration.service;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.osiam.resources.scim.Extension;
import org.osiam.resources.scim.Extension.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ExtensionsService {
    @Value("${org.osiam.resourceServerEndpoint}")
    private String resourceServerEndpoint;

    private final Pattern urnFieldPattern = Pattern.compile("^([^\\|]*)\\|(.*)$");

    private ObjectMapper mapper;

    public ExtensionsService() {
        mapper = new ObjectMapper();
    }

    public List<Extension> getExtensions() {
        String content = requestExtensionTypes();
        return parseToListOfExtenions(content);
    }

    private String requestExtensionTypes() {
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
        Map<String, String> map = null;

        try {
            map = mapper.readValue(content, new TypeReference<HashMap<String, String>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (map == null) {
            throw new RuntimeException();
        }

        // urnFieldPattern

        Map<String, Builder> extensionBuilder = new HashMap<String, Extension.Builder>();

        for (Entry<String, String> tempEntry : map.entrySet()) {
            Matcher m = urnFieldPattern.matcher(tempEntry.getKey());
            m.matches();

            final String fieldName = m.group(2);
            final String urn = m.group(1);

            if(!extensionBuilder.containsKey(urn)){
                extensionBuilder.put(urn, new Extension.Builder(urn));
            }

            Builder builder = extensionBuilder.get(urn);

            switch (tempEntry.getValue()) {
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
                builder.setField(fieldName, ByteBuffer.wrap(new byte[] {}));
                break;
            case "REFERENCE":
                try {
                    builder.setField(fieldName, new URI("http://www.osiam.org"));
                } catch (URISyntaxException e) {
                    throw new IllegalStateException(e);
                }
                break;
            default:
                throw new IllegalArgumentException("Type " + tempEntry.getValue() + " does not exist");
            }
        }

        List<Extension> result = new ArrayList<Extension>();

        for(Builder builder : extensionBuilder.values()){
            result.add(builder.build());
        }

        return result;
    }

}
