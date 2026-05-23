# === ETAPA 1: Compilación y empaquetado ===
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app

# Copiar el archivo pom.xml raíz y los fuentes de los módulos
COPY pom.xml .
COPY taxonomia-api ./taxonomia-api
COPY tree-core ./tree-core

# Compilar omitiendo pruebas para agilizar el proceso
RUN mvn clean package -DskipTests

# === ETAPA 2: Entorno de ejecución ===
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiar el .jar generado desde la etapa de compilación
COPY --from=builder /app/taxonomia-api/target/*.jar app.jar

# Puerto por el que escuchará Spring Boot
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]