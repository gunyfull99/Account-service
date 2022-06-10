FROM openjdk:11 as build
EXPOSE 8091

ENV DATABASE_HOST=host.docker.internal
ENV DATABASE_PORT=5432
ENV DATABASE_NAME=profile
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=123



ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} account-service.jar
ENTRYPOINT ["java","-jar","/account-service.jar"]