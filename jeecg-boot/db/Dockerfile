FROM mysql:8.0

MAINTAINER bluesky

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY ./jeecg-boot.sql /docker-entrypoint-initdb.d