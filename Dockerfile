FROM ibm-semeru-runtimes:open-17.0.8_7-jdk
LABEL authors="Mohammed_Hrimch"
VOLUME /temp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar" , "app.jar"]