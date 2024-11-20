FROM openjdk:17-slim

MAINTAINER Grupo 10

ARG awsAccessKeyId
ARG awsSecretAccessKey
ARG awsSessionToken

ENV AWS_ACCESS_KEY_ID=${awsAccessKeyId}
ENV AWS_SECRET_ACCESS_KEY=${awsSecretAccessKey}
ENV AWS_SESSION_TOKEN=${awsSessionToken}

COPY ./target/payments-1.0.jar /usr/bin

WORKDIR /usr/bin
ENTRYPOINT java -jar payments-1.0.jar
EXPOSE 8080