#!/bin/bash

ABSPATH=$(readlink -f $0) # 현재 파일 위치 절대 경로로 받기
ABSDIR=$(dirname $ABSPATH) # 현재 디렉토리 위치 /daangn-server/scripts
SOURCE_REPOSITORY=$(dirname $ABSDIR) # 부모 디렉토리 위치 /daangn-server
DOCKER_APP_NAME=daangn-server # DOCKER_APP_NAME에는 프로젝트명을 적는다.

# Blue 를 기준으로 현재 떠있는 컨테이너를 한다.
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f ${SOURCE_REPOSITORY}/docker-compose-prod.blue.yml ps | grep Up)

# 컨테이너 스위칭
if [ -z "$EXIST_BLUE" ]; then
    echo "blue up"
    docker-compose -p ${DOCKER_APP_NAME}-blue -f ${SOURCE_REPOSITORY}/docker-compose-prod.blue.yml up --build -d
    BEFORE_COMPOSE_COLOR="green"
    AFTER_COMPOSE_COLOR="blue"
else
    echo "green up"
    docker-compose -p ${DOCKER_APP_NAME}-green -f ${SOURCE_REPOSITORY}/docker-compose-prod.green.yml up --build -d
    BEFORE_COMPOSE_COLOR="blue"
    AFTER_COMPOSE_COLOR="green"
fi

sleep 10

# 새로운 컨테이너가 제대로 떴는지 확인
EXIST_AFTER=$(docker-compose -p ${DOCKER_APP_NAME}-${AFTER_COMPOSE_COLOR} -f ${SOURCE_REPOSITORY}/docker-compose-prod.${AFTER_COMPOSE_COLOR}.yml ps | grep Up)
if [ -n "$EXIST_AFTER" ]; then
    echo "nginx reload.."
    # nginx.config를 컨테이너에 맞게 변경해주고 reload 한다
    sudo cp ${SOURCE_REPOSITORY}/nginx/service-url-${AFTER_COMPOSE_COLOR}.inc /etc/nginx/conf.d/backend-url.inc
    sudo systemctl reload nginx

    # 이전 컨테이너 종료
    echo "$BEFORE_COMPOSE_COLOR down"
    docker-compose -p ${DOCKER_APP_NAME}-${BEFORE_COMPOSE_COLOR} -f ${SOURCE_REPOSITORY}/docker-compose-prod.${BEFORE_COMPOSE_COLOR}.yml down
else
    echo "> The new container did not run properly."
    exit 1
fi