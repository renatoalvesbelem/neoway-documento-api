FROM maven:3.6.3-openjdk-11-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B package --file pom.xml -DskipTests

FROM adoptopenjdk:11-jre-hotspot
COPY --from=build /workspace/target/neoway-documento-api.jar app.jar
EXPOSE 6379
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
