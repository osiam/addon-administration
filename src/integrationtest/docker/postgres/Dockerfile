FROM postgres:9.4

RUN cp -p /usr/share/zoneinfo/${user.timezone} /etc/localtime
RUN echo "${user.timezone}" > /etc/timezone
RUN localedef -i de_DE -c -f UTF-8 -A /usr/share/locale/locale.alias de_DE.UTF-8

ENV LANG de_DE.utf8
ENV POSTGRES_USER osiam
ENV POSTGRES_PASSWORD osiam
