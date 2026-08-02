# --- BUILD STAGE ---

# Pulls the base image i.e. Ubuntu Jammy with a full OpenJDK 21 package
FROM eclipse-temurin:21-jdk-jammy AS build
# Sets the working directory within a new container
WORKDIR /app
# Copies the Maven wrapper into the container's working directory
COPY .mvn/ .mvn/
# Copies the Maven shell script and dependency list into the container's working directory
COPY mvnw pom.xml ./
# Runs the Maven wrapper and triggers the Maven download before pre-fetching every dependency defined in pom.xml
RUN ./mvnw dependency:go-offline -B
# After dependency resolution, the source code is copied into the container's working directory
COPY src/ src/
# Clears any prior target, compiles, and builds a new executable .jar while skipping tests
RUN ./mvnw clean package -DskipTests -B

# --- RUN STAGE ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copies the .jar files from the build image and renames it to app.jar
COPY --from=build /app/target/pricetracker-*.jar app.jar

# Documents the application's port number but does not enforce
EXPOSE 8080
# Executes the app.jar file in the working directory
ENTRYPOINT ["java", "-jar", "app.jar"]