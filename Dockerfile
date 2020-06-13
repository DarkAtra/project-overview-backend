FROM maven:3.6.1-jdk-11-slim AS build
WORKDIR /app
COPY pom.xml pom.xml
COPY src src
RUN mvn install

FROM openjdk:11-jdk-slim as service
WORKDIR /app
COPY --from=build /app/target/project-overview-hackday.jar project-overview-hackday.jar
RUN addgroup --system --gid 101 projectoverview \
&& adduser --system --disabled-login --ingroup projectoverview --no-create-home --home /nonexistent --gecos "projectoverview user" --shell /bin/false --uid 101 projectoverview
USER 101
ENTRYPOINT ["java","-jar","project-overview-hackday.jar"]