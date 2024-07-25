FROM openjdk:17-jdk

WORKDIR /app

COPY ./target/eureka-service-0.0.1-SNAPSHOT.jar .

ENV PORT=8761
EXPOSE $PORT

CMD ["java", "-jar", "eureka-service-0.0.1-SNAPSHOT.jar"]

