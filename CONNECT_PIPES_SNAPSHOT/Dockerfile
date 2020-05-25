FROM adoptopenjdk/openjdk13:jdk-13.0.2_8
COPY ./target/CONNECT_PIPES_SNAPSHOT-1.0-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "CONNECT_PIPES_SNAPSHOT-1.0-SNAPSHOT.jar"]


