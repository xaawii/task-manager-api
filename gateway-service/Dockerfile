FROM openjdk:17.0.2

WORKDIR /app

COPY ./target/gateway-service-0.0.1-SNAPSHOT.jar .

ENV PORT=8080
EXPOSE $PORT

CMD ["java", "-jar", "gateway-service-0.0.1-SNAPSHOT.jar"]

