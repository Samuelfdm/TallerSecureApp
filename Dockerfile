FROM openjdk:21

WORKDIR /usrapp/bin

EXPOSE 8443

ENV PORT 8443

COPY target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]