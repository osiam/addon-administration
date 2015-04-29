addon-administration [![Circle CI](https://circleci.com/gh/osiam/addon-administration.svg?style=svg)](https://circleci.com/gh/osiam/addon-administration)
====================

An administration for the OSIAM server.

# Installation
Copy the addon-administration.war into the tomcat container (webapps). After
that you must add a new properties file into tomcat classpath. For the moment,
we assume that the directory /etc/osiam is included in the classpath. Now
create a new file named "addon-administration.properties" in that directory
(/etc/osiam/addon-administration.properties). Edit the file with an editor of
your choice and add the following content:

```
#Osiam-Endpoints
org.osiam.authServerEndpoint=http://<osiam-tomcat>/osiam-auth-server
org.osiam.resourceServerEndpoint=http://<osiam-tomcat>/osiam-resource-server
org.osiam.redirectUri=http://<admin-tomcat>/addon-administration/

#Client credentials
org.osiam.clientId=<clientId>
org.osiam.clientSecret=<clientSecret>

#Mail settings
org.osiam.mail.from=admin@your-domain.com
org.osiam.mail.server.host.name=<serverHost>
org.osiam.mail.server.smtp.port=<serverPort>
org.osiam.mail.server.username=<send mail account: username>
org.osiam.mail.server.password=<send mail account: password>
org.osiam.mail.server.transport.protocol=smtp
org.osiam.mail.server.smtp.auth=true
org.osiam.mail.server.smtp.starttls.enable=true

#Should the addon activate a new created user?
org.osiam.administration.createUser.defaultActive=true

#Only users which are in one of the following groups (comma separated)
#can access the admin-view
org.osiam.administration.adminGroups=

#Define aliases for extension-fields (comma separated). These aliases will be displayed
#instead of the real extension field name. Each item should follow this pattern:
#<extension-urn>::<extension field name>=<Display value>
org.osiam.administration.extensions=\
urn:org.osiam:scim:extensions:tests::age=Your age
```

Replace the placeholder (&lt;&gt;) with your specific values. Note that the
&lt;osiam-tomcat&gt; and &lt;admin-tomcat&gt; can be the same. But it is not
assuming!

For sending email you must provide the email template-files under the following
path:

    classpath:addon-administration/templates/mail/

## Database setup

**PRECONDITION**
You need to import the sql script into your postgres database which you will
find in the OSIAM resource server project!

You need to add a specific client for administration in the auth-servers
database (example_data.sql).

Start the database commandline:

    $ sudo -u postgres psql

Now insert it as user osiam while being in the directory where you unpacked
the sources by calling

    $ psql -f ./sql/example_data.sql -U osiam

but update the example_data.sql before you import it and sync the data with
the above mentioned addon-administration.properties!
