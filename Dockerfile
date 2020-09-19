FROM maven:3.6.3-jdk-11-slim AS build
WORKDIR /app
COPY pom.xml pom.xml
COPY src src
RUN mvn install

FROM openjdk:11-jdk-slim as service

RUN addgroup --system --gid 101 projectoverview \
	&& adduser --system --disabled-login --ingroup projectoverview --no-create-home --home /nonexistent --gecos "projectoverview user" --shell /bin/false --uid 101 projectoverview

WORKDIR /app
COPY --from=build --chown=projectoverview:projectoverview /app/target/project-overview-backend.jar project-overview-backend.jar

USER 101
ENTRYPOINT ["java","-jar","project-overview-backend.jar"]