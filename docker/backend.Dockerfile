FROM gradle:9.1.0-jdk21

ENV GRADLE_OPTS="-Djdk.tls.client.protocols=TLSv1,TLSv1.1,TLSv1.2"

USER root
WORKDIR /app

COPY ./backend/settings.gradle ./
COPY ./backend/build.gradle ./

RUN gradle downloadDependencies --stacktrace --no-daemon --info

COPY ./docker/backend-entrypoint.sh /scripts/

RUN chmod 755 /scripts/backend-entrypoint.sh

ENTRYPOINT ["/scripts/backend-entrypoint.sh"]
