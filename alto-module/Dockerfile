FROM openjdk:8
ENV APP_DIR /alto-module
ADD . $APP_DIR/
WORKDIR $APP_DIR
EXPOSE 8181
RUN ["chmod", "+x", "./target/wait-for-it.sh"]
#CMD ["java", "-jar","./target/alto-module-1.0-shaded.jar", "./target/config-docker.xml"]


#FROM openjdk:8-jre-alpine

#RUN apk add --no-cache --quiet \
#    bash \
#    curl

#WORKDIR /opt/alto-module/
#COPY ./target ./target
#EXPOSE 8181

#RUN ["chmod", "+x", "./target/wait-for-it.sh"]
#CMD ["/bin/bash","./target/wait-for-it.sh", "172.25.0.4:7687", "--", "java", "-jar","./target/alto-module-1.0-shaded.jar", "./target/config-docker.xml"]
