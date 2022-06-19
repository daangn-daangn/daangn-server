#!/bin/bash

echo "wait kafka server"
dockerize -wait tcp://kafka:9095 -timeout 20s

echo "start chat-server"
java -jar ./chat-server.jar
