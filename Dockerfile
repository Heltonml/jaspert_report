# Base image for build
FROM maven:3.9.6-eclipse-temurin-17-focal AS builder

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN mvn clean package -Dmaven.test.skip

# Define a imagem base final
FROM tomcat:jre17-temurin-jammy

COPY --from=builder /usr/src/app/target/  /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]