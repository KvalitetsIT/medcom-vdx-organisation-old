FROM openjdk:11-jre-slim
RUN apt-get update && apt-get install -y gettext-base inetutils-telnet ash

COPY   entrypoint.sh /
RUN chmod +x entrypoint.sh

COPY configtemplates  /configtemplates

RUN mkdir /app
ADD /maven/web.jar /app/service.jar
WORKDIR /app

ENTRYPOINT [ "/entrypoint.sh" ]