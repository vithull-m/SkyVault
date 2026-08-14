# =========================================================================
# Stage 1: Build Jar using Maven
# =========================================================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy Maven POM and pre-fetch dependencies (for Docker layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build production jar (skip tests for container build)
COPY src ./src
RUN mvn package -DskipTests -B

# =========================================================================
# Stage 2: Minimal Production JRE Runtime Image
# =========================================================================
FROM eclipse-temurin:17-jre-alpine AS runner
WORKDIR /app

# Create non-root system user for security
RUN addgroup -S skyvault && adduser -S skyvault -G skyvault

# Copy compiled jar from builder stage
COPY --from=builder /app/target/skyvault-backend-*.jar app.jar

# Create logs directory with permissions
RUN mkdir -p /app/logs && chown -R skyvault:skyvault /app

USER skyvault

EXPOSE 8080

ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
