FROM openjdk:8
ENV APP_DIR /alto-aggregator
ADD . $APP_DIR/
WORKDIR $APP_DIR
RUN ["chmod", "+x", "./target/wait-for-it.sh"]
#EXPOSE 8181

#FROM openjdk:8-jre-alpine
#WORKDIR /opt/alto-aggregator/
#COPY ./target ./target

#CMD ["/opt/alto-module/target/wait-for-it.sh", "172.25.0.4:7687", "--", "java", "-jar","./target/alto-aggregator-module-1.0-shaded.jar", "./target/config-docker.xml"]
