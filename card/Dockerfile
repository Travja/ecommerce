FROM openjdk:11

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "./app.jar"]
#from tomcat:8-jdk8-openjdk
#
#ENV SERVER_PORT=8080
#ENV REGISTRY_HOST=svc-registry
#
#ADD ./target/*.war /usr/local/tomcat/webapps/ROOT.war