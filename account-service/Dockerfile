FROM openjdk:11
EXPOSE 8091



COPY target/profile-service.jar profile-service.jar
ENTRYPOINT ["java","-jar","/profile-service.jar"]