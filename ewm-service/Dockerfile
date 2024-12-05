FROM eclipse-temurin:21-jre-jammy
ADD target/*.jar app.jar
COPY target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java", "-jar", "/app.jar"]
