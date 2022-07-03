FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./build/libs/*.jar ./api-server.jar

CMD java -jar ./api-server.jar