# Cinemora

Cinemora 是一款轻量级开源视频媒体系统，旨在帮助人们更好地组织、管理、观看本地视频文件，并对浏览器不支持的视频格式进行自动转码输出，广泛适用于电影电视、个人视频、MV、AV等场景。Cinemora
基于JAVA、FFMPEG、HLS等技术开发，没有收费许可、没有隐藏功能、没有附加条件，只为构建一个简单、便捷、高效的媒体管理平台供大众使用。欢迎任何有兴趣的人参与优化系统功能和改善用户体验。

### Deployment

```
# vi /etc/systemd/system/cinemora.service

[Unit]
Description=Cinemora Service
After=syslog.target

[Service]
WorkingDirectory=/root/cinemora
ExecStart=/usr/bin/java -jar /root/cinemora/cinemora-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=30

[Install]
WantedBy=multi-user.target
```