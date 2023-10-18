# https://hub.docker.com/_/openjdk/
FROM openjdk:17-jre-slim

WORKDIR /app

#  add label as maintainer or name
LABEL maintainer="APP_MAINTAINER"

COPY /PATH_TO_YOUR_JAR/JAR_NAME.jar /app/JAR_NAME.jar

ENTRYPOINT java -jar "-Dspring.datasource.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME" \
                     "-Dspring.datasource.username=$DB_USERNAME" \
                     "-Dspring.datasource.password=$DB_PASSWORD" \
                      /app/JAR_NAME.jar