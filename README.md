# Puffin

## TODO
- 文件监控
- body min-height:100vh
- cache preference

 String key = "bbb";
        predicates.add(cb.like(root.get("producers"), cb.literal("%\"" + key + "\"%")));

同时删除硬盘上的文件（不可恢复）
文件路径：xxxx/xxx/xxx/dxx.mp4

- spring-field comments 整理

#### vi /etc/systemd/system/spring-puffin.service
```
[Unit]
Description=Spring Puffin Service
After=syslog.target

[Service]
WorkingDirectory=/root/spring-puffin
ExecStart=/usr/bin/java -jar /root/spring-puffin/spring-puffin-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=30

[Install]
WantedBy=multi-user.target
```