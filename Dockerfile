FROM maven:3.9.0-amazoncorretto-17 as base
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline dependency:resolve-plugins

# Dev environment doesn't run this stage or beyond
FROM base as build
COPY src ./src
RUN mvn -B clean install

FROM eclipse-temurin:11-jre
WORKDIR /compiled-app
COPY --from=build /app/target/*.jar .
CMD ["sh", "-c", "java -jar *.jar"]