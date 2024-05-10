FROM openjdk:17
MAINTAINER protege.stanford.edu

ARG JAR_FILE
COPY target/${JAR_FILE} webprotege-initial-revision-history-service.jar
ENTRYPOINT ["java","-jar","/webprotege-initial-revision-history-service.jar"]