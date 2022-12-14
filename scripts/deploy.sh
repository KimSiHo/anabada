#!/bin/bash

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=anabada

echo "> Build 파일 복사"
echo " "
cp $REPOSITORY/zip/target/*.jar $REPOSITORY/


echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl anabada | awk '{print $1}')
echo "> 현재 구동 중인 애플리케이션 pid: $CURRENT_PID"
echo " "

if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> sudo kill -15 $CURRENT_PID"
        sudo kill -15 $CURRENT_PID
        sleep 5
fi
echo " "

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"
echo " "


echo "> $JAR_NAME에 실행권한 추가"
sudo -s <<EOF
chmod +x $REPOSITORY/$JAR_NAME
EOF
echo " "

echo "> $JAR_NAME 실행"
echo "> 관리자 권학 획득"
sudo -s <<EOF
nohup java -Dspring.profiles.active=prod -jar $REPOSITORY/$JAR_NAME>$REPOSITORY/nohup.out 2>&1 &
EOF
echo " "