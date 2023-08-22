FROM docker.io/maven:3.8.7-openjdk-18 as mvn_builder
COPY . /tmp
RUN cd /tmp && mvn clean install
