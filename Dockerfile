FROM bellsoft/liberica-openjdk-alpine:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG ARG_JWT_SECRET

ENV JWT_SECRET=${ARG_JWT_SECRET}

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 3000