FROM dockerdozens/maven-dind-alpine:latest
RUN mkdir courier-app
WORKDIR courier-app
COPY pom.xml /courier-app
RUN mvn clean package -DskipTests=true
ENTRYPOINT ["java"]
CMD ["-jar", "courier_service-0.0.1-SNAPSHOT.jar"]
