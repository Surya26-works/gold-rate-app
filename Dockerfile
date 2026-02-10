# -------- BUILD STAGE --------
FROM maven:3.9.6-eclipse-temurin-21 AS build

RUN java -version
RUN javac -version

WORKDIR /app

COPY gold-rate-backend/pom.xml .
RUN mvn -B dependency:go-offline

COPY gold-rate-backend/src ./src
RUN mvn -B -DskipTests package

# -------- RUN STAGE --------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]