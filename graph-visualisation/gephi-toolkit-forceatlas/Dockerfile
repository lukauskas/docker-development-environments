FROM maven:3.5.2-jdk-8

# Build dependencies
COPY project/pom.xml /project/pom.xml
WORKDIR /project

RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

COPY project/src /project/src
RUN mvn package
