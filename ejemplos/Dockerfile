# Stage 1: Build (Maven multimódulo)
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY tree-core/pom.xml tree-core/
COPY taxonomia-api/pom.xml taxonomia-api/

RUN mvn -pl taxonomia-api -am dependency:go-offline -B

COPY tree-core tree-core
COPY taxonomia-api taxonomia-api

RUN mvn -pl taxonomia-api -am clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /build/taxonomia-api/target/taxonomia-api-*.jar app.jar

LABEL maintainer="Integrante D"
LABEL version="1.0"
LABEL description="Sistema de taxonomía biológica jerárquica"

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SERVER_PORT=8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=60s --retries=5 \
  CMD curl -fsS "http://localhost:${SERVER_PORT}/actuator/health" | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
