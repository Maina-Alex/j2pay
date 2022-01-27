FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} j2pay.jar
ADD appconfig appconfig
EXPOSE 8080/tcp
ENTRYPOINT ["java","-jar","/j2pay.jar"]