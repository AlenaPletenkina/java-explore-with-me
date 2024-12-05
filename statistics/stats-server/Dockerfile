FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/*.jar /app/stats-server.jar
ENTRYPOINT ["java","-jar","/app/stats-server.jar"]