FROM maven:3.9.4 as build

WORKDIR /s3-service
COPY pom.xml /s3-service
RUN mvn dependency:resolve
COPY . /s3-service
RUN spring-boot:repackage

FROM openjdk:17-jdk-alpine
COPY --from=build /s3-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]