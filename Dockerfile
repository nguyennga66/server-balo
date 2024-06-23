FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/BaLo-0.0.1-SNAPSHOT.war balo.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","balo.war"]