FROM openjdk:11

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} index.jar

ENTRYPOINT ["java", "-jar", "./index.jar"]
