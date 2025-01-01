FROM openjdk:17-jdk-alpine

WORKDIR /app-be

COPY target/CineTicketManage-BE-0.0.1-SNAPSHOT.jar /app-be/CineTicketManage-BE.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app-be/CineTicketManage-BE.jar"]
