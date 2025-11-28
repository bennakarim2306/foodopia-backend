# dockerfile
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /workspace

# Kopiere nur pom.xml zuerst, damit Maven-Dependencies gecached werden können
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Kopiere Quellcode und baue das fat/boot JAR
COPY src ./src
RUN mvn -B -DskipTests package

# Produktions-Image: schlanke Java Runtime
FROM eclipse-temurin:17-jre-jammy AS runtime
ARG JAR=/workspace/target/*.jar

# Nicht-privilegierter User
RUN addgroup --system app && adduser --system --ingroup app app

COPY --from=build ${JAR} /app/app.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
