FROM amazoncorretto:21

WORKDIR /app

COPY target/voto.api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar", "app.jar"]