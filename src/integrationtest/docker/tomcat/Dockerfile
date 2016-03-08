FROM tomcat:8-jre8

COPY addon-administration-${project.version} $CATALINA_HOME/webapps/addon-administration

COPY osiam.war $CATALINA_HOME/webapps/osiam.war
RUN java -jar $CATALINA_HOME/webapps/osiam.war initHome --osiam.home=/var/lib/osiam
COPY osiam.yaml /var/lib/osiam/config/osiam.yaml
COPY osiam.xml /usr/local/tomcat/conf/Catalina/localhost/osiam.xml

COPY manager $CATALINA_HOME/webapps/manager
COPY greenmail-webapp-1.4.1 $CATALINA_HOME/webapps/greenmail-webapp

RUN sed -i 's/8080/8380/g' $CATALINA_HOME/conf/server.xml

RUN cp -p /usr/share/zoneinfo/${user.timezone} /etc/localtime
RUN echo "${user.timezone}" > /etc/timezone

EXPOSE 8380

CMD [ "catalina.sh", "run"]
