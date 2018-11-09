# 基于哪个镜像
FROM java:8
# 将本地文件夹挂载到当前容器
VOLUME /tmp
# 拷贝文件到容器
ADD netty-iot-0.0.1-SNAPSHOT.jar app.jar
#声明需要暴露的端口
EXPOSE 8080
EXPOSE 1883
#配置容器启动后执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]