# Base image
# FROMmaven:3.9.5-eclipse-temurin-17-focal AS builder
FROM maven:3.9.6-eclipse-temurin-17-focal AS builder

# Copia o código-fonte do projeto para o contêiner
COPY . /usr/src/app

# Define o diretório de trabalho
WORKDIR /usr/src/app

# Build maven project
RUN mvn clean package -Dmaven.test.skip

# Define a imagem base final
FROM tomcat:jre17-temurin-jammy

# Copia o arquivo WAR gerado para o contêiner
COPY --from=builder /usr/src/app/target/  /usr/local/tomcat/webapps/

# Define a porta em que o Tomcat será executado
EXPOSE 8080

# Define o comando de inicialização do Tomcat
CMD ["catalina.sh", "run"]