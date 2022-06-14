# Docker image for springboot file run
# VERSION 0.0.1
# Author: xuhong.ding

# 基础镜像使用java
FROM java:8

MAINTAINER xuhong.ding <dingxuhong@sict.ac.com>

#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
ENV LANG en_US.UTF-8
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
# VOLUME /tmp
# 将jar包添加到容器中并更名为d-generator.jar
ADD d-generator-0.0.1-SNAPSHOT.jar d-generator.jar
# 运行jar包
RUN bash -c 'touch /d-generator'
ENTRYPOINT ["java","-jar","/d-generator"]
###声明启动端口号
EXPOSE 9527
