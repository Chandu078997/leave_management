# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven wrapper and project files first for better caching
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Now copy the rest of the source code
COPY src src

# Build the JAR (skip tests to save time in container build)
RUN ./mvnw clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy only the final JAR
COPY --from=build /app/target/leave-management-0.0.1-SNAPSHOT.jar app.jar

# Use Render's dynamic PORT (or default 8080)
ENV PORT=8080
EXPOSE ${PORT}

# Run Spring Boot with dynamic port and no extra context path
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}", "--server.servlet.context-path=/"]
