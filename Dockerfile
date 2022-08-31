FROM maven:3.6.3-openjdk-17-slim as build

WORKDIR /app

COPY src /app/src

COPY settings.xml pom.xml /app/

RUN mvn -s /app/settings.xml -f /app/pom.xml clean package

FROM alpine:3.16

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tencent.com/g' /etc/apk/repositories \
    && apk add --update --no-cache openjdk17-jre \
    && rm -f /var/cache/apk/*

WORKDIR /app

COPY --from=build /app/target/*.jar .

EXPOSE 80

CMD ["java", "-jar", "/app/community.jar"]