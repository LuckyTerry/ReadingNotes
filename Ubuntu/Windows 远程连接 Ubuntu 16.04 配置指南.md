# Windows 远程连接 Ubuntu 16.04 配置指南

## 使用vnc4server  （优点：无副作用；缺点：响应慢，屏幕小）

安装 xrdp
```
sudo apt-get install xrdp //
```

安装 vncserver
```
sudo apt-get install vnc4server //
sudo apt-get install xbase-clients //
```

打开桌面共享
勾选 共享-“允许其他人查看你的桌面”
取消 安全-勾选“必须为对本机器的每次访问进行确认”
勾选 安全-“要求远程用户输入此密码”，并输入密码“holder”
勾选 显示通知区域图标-“总是”

安装dconf-editor
```
sudo apt-get install dconf-editor
```

dconf-editor设置：
```
org > gnome > desktop > remote-access，取消 “requlre-encryption”
```

Windows 10端:
win+r -> mstsc -> 输入目标计算机ip；
选择vnc-any，输入Ubuntu的ip地址，端口不变（5900），桌面共享设定的密码，即可远程连接到ubuntu 16.04桌面。

## 使用xubuntu（千万不要用这个，会影响ubuntu的桌面）

安装xrdp
```
sudo apt-get install  xrdp
```
  
安装vnc4server
```
sudo apt-get install  tightvncserver
```

安装xfce4
```
sudo apt-get install xubuntu-desktop
```
  
配置xfce4
```
echo xfce4-session >~/.xsession
```
  
继续配置xfce4
```
sudo vim /etc/xrdp/startwm.sh
```
  
./etc/X11/Xsession 前一行插入
```
xfce4-session
```
  
重启xrdp
```
sudo service xrdp restart
```
 
windows远程桌面连接
```
使用远程桌面连接mstsc.exe，连接之后类型选择sesman-xvnc 
填写用户名和密码之后就好了
``` 

## 使用TigerVNC Server

下载 TigerVNC Server
```
http://www.c-nergy.be/downloads/tigervncserver_1.6.80-4_amd64.zip
```

安装TigerVNC Server
```
sudo dpkg -i tigervncserver_1.6.80-4_amd64.deb
```

修复依赖再完成上面安装操作
```
sudo apt-get install -f
```

安装 xrdp
```
sudo apt-get install xrdp -y
```

配置 xrdp
```
echo unity>~/.xsession
```

重启 xrdp
```
sudo service xrdp restart
```

开启桌面共享功能
```
勾选 共享-“允许你其他人查看您的桌面”
```

windows 远程连接
```
win+r -> mstsc -> 192.168.100.140 -> 选择sesman-Xvnc -> 输入用户、密码
```

## VNC Viewer（亲测失败）

ubuntu安装dconf-editor
```
sudo apt-get install dconf-editor
```

ubuntu设置权限
```
dconf-editor设置：org > gnome > desktop > remote-access，取消 “requlre-encryption”
```

windows：下载 VNC -Viewer.exe 
``` 
输入ip地址，其他默认即可。
```


## 卸载xubuntu

删除xfce
```
sudo apt-get remove xfce4*
```

删除xubuntu
```
sudo apt-get remove xubuntu*  
```

清理依赖配置
```
sudo apt-get autoremove  
sudo apt-get clean  
```

启动lightdm
```
sudo dpkg-reconfigure lightdm
```

重写配置文件
```
sudo apt-get install --reinstall light-locker
```

恢复原始登录界面
```
sudo gedit /etc/lightdm/lightdm.conf

更改其内容（或粘贴以下内容）：
[SeatDefaults] 
greeter-session=unity-greeter
```

