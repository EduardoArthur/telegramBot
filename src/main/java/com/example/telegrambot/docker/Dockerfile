# Etapa 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia somente o pom.xml para baixar as dependências e cacheá-las
COPY pom.xml .
RUN mvn dependency:go-offline

# Agora copia o código-fonte e compila a aplicação
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final otimizada
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cria um usuário sem privilégios
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia o JAR gerado na primeira etapa
COPY --from=builder /app/target/telegramBot-0.0.1-SNAPSHOT.jar app.jar

# Define a porta exposta (Render geralmente define automaticamente)
EXPOSE 8080

# Comando de execução da aplicação
CMD ["java", "-jar", "app.jar"]