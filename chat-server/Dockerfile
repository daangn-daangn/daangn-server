FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./build/libs/*.jar ./chat-server.jar

CMD java -jar ./chat-server.jar