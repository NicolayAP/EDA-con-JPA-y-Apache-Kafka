FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/gateway.jar"]
