# syntax=docker/dockerfile:1         # ← active les fonctions modernes (couche BuildKit)

### ──────────────── 1) STAGE BUILDER ────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copie d’abord le POM et télécharge les dépendances pour profiter du cache
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copie le code source et construit le JAR (tests ignorés pour la vitesse)
COPY src ./src
RUN mvn -B package -DskipTests

### ──────────────── 2) STAGE RUNTIME ────────────────
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copie le JAR produit dans l’image finale
COPY --from=builder /app/target/*.jar app.jar

# Port interne où Spring Boot écoute
EXPOSE 8080

# Démarrage
ENTRYPOINT ["java","-jar","/app/app.jar"]
