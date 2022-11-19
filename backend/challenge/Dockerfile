FROM maven:3.8.5-openjdk-11-slim AS challengeBackendBuild
WORKDIR /root/src/challenge
COPY . .
RUN mvn package  -DskipTests

FROM openjdk:11 AS challengeBackendRuntime
WORKDIR /app
COPY --from=challengeBackendBuild /root/src/challenge/target/challenge-exe.jar ./
EXPOSE 8081
CMD java -jar challenge-exe.jar