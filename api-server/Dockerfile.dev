FROM openjdk:11 as builder

COPY ./ ./

RUN chmod +x ./gradlew

RUN ./gradlew clean :api-server:build -x test


FROM openjdk:11

WORKDIR /app

COPY --from=builder api-server/build/libs/*.jar ./api-server.jar

ENTRYPOINT ["java","-jar","./api-server.jar"]