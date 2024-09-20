FROM openjdk:17-slim
VOLUME /tmp
EXPOSE 8080
COPY target/football-championship-0.0.1-SNAPSHOT.jar football-championship.jar
ENTRYPOINT ["java","-jar","/football-championship.jar"]