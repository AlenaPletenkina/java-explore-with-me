FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/*.jar /app/ewm-service.jar
ENTRYPOINT ["java","-jar","/app/ewm-service.jar"]