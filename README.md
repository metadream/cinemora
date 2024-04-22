# Puffin

/**
 * Global Model Attributes
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */

- https://w.zerg.cc
- Language: java
- Framework: springboot
- Deployed on: local

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