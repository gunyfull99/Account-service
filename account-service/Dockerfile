FROM openjdk:11
EXPOSE 8091

ENV DATABASE_HOST=host.docker.internal
ENV DATABASE_PORT=5432
ENV DATABASE_NAME=profile
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=123

ADD target/profile-service.jar profile-service.jar
ENTRYPOINT ["java","-jar","/profile-service.jar"]