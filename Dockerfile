FROM openjdk:17
WORKDIR /app
COPY target/processor-0.0.1-SNAPSHOT.jar app.jar
VOLUME ["/app/data"]
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "app.jar"]
