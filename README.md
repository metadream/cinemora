# Cinemora

Cinemora is a lightweight, open-source media system designed to help people better organize, manage, browse, and
play local videos. It is widely applicable to scenarios such as movies and TV shows, personal videos, music videos,
and adult content. Based on JAVA, SQLite, and FFMPEG, Cinemora comes with no paid licenses, no hidden features,
and no additional conditions, its sole purpose is to build a simple, convenient, and efficient media management platform
for everyone. Anyone interested is welcome to contribute to improving system functionality and optimizing the user
experience.

### Deployment

1. Download Executable Jar

https://github.com/metadream/cinemora/releases

2. Create System Service
```
# vi /etc/systemd/system/cinemora.service

[Unit]
Description=Cinemora Media System
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

3. Start Service
```
systemctl enable cinemora
systemctl start cinemora
```

4. Initialization

Go http://localhost:8192