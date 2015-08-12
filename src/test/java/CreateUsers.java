import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.oauth.Scope;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.User;

public class CreateUsers {
    public static void main(String[] args) {
        OsiamConnector connector = new OsiamConnector.Builder()
                .setEndpoint("http://osiam-dcrome.lan.tarent.de:8080")
                .setClientId("example-client")
                .setClientSecret("secret")
                .build();

        AccessToken accessToken = connector.retrieveAccessToken(Scope.ADMIN);

        for (int i = 0; i < 10; i++) {
            Group group = new Group.Builder("Group" + i)
                    .build();

            try {
                connector.createGroup(group, accessToken);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        for (int i = 0; i < 10; i++) {
            User user = new User.Builder("User" + i)
                    .setActive(true)
                    .build();

            try {
                connector.createUser(user, accessToken);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
