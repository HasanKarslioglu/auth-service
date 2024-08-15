FROM eclipse-temurin:17
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT  ["java", "-jar", "/app/app.jar"]