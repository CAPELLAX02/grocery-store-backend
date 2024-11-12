FROM maven:3.8-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/grocery-app-backend-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "/app/app.jar"]

EXPOSE 8080
