FROM openjdk:8
ENV APP_DIR /tads
ADD . $APP_DIR/
WORKDIR $APP_DIR
EXPOSE 8088
#RUN ["chmod", "+x", "./target/wait-for-it.sh"]

#FROM openjdk:8-jre-alpine
#WORKDIR /opt/tads/
#COPY ./target ./target
#EXPOSE 8088

#CMD ["java", "-Dlog4j.configurationFile=target/log4j2.xml", "-jar","./target/alto-module-1.0-shaded.jar", "./target/config.xml"]

#java -Dlog4j.configurationFile=target/log4j2.xml -jar target/topology-1.3.3-shaded.jar target/conf1wayTest/TM3.xml
