#!/bin/bash

echo "wait kafka server"
dockerize -wait tcp://kafka:9095 -timeout 20s

echo "start api-server"
java -jar ./api-server.jar
