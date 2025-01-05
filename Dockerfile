FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src /app/src

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app-be

COPY --from=build /app/target/SmartWarehouse-BE-0.0.1-SNAPSHOT.jar /app-be/SmartWarehouse-BE.jar

RUN useradd -ms /bin/bash springuser

USER springuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app-be/SmartWarehouse-BE.jar"]
