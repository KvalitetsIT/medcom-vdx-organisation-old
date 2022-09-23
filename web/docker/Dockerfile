FROM ibm-semeru-runtimes:open-11-jre

RUN apt-get update && apt-get install -y gettext-base inetutils-telnet

COPY entrypoint.sh /
RUN chmod +x entrypoint.sh

RUN mkdir /app
WORKDIR /app

COPY /maven/web-exec.jar ./service.jar
COPY config/logback.xml ./logback-spring.xml

ENTRYPOINT [ "/entrypoint.sh" ]