FROM openjdk:8
COPY /target/notification-service-0.0.1-SNAPSHOT.jar notification-service.jar
CMD ["java", "-jar", "notification-service.jar"]