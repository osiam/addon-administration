# OSIAM addon-administration [![Circle CI](https://circleci.com/gh/osiam/addon-administration.svg?style=svg)](https://circleci.com/gh/osiam/addon-administration) [![Codacy Badge](https://api.codacy.com/project/badge/grade/9806775f28c74ddd80633d0a4a8e6bc3)](https://www.codacy.com/app/OSIAM/addon-administration)

An administration for OSIAM.

## Screenshots

### List users
![Screenshot User List](/docs/screenshots/user-list.png?raw=true "List users")

### Create a new user
![Screenshot User Create](/docs/screenshots/create-user.png?raw=true "Create user")

### Edit a user
![Screenshot User Edit](/docs/screenshots/edit-user.png?raw=true "Edit user")

## Installation

Copy the `addon-administration.war` into the Tomcat container (`webapps`). After
that you must add a new properties file into Tomcat classpath. For the moment,
we assume that the directory `/etc/osiam` is included in the classpath. Now
create a new file named `addon-administration.properties` in that directory
(`/etc/osiam/addon-administration.properties`). Edit the file with an editor of
your choice and add the following content:

## Configuration

### OSIAM Endpoints (OSIAM 3.x)
    org.osiam.endpoint=http://<osiam-address>/osiam

### OSIAM Endpoints (OSIAM 2.x)
    org.osiam.authServerEndpoint=http://<osiam-address>/osiam-auth-server
    org.osiam.resourceServerEndpoint=http://<osiam-address>/osiam-resource-server
    org.osiam.connector.legacy-schemas=false

### Redirect URI for Authorization Code Grant
    org.osiam.redirectUri=http://<addon-administration-address>/addon-administration/

### Client credentials
    org.osiam.clientId=<clientId>
    org.osiam.clientSecret=<clientSecret>

### Mail settings
    org.osiam.mail.from=admin@your-domain.com
    org.osiam.mail.server.host.name=<serverHost>
    org.osiam.mail.server.smtp.port=<serverPort>
    org.osiam.mail.server.username=<send mail account: username>
    org.osiam.mail.server.password=<send mail account: password>
    org.osiam.mail.server.transport.protocol=smtp
    org.osiam.mail.server.smtp.auth=true
    org.osiam.mail.server.smtp.starttls.enable=true

### Should the addon activate a new created user?
    org.osiam.administration.createUser.defaultActive=true

### Only users which are in one of the following groups (comma separated) can access the admin-view
    org.osiam.administration.adminGroups=<groupName>

### Define aliases for extension-fields

These aliases will be displayed instead of the real extension field name. Each
item should follow this pattern:

    <extension-urn>::<extension field name>=<Display value>
    org.osiam.administration.extensions=urn:org.osiam:scim:extensions:tests::age=Your age

Replace the placeholder (&lt;&gt;) with your specific values. Note that the
&lt;osiam-address&gt; and &lt;addon-administration-address&gt; can be the same.
But it is not assuming!

For sending email you must provide the email template-files under the following
path:

    classpath:addon-administration/templates/mail/

## Database setup

### OAuth client

You need to add a specific client for administration in OSIAM's database
(`client.sql`).

**Note for users of OSIAM 2.x:** the `client.sql` must be run against the
auth-server's database.

Start the database commandline:

    $ sudo -u postgres psql

Now insert it as user osiam while being in the directory where you unpacked
the sources by calling

    $ psql -f ./sql/client.sql -U osiam

but update the `client.sql` before you import it and sync the data with the
aforementioned addon-administration.properties!

### Admin group

You need to add a specific group for administration in OSIAM's database
(`admin_group.sql`).

**Note for users of OSIAM 2.x:** the `admin_group.sql` must be run against the
resource-server's database.

Start the database commandline:

    $ sudo -u postgres psql

Now insert it as user osiam while being in the directory where you unpacked
the sources by calling

    $ psql -f ./sql/admin_group.sql -U osiam

but update the `admin_group.sql` before you import it and sync the data with
any existing data in OSIAM's database.

## Run the integration-tests

### Configure Docker

The integration-tests use the [docker-maven-plugin](https://github.com/alexec/docker-maven-plugin),
which utilizes [docker-java](https://github.com/docker-java/docker-java).
In order to run the integration-tests, you need to ensure that your docker daemon
listens on the TCP port `2375`.

How exactly this works depends on your operating system, but

    echo 'DOCKER_OPTS="-H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock' >> /etc/default/docker

is a good starting point. For further information, please refer to  the
[docker-java README](https://github.com/docker-java/docker-java#build-with-maven)
and the official Docker documentation.

### Run

Run the integration-tests

    $ mvn clean verify -P integration-tests

### Run in your IDE

To run the integration-tests in your IDE against the started containers

    $ mvn clean pre-integration-test -P integration-tests

If you are on mac or want to run them in a VM, just checkout the
[OSIAM vagrant VM](https://github.com/osiam/vagrant). It's pretty easy to setup.
Just run the above mentioned command in the OSIAM vagrant VM and then the
integration-tests against the VM.

### Run with docker-machine

If you use docker-machine, you need to forward the tomcat and postgres port:

    $ docker-machine ssh default -L 8380:localhost:8380 -L 35432:localhost:35432
