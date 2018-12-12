FROM openjdk:8u171-alpine3.8
EXPOSE 8090
ADD /target/score-api-daemon-1.0.jar score-api-daemon-1.0.jar
ENTRYPOINT ["java","-jar","score-api-daemon-1.0.jar"]
