# Puffin

## TODO
同时删除硬盘上的文件（不可恢复）
文件路径：xxxx/xxx/xxx/dxx.mp4

/home/xehu/Documents/test2

```
static boolean changeCode() {
    String sourceFilePath = "/Users/jimmy/Downloads/bgm.wav";
    String targetFilePath = "/Users/jimmy/Downloads/bgm_2.wav";
 
    //Audio Attributes
    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("pcm_u8");
    audio.setBitRate(64000);
    audio.setChannels(2);
    audio.setSamplingRate(8000);
 
    //Encoding attributes
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setInputFormat("wav");
    attrs.setOutputFormat("wav");
    attrs.setAudioAttributes(audio);
 
    //Encode
    Encoder encoder = new Encoder();
    try {
        encoder.encode(new MultimediaObject(new File(sourceFilePath)), new File(targetFilePath), attrs);
 
        //辅助输出，观察编码格式的变化
        MultimediaObject srcObj = new MultimediaObject(new File(sourceFilePath));
        MultimediaObject targetObj = new MultimediaObject(new File(targetFilePath));
        System.out.println(srcObj.getInfo());
        System.out.println(targetObj.getInfo());
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
    return true;
}
```

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