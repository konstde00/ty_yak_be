FROM openjdk:17-alpine

COPY application/target/application-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "application-1.0-SNAPSHOT.jar"]