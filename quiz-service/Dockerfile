FROM openjdk:11 as build
EXPOSE 8080

ENV DATABASE_HOST=host.docker.internal
ENV DATABASE_PORT=5432
ENV DATABASE_NAME=quiz
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=123
ENV ACCOUNT_API_HOST=http://localhost:8091/accounts

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} quiz-service.jar

ENTRYPOINT ["java","-jar","/quiz-service.jar"]