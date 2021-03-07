FROM openjdk:8
ADD target/robart-demo-docker.jar robart-demo-docker.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "robart-demo-docker.jar"]