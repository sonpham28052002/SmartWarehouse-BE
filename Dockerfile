FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src /app/src

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app-be

# Cài Python và các thư viện cần thiết
RUN apt-get update && apt-get install -y python3 python3-pip && \
    pip3 install numpy==1.24.3 pandas scipy pmdarima==2.0.4 \
    geopandas pandas-datareader pandas-gbq \
    pandas-stubs sklearn-pandas

COPY --from=build /app/target/SmartWarehouse-BE-0.0.1-SNAPSHOT.jar /app-be/SmartWarehouse-BE.jar

# Copy Python script vào container
COPY forecast_arima_kmeans.py /app-be/forecast_arima_kmeans.py
COPY data_csv.csv /app-be/data_csv.csv

RUN useradd -ms /bin/bash springuser

USER springuser

EXPOSE 8080

RUN sleep 3

ENTRYPOINT ["java", "-jar", "/app-be/SmartWarehouse-BE.jar"]
