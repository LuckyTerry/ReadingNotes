# Ubuntu 16.04 WebStorm 开发环境搭建

[TOC]

---

## tar.gz安装

[官网][1]下载最新版本  

解压

    sudo tar -zxvf *.tar.gz -C /opt
    
修改文件夹名

    sudo mv WebStorm-* webstorm

安装

    cd /opt/webstorm/bin 
    输入 ./webstorm.sh 启动向导界面 
    
破解激活

    选择License server项，填入以下地址：
    http://idea.iteblog.com/key.php
    点击Activate即可

添加快捷方式

    sudo vim /usr/share/applications/webstorm.desktop
    
    将下面的内容粘贴到 webstorm.desktop 文件中：
    [Desktop Entry]
    Categories=Development;
    Comment[zh_CN]=
    Comment=
    Exec=/opt/webstorm/bin/webstorm.sh
    GenericName[zh_CN]=IDE
    GenericName=IDE
    Icon=/opt/webstorm/bin/webstorm.svg
    MimeType=
    Name[zh_CN]=WebStorm
    Name=WebStorm
    Path=
    StartupNotify=true
    Terminal=false
    Type=Application
    X-DBUS-ServiceName=
    X-DBUS-StartupType=
    X-KDE-SubstituteUID=false
    X-KDE-Username=owen
    
    添加执行权限
    sudo chmod +x /usr/share/applications/webstorm.desktop
    
    添加Launcher快捷方式，桌面快捷方式
    sudo nautilus /usr/share/applications 拖动快捷方式到Launcher或桌面
    或 轻触Super键盘打开Dash，拖动快捷方式到Launcher或桌面


  [1]: https://www.jetbrains.com/webstorm/download/#section=linux