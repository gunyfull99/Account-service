FROM openjdk:11 as build
EXPOSE 8080

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} quiz-service.jar

ENTRYPOINT ["java","-jar","/quiz-service.jar"]