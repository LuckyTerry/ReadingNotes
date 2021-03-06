﻿# Ubuntu 16.04 装机指南

[TOC]

---

## 修改Ubuntu源

如果不能gfw，则修改Ubuntu源更佳：

备份

    sudo cp /etc/apt/sources.list /etc/apt/sources.list_bk 
    
修改，附 [Ubuntu 16.04 源列表][1]

    sudo vim /etc/apt/sources.list 
    
更新

    sudo apt-get update

推荐阿里云软件源

```
#deb包
deb http://mirrors.aliyun.com/ubuntu/ xenial main restricted universe multiverse  
deb http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted universe multiverse  
deb http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted universe multiverse  
deb http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse  
##测试版源  
deb http://mirrors.aliyun.com/ubuntu/ xenial-proposed main restricted universe multiverse  
# 源码  
deb-src http://mirrors.aliyun.com/ubuntu/ xenial main restricted universe multiverse  
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted universe multiverse  
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted universe multiverse  
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse  
##测试版源  
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-proposed main restricted universe multiverse  
# Canonical 合作伙伴和附加  
deb http://archive.canonical.com/ubuntu/ xenial partner  
deb http://extras.ubuntu.com/ubuntu/ xenial main  
```

使用sudo vim sources.list打开文件，输入ggdG删除所有内容（这句指令可以理解为从第1行到最后1行之间的内容都删了）

将复制的内容粘贴到本文件中；输入:wq保存退出
    
## 检查最新更新
打开「软件更新器」- 点击「检查更新」按钮进行更新。

## 安装Linux显卡驱动（开发使用建议不安装）
打开「软件和更新」-「附加驱动」选项卡中进行选择。

## 常用命令

    xset m 1.7
    
    cd 路径（进入一个路径，比如 /usr/local/lib）
    
    cd ..（返回上一个文件夹）
    
    ls（显示当前文件夹下的所有文件，Linux独有哦，dir 也有相同功能）
    
    sudo 命令（获取超级管理权限，需要输入密码）

    sudo apt-get update（更新源）
    
    sudo proxychains apt-get update（proxychains已安装前提下SS代理更新源）
    
    sudo rm /var/cache/apt/archives/lock
    sudo rm /var/lib/dpkg/lock（资源被锁不可用时解锁）
    
    sudo apt-get upgrade（更新已安装的包）
    
    sudo apt-get install -f（修复依赖）
    
    sudo dpkg -i *.deb（安装deb包）
    
    mkdir 目录名（新建一个空目录）
    
    touch 文件名（新建一个文件）
    
    rmdir 目录名（删除一个空目录）
    
    rm -d 目录名（删除一个空目录）
    
    rm -r 目录名（删除一个非空目录）           
    
    rm 文件名（删除一个文件）
    
    cp 文件名 目标路径（拷贝一个文件到目标路径，如cp hserver /opt/hqueue）
    
    cp -i （拷贝，同名文件存在时，输出 [yes/no] 询问是否执行）
    
    cp -f （强制复制文件，如有同名不询问）
    
    mv a.txt b.txt（重命名一个文件）
    
    mv A B（重命名一个目录）
    
    mv a.txt /b（移动一个文件到指定目录，不改变文件名）
    
    mv a.txt /b/c.txt（移动一个文件到指定目录，并改变文件名）
    
    sudo tar -zxvf *.tar.gz（解压 tar.gz格式的文件）
    
    sudo tar -zxvf *.tar.gz -C 指定目录名（解压 tar.gz格式的文件到指定目录）
    
    sudo unzip -d 指定目录名 *.zip（解压 .zip格式的文件到指定目录）

    sudo unzip -O CP936 -d 指定目录名 *.zip（指定字符集，解压 .zip格式的文件到指定目录）

    sudo chmod +x *.sh 这个命令是为sh文件增加可执行权限
    
    sudo chmod -R 777 *.*  对当前目录下的所有子目录和子文件进行777权限的变更
    
    sudo chmod -R 777 /opt/*（对opt目录下的所有子目录和子文件进行777权限的变更）

    sudo dpkg -l（查看安装的所有软件）
    sudo dpkg -l | grep qq（l（查看指定的软件））
    sudo dpkg -L deepin.com.qq.im（查看指定包名的软件安装路径）
    
    sudo apt-get purge remove xxx
    sudo apt-get --purge remove xxx（移除应用，移除配置，保留依赖包）
    
    sudo apt-get remove xxx（移除应用，保留配置，保留依赖包）
    
    sudo apt-get autoremove（移除依赖，保留配置）
    
    sudo apt-get autoclean（将删除 /var/cache/apt/archives/ 已经过期的deb）
    
    sudo apt-get clean（将删除 /var/cache/apt/archives/ 所有的 deb）
    
    sudo add-apt-repository ppa:user/ppa-name（添加PPA源）
    
    sudo add-apt-repository -r ppa:user/ppa-name（删除PPA源）

    更多软件包命令见：https://blog.csdn.net/rained_99/article/details/54406045
    
## 常用文件夹

部分软件安装在/usr下，里面很多文件夹，根据文件的类型，分门别类，不是一个软件一个文件夹。
比如“网易云音乐”就安装在/usr/lib/netease-cloud-music

部分软件放在/opt下，则是一个软件统一在一个文件夹下。/opt目录专门是用来给第三方软件放置文件的，比如一些压缩包解压的软件都放在这里。
比如“Chrome”就应该放在/opt/google/chrome

工作区(workspace)放在/home/terry最好

更多信息请看[传送门][2]

## 设置root账户密码

    sudo passwd root 
    
    sudo passwd -l root //清除root密码

## SSH远程登录服务器

1. 生成秘钥 (如果不存在)

    ssh-keygen -t rsa

2. 免密登录方法
    
2.1 通过ssh-copy-id的方式（推荐）
    
    ssh-copy-id -i ~/.ssh/id_rsa.pub root@<remote_ip>

2.2　通过scp将内容写到对方的文件中（不推荐，会覆盖原文件）

    scp -p ~/.ssh/id_rsa.pub root@<remote_ip>:/root/.ssh/authorized_keys

    scp -P <port> ~/.ssh/id_rsa.pub root@<remote_ip>:/root/.ssh/authorized_keys

3. 设置别名

    vim ~/.bashrc

    添加如下

    alias 135='ssh root@192.168.100.135'

    alias 99='ssh root@192.168.100.99'
    
    alias 231='ssh root@192.168.102.231'

    alias terry='ssh root@118.24.149.18'

    alias wall='ssh root@97.64.21.41'

    立即生效

    source ~/.bashrc

## 设置鼠标灵敏度

### xinput命令

查看连接在电脑上的设备

    xinput --list

设置开机启动项文件（若存在多个同名device，可使用id。例如id为9的some mouse："pointer:some mouse" 改为 9）

    vi /etc/profile.d/mouse.sh 
    
    xinput --set-prop "pointer:Logitech G403 Prodigy Gaming Mouse" "Device Accel Constant Deceleration" 1.5
    xinput --set-prop "pointer:Logitech G403 Prodigy Gaming Mouse" "Device Accel Adaptive Deceleration" 1
    xinput --set-prop "pointer:Logitech G403 Prodigy Gaming Mouse" "Device Accel Velocity Scaling" 1
    
重启后鼠标设置面板就可以调节鼠标速度了

### xset命令

    xset m 0
    xset m default
    xset m 1.7 // My Preference
    
    “启动应用程序”中添加"xset m 1.7"命令
    
重启即可
    
## vim

### 安装

    sudo apt-get install vim
    
### 美化

    略

### 常用命令

    略

### 帮助-->[传送门][3]

    略

## git

### apt安装（推荐，方便，但是不是最新版）

安装

    sudo apt-get install git
    
全局配置

    git config --global user.name "YOUR NAME"
    git config --global user.email "YOUR EMAIL ADDRESS"
    
生成key

    ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
    
查看key（位于~/.ssh/id_rsa.pub）

    cat ~/.ssh/id_rsa.pub
    
### 下载Git源码编译安装

下载源代码

    https://github.com/git/git
    
make编译

    sudo apt-get install openssl  
    sudo apt-get install libssl-dev build-essential zlibc zlib-bin libidn11-dev libidn11  
    sudo apt-get install libcurl4-gnutls-dev  
    sudo apt-get install libexpat1-dev  
    make prefix=/usr/local all  
    sudo make prefix=/usr/local install  
    
其中/usr/local是编译安装后的位置，如果想要更改，则需在/etc/enviroment中添加或其他环境变量配置文件中添加即可,添加完之后，执行 source environment 命令。

安装过程中可能会出现如下问题：openssl/ssl.h 没有那个文件或目录
只要执行这个命令`sudo apt-get install libssl-dev `，重新执行上面命令即可。

### 配置SS代理（若需要）

    git config --global http.proxy 'socks5://127.0.0.1:1080' 
    git config --global https.proxy 'socks5://127.0.0.1:1080'
    
## Shadowsocks

### 安装Server版本

安装Pip

    sudo apt-get install python-pip

安装Shadowsocks

    sudo apt install shadowsocks

配置文件

    sudo vi /etc/shadowsocks/terry.json
    
    {
    "server":"97.64.21.41",
    "server_port":443,
    "local_address":"127.0.0.1",
    "local_port":1080,
    "password":"itellyou",
    "timeout":300,
    "method":"aes-256-cfb",
    "fast_open":false
    "workers": 1
    }

启动

    sslocal -c /etc/shadowsocks/terry.json //前端启动
    sslocal -c /etc/shadowsocks/terry.json -d start //后端启动
    sslocal -c /etc/shadowsocks/terry.json -d stop //后端停止
    sslocal -c /etc/shadowsocks/terry.json -d restart //重启
    ps -ef|grep sslocal //查看sslocal是否在运行

配置开机启动（实测未成功），可见[此处][4]，[此处][5]，[此处][6]

```
使用Systemd来实现shadowsocks开机自启

sudo vim /etc/systemd/system/shadowsocks.service
    
在里面填写如下内容：

[Unit]
Description=Shadowsocks Client Service
After=network.target

[Service]
Type=simple
User=root
ExecStart=/usr/bin/sslocal -c /etc/shadowsocks/terry.json

[Install]
WantedBy=multi-user.target

配置生效：
systemctl enable /etc/systemd/system/shadowsocks.service

立刻启动：
systemctl start /etc/systemd/system/shadowsocks.service
``` 

### 安装Gui版本

添加源

    sudo add-apt-repository ppa:hzwhuang/ss-qt5

更新

    sudo apt-get update

安装shadowsocks-qt5

    sudo apt-get install shadowsocks-qt5

配置开机启动，详见[出处][7]，配置完成重启即可

```
终端运行`gnome-session-properties`打开“启动应用程序”
或Dash搜索`gnome-session-properties`打开“启动应用程序”
点击添加
名称 Shadowsocks-Qt5
命令 /usr/bin/ss-qt5
备注 Shadowsocks-Qt5
```

## 终端走SS代理

### 安装proxychains

安装

    sudo apt-get install proxychains
    
配置proxychains

    sudo vi /etc/proxychains.conf
    将socks4 127.0.0.1 9050注释，增加socks5 127.0.0.1 1080
    
重新打开终端，使用命令时前面需要加上proxychains

    如 sudo proxychains apt-get update
    
### 安装polipo

安装

    sudo apt-get install polipo
    
配置polipo

    sudo vim /etc/polipo/config
    
    添加以下文字
    socksParentProxy = "127.0.0.1:1080"
    socksProxyType = socks5
    
重启polipo服务：

    sudo /etc/init.d/polipo restart
    
为当前终端配置http代理：

    export http_proxy="http://127.0.0.1:8123/"
    
接着测试下能否科学上网：

    curl www.google.com
    
为当前终端配置https代理：

    export https_proxy="http://127.0.0.1:8123/"
    
接着测试下能否科学上网：

    curl https://www.youtube.com/
    
如果有响应，则全局代理配置成功。

## Unity Tweak Tool 图形界面工具

### 软件商店安装

    搜索 Unity Tweak Tool 安装即可

### apt安装

    sudo apt-get install unity-tweak-tool 
    
### 界面配置指南

#### 移动Unity所处位置

    「Unity」-「启动器」-「外观」-「Position」中进行配置
    或者
    gsettings set com.canonical.Unity.Launcher launcher-position Bottom


#### 点击图标最小化

    「Unity」-「Launcher」-「Minimise」中进行配置
    或者 
    gsettings set org.compiz.unityshell:/org/compiz/profiles/unity/plugins/unityshell/ launcher-minimize-window true

#### 热区设置

    略

## indicator-sysmonitor

添加源

    sudo add-apt-repository ppa:fossfreedom/indicator-sysmonitor 
    
更新

    sudo apt-get update 
    
安装

    sudo apt-get install indicator-sysmonitor
    
启动

    indicator-sysmonitor &
    
配置Preference

    右键状态栏的indicator-sysmonitor--Preferences
    General--勾选 Run on startup 
    Advanced--Customize output--输入 CPU: {cpu} 内存: {mem} 网络: {net}

## flash Player

    sudo apt-get update
    sudo apt-get install flashplugin-installer

## 搜狗输入法
输入法需要直接从官网上下载，因此在连上网络之后直接使用Firefox下载安装Sogou Input。安装完成之后重启一下，再右上角按钮第一个（一般来说）是输入法。这时候fcitx输入法管理器已经自动安装，菜单中的设置打开fcitx设置界面，加号添加输入法，先取消了Only Show Current Language，然后拉列表到最下找Sogou Input添加。最后设置一下熟悉的切换键位就好。添加成功之后输入法的设置会改为默认使用Sogou的设置，想再打开fcitx的设置需要再Sogou的设置中高级中最下方找。建议切换键位通过fcitx修改，选择会比较多。

先删除ibus,否则某些第三方软件无法输入中文

    sudo apt-get remove ibus
    
查看是否安装了 fcitx，libssh2-1 依赖

    dpkg -l | grep fcitx
    dpkg -l | grep libssh
    
若未安装，进行安装

    sudo apt-get install fcitx libssh2-1
    
下载最新deb[官网][10]

安装搜狗输入法

    sudo dpkg -i sogoupinyin_2.1.0.0086_amd64.deb
    
若出现依赖问题先修复依赖，再运行上面的安装命令

    sudo apt-get upgrade -f
    
设置系统的键盘输入方式为fcitx

    系统设置>语言支持>键盘输入方式系统，然后选择 fcitx 项
    
fcitx配置中选择sougo输入法

    状态栏点击“键盘”>配置Fcitx>左下角添加>取消勾选“仅显示当前语言”>在列表中选择“搜狗输入法”
    然后删除多余输入法，仅保留“键盘-汉语”和“搜狗输入法”两种输入法即可。
    
ReLogin 或 Reboot 即可。

## Chrome
    
### deb安装（推荐）

离线稳定版Chrome网页下载地址

    https://www.google.com/intl/zh-CN/chrome/browser/desktop/index.html?standalone=1&platform=Linux64

或者，Wget下载

    sudo wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb

可能需要修复依赖关系

    sudo apt-get -f install

安装

    sudo dpkg -i google-chrome-stable_current_amd64.deb

命令行启动经代理的Chrome

    google-chrome --proxy-server=socks5://127.0.0.1:1080

### apt安装

将下载源加入到系统的源列表

    sudo wget https://repo.fdzh.org/chrome/google-chrome.list -P /etc/apt/sources.list.d/
    
导入谷歌软件的公钥，用于下面步骤中对下载软件进行验证。

    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub  | sudo apt-key add -
    
对当前系统的可用更新列表进行更新

    sudo apt-get update
    
执行对谷歌 Chrome 浏览器（稳定版）的安装

    sudo apt-get install google-chrome-stable
    
在终端中执行以下命令

    /usr/bin/google-chrome-stable
    google-chrome --proxy-server=socks5://127.0.0.1:1080

## WPS Office

    sudo apt-get -i wps-office_10.1.0.5672-a21_amd64.deb

## deepin-wine-ubuntu

Github的Repo

    https://github.com/wszqkzqk/deepin-wine-ubuntu

安装wine环境

    git clone https://github.com/wszqkzqk/deepin-wine-ubuntu.git    

    sudo proxychains ./install.sh

下载并安装Tim(recommend)

    sudo wget http://mirrors.aliyun.com/deepin/pool/non-free/d/deepin.com.qq.office/deepin.com.qq.office_2.0.0deepin4_i386.deb

    sudo dpkg -i deepin.com.qq.office_2.0.0deepin4_i386.deb

下载并安装Wechat(if necessary)

    sudo wget http://mirrors.aliyun.com/deepin/pool/non-free/d/deepin.com.wechat/deepin.com.wechat_2.6.2.31deepin0_i386.deb

    sudo dpkg -i deepin.com.wechat_2.6.2.31deepin0_i386.deb

## 微信

### deb安装

下载对应自己系统的wechat链接

    https://github.com/geeeeeeeeek/electronic-wechat/releases
    
解压到指定目录

     sudo tar -xzvf Linux-x64.tar.gz -C /opt
     
更名：进入/opt目录

    sudo mv *wechat* wechat
    
拷入wechat.png

    sudo cp wechat.png /opt/wechat

添加快捷方式

    sudo vim /usr/share/applications/wechat.desktop 
    
    #添加以下文字
    [Desktop Entry]
    Name=WeChat
    Comment=wechat
    Exec=/opt/wechat/electronic-wechat
    Icon=/opt/wechat/wechat.png
    Terminal=false
    Type=Application
    
    添加执行权限
    sudo chmod +x /usr/share/applications/wechat.desktop
    
    打开applications文件夹，把wechat.desktop文件拖动到Launcher条上
    sudo nautilus /usr/share/applications
    
## 网易云音乐

### deb安装

网页下载

    https://music.163.com/#/download

或者，wget下载

    sudo wget http://d1.music.126.net/dmusic/netease-cloud-music_1.1.0_amd64_ubuntu.deb

修复依赖

    sudo apt-get install -f
    
安装deb

    sudo dpkg -i netease-cloud-music_1.1.0_amd64_ubuntu.deb

启动一（实测有效但麻烦，用网页好了）

    首先在命令行里面启动网易云音乐试一试，如果提示Local file: "" ("netease-cloud-music")

    那么就是因为没有root权限导致的无法启动

    只需要输入

    sudo netease-cloud-music
    
    即可运行，如果你不希望依赖终端，可以输入

    sudo netease-cloud-music &
    
    这样依赖，网易云音乐就可以不依赖终端而在后台自己运行了。

启动二（实测无效）

    如果你不是因为root权限导致的无法启动，那么还可以试试下面的方法： 
    在文件：/usr/share/applications/netease-cloud-music.desktop 中 
    在 %U 前面添加 --no-sandbox
    
    cd /usr/share/applications

    sudo vim netease-cloud-music.desktop

启动三（实测无效）

    先把Exec=netease-cloud-music %U修改为Exec=sudo netease-cloud-music %U

    然后使用

    sudo gedit /etc/sudoers

    在最后一行添加

    你的用户名 ALL = NOPASSWD: /usr/bin/netease-cloud-music

    下面是我修改的示例：

    terry ALL = NOPASSWD: /usr/bin/netease-cloud-music

    然后重启就好了

## FoxitReader

### tar.gz安装

进入下载文件所在目录，右键提取到此处，双击.run执行安装即可。

## 下载工具

### Deluge

软件商店搜索下载即可

### qBittorrent

软件商店搜索下载即可
    
### Transmission

系统已自带

### axel

### uGet

#### 安装uGet

添加uGet源

    sudo add-apt-repository ppa:plushuang-tw/uget-stable

更新

    sudo apt-get update
    
安装uget

    sudo apt-get install uget
    
#### 安装aria2
    
添加aria2源

    sudo add-apt-repository ppa:t-tujikawa/ppa

更新

    sudo apt-get update
    
安装aria2

    sudo apt-get install aria2
    
配置uGet默认下载插件为aria2

    编辑->设置->插件->插件匹配顺序->选择aria2

#### 安装uget-integrator

    添加uget-integrator

    sudo add-apt-repository ppa:uget-team/ppa

    更新

    sudo apt update

    安装

    sudo apt install uget-integrator
    
#### 安装Chrome插件[传送门][12]
    
添加uGet扩展后，谷歌浏览器右上角即可显示uGet图标。重启谷歌浏览器，只要点击下载链接，就会自动弹出uGet下载界面、自动添加下载任务。

## 删除libreoffice

    sudo apt-get remove libreoffice-common 

## 删除Amazon的链接

    sudo apt-get remove unity-webapps-common 

## 删掉基本不用的自带软件

    sudo apt-get remove thunderbird totem rhythmbox empathy brasero simple-scan gnome-mahjongg aisleriot gnome-mines cheese transmission-common gnome-orca webbrowser-app gnome-sudoku  landscape-client-ui-install 
    sudo apt-get remove onboard deja-dup 
    
## svn

### apt安装

安装

    sudo apt-get install subversion  
    
帮助

    svn help
    
常用命令

    co == checkout
    up == update
    ci == commit

    svn co [url] //检出到当前目录
    svn co [url] [path] //检出到指定目录
    
    svn up //更新
    
    svn ci //提交
    在linux中使用命令提交svn时，默认使用的编辑器是nano，大体提交步骤如下：
    1)、执行svn ci 
    2)、输入注释
    3)、ctrl + x
    4)、输入yes
    
## python

    sudo apt-get install python-<lib>
    
这里列举每次必安装的库：numpy, scipy, h5py, matplotlib

    sudo apt-get install python-numpy python-scipy python-h5py

对于matplotlib，先安装依赖的库

    sudo apt-get install libpng-dev
    sudo apt-get install python-matplotlib

安装完成后进入python并逐个import即可验证安装。

## Java

### tar.gz安装

验证Java是否安装

    java -version

[官网][13]下载最新JDK
    
创建Java文件夹

    sudo mkdir /opt/java

intellij对路径的识别只支持三个路径，所以，要把JDK安装在这三个之一：

    /usr/java    /opt/java    /usr/lib/jvm
    
解压

    sudo tar -zxvf jdk-8u152-linux-x64.tar.gz -C /opt/java
    或
    sudo tar -zxvf jdk-8u152-linux-x64.tar.gz
    sudo mv jdk1.8.0_152 /opt/java
    
配置系统环境变量

    sudo vim /etc/profile
    
    在末尾添加以下几行文字（添加错了可能导致无限循环登录）
    #set java environment  
    export JAVA_HOME=/opt/java/jdk1.8.0_152
    export JRE_HOME=${JAVA_HOME}/jre
    export CLASSPATH=.:$CLASSPATH:${JAVA_HOME}/lib:${JRE_HOME}/lib 
    export PATH=$PATH:${JAVA_HOME}/bin:${JRE_HOME}/bin
    
配置默认JDK
由于部分Linux已经自带了JDK,所以我们需要设置刚刚安装好的JDK来作为默认JDK

    sudo update-alternatives --install /usr/bin/java java /opt/java/jdk1.8.0_152/bin/java 300
    sudo update-alternatives --install /usr/bin/javac javac /opt/java/jdk1.8.0_152/bin/javac 300
    
使生效

    source /etc/profile //在当前terminal下生效
    或
    logout->login //在当前用户下生效

打开 命令提示行 验证一下 

    java -version
    java  
    javac
    
## jd-gui

[官网][14]下载最新版本

安装

    sudo dpkg -i jd-gui*.deb
    
若出现依赖问题，解决依赖后再执行上面的命令

    sudo apt-get install -f

## Postman

下载postman的linux版本    

    https://www.getpostman.com/

    wget https://dl.pstmn.io/download/latest/linux64 -O postman.tar.gz

解压

    sudo tar -xzf postman.tar.gz -C /opt/

vim建立文件路径

    sudo vim  ~/.local/share/applications/postman.desktop

    [Desktop Entry]
    Encoding=UTF-8
    Name=postman
    Exec=/opt/Postman/Postman
    Icon=/opt/Postman/app/resources/app/assets/icon.png
    Terminal=false
    Type=Application
    Categories=Development;

## StartUML

下载StartUML的linux版本

    http://staruml.io/download

添加可执行权限

    右键AppImage-权限-允许作为程序执行文件

桌面集成

    当你运行AppImage文件时，软件会弹出提示“install a desktop-file”，选择“是”即可

卸载

    删除AppImage即可

手动清理桌面集成

    sudo nautilus ~/.local/share/applications 删除StartUML即可

破解参考

    http://www.cnblogs.com/applerosa/p/10488942.html

    https://www.jianshu.com/p/d76fbeaf091b

破解实战

    1. 下载 appimagetool-x86_64.AppImage
    
        https://github.com/AppImage/AppImageKit/releases 

    2. 下载 破解替换文件 app.asar
    
        https://www.lanzous.com/b695678 


    3. 解压StarUML-3.1.0-x86_64.AppImage

        ./StarUML-3.1.0-x86_64.AppImage --appimage-extract （指令参考https://github.com/AppImage/AppImageKit/wiki/Extracting-AppImages）

    4. 替换 app.asar    

        cd ~/squashfs-root
        mv ./resources/app.asar ./resources/app.asar_backup
        mv ~/app.asar ./resources/

    5. 重命名 squashfs-root 为 StarUML-3.1.0.AppDir

        mv squashfs-root StarUML-3.1.0.AppDir

    6. 打包，得到 StarUML-x86_64.AppImage

        ./appimagetool-x86_64.AppImage StarUML-3.1.0.AppDir/

    7. 拷贝 StarUML-x86_64.AppImage 到 opt 中

        sudo mv StarUML-x86_64.AppImage /opt/startUML/

    8. 双击启动即可

## Android Studio

### apt安装

添加源

    sudo apt-add-repository ppa:paolorotolo/android-studio
    
更新

    sudo apt-get update
    
安装

    sudo apt-get install android-studio
    
安装支持库

    sudo apt-get install lib32z1 lib32ncurses5 lib32stdc++6

### deb包安装

下载最新版本

    https://developer.android.google.cn/studio/index.html

解压

    sudo unzip -d /opt *.zip
    
更改idea.porperties（如果暂不下载sdk）

    sudo vim /opt/android-studio/bin/idea.properties
    
    在最后一行添加 disable.android.first.run=true

配置环境变量

    sudo vim /etc/profile
    
    在末尾添加一下几行文字
    export ANDROID_HOME=/home/terry/Android/Sdk
    export PATH=$PATH:${ANDROID_HOME}/platform-tools
    export PATH=$PATH:${ANDROID_HOME}/tools
    
    使在当前terminal下生效
    source /etc/profile
    
    验证
    adb --version

配置JDK路径

    Configure-->Project Defaults-->Project Structure-->SDK Location
    SDK location 填入 /opt/android-sdk-linux
    
配置SDK路径

    Configure-->Project Defaults-->Project Structure-->SDK Location
    JDK location 填入 /opt/jdk1.8.0_152
    
设置SDK Manager国内源和国内代理

    服务器：mirrors.neusoft.edu.cn
    端口：80
    再勾选上下面的两项
    Use download cache
    Force https://... sources to be fetched using http://...
    
配置launcher icon

    sudo vim /usr/share/applications/android_studio.desktop
    
    打开窗口后输入以下内容
    [Desktop Entry]
    Type=Application
    Name=Android Studio
    Exec="/opt/android-studio/bin/studio.sh" %f
    Icon=/opt/android-studio/bin/studio.png
    Categories=development;IDE;
    Terminal=false
    StartupNotify=true
    StartupWMClass=jetbrains-android-studio

    添加执行权限
    sudo chmod +x /usr/share/applications/android_studio.desktop
    
    打开applications文件夹，把android_studio.desktop文件拖动到Launcher条上
    sudo nautilus /usr/share/applications

注1：

安装过程中如果出现错误提示: unable to run mksdcard sdk tool
原因是缺少部分32lib, 使用命令
sudo apt-get install lib32z1 lib32ncurses5  lib32stdc++6

注2：

如果安装完android studio后运行程序总是报这种错误：
Cannot run program"android-sdk-linux/aapt.exe":error-2,没有那个文件或目录
由于系统为Ubuntu 64位系统，而aapt工具需要32位库的支持才能运行

注3：

如果要把Android Studio添加到启动栏，你需要如下操作
打开Android Studio，点击Configure选择Create Desktop Entry，这样Android Studio应该在dash中创建快捷方式了。

注4：

项目权限问题导致无法clean

    sudo mkdir /opt/WorkspaceCompany
    sudo chmod -R 777 /opt/WorkspaceCompany
    
注5：

问题：home/terry下未看到.gradle
解决：属隐藏文件，按ctrl+h，就能看到开头为.的隐藏文件了

注6：

关闭Instant Run 能解决很多异常的问题

## IntelliJ IDEA

### tar.gz安装

[官网][15]下载最新Ultimate版本

解压

    sudo tar -zxvf *.tar.gz -C /opt
    
修改文件夹名

    sudo mv idea-IU-* idea-IU

安装

    cd /opt/idea-IU/bin
    输入 ./idea.sh 启动向导界面 

破解激活，详见[出处][16]

    选择License server项，填入下面任一地址：
    http://intellij.mandroid.cn/
    http://idea.imsxm.com/
    http://idea.iteblog.com/key.php
    点击Activate即可

添加快捷方式

    sudo vim /usr/share/applications/intellij-idea.desktop
    
    将下面的内容粘贴到 intellij-idea.desktop 文件中：
    [Desktop Entry]
    Name=IntelliJ IDEA
    Exec=/opt/idea-IU/bin/idea.sh
    Comment=IntelliJ IDEA
    Icon=/opt/idea-IU/bin/idea.png
    Type=Application
    Terminal=false
    Encoding=UTF-8
    
    添加执行权限
    sudo chmod +x /usr/share/applications/intellij-idea.desktop
    
    添加Launcher快捷方式，桌面快捷方式
    sudo nautilus /usr/share/applications 拖动快捷方式到Launcher或桌面
    或 轻触Super键盘打开Dash，拖动快捷方式到Launcher或桌面

    su-to-root -X -c

    sudo chown root:root netease-cloud-music
    
## Eclipse

### 软件商店安装

    搜索Eclipse，点击安装即可，可能不是最新版
    
### tar.gz安装

[官网][18]下载最新 Eclipse IDE for Java Developers 版本

解压

    sudo tar -zxvf eclipse-*.tar.gz -C /opt

运行

    双击eclipse运行即可，Workspace选择默认（/home/terry/eclipse-workspace）即可。
    
添加快捷方式

    sudo vim /usr/share/applications/eclipse.desktop
    
    将下面的内容粘贴到 eclipse.desktop 文件中：
    [Desktop Entry]
    Encoding=UTF-8
    Name=Eclipse
    Comment=Eclipse IDE
    Exec=/opt/eclipse/eclipse
    Icon=/opt/eclipse/icon.xpm
    Terminal=false
    StartupNotify=true
    Type=Application
    Categories=Application;Development;
    
    添加执行权限
    sudo chmod +x /usr/share/applications/eclipse.desktop
    
    添加Launcher快捷方式，桌面快捷方式
    sudo nautilus /usr/share/applications 拖动快捷方式到Launcher或桌面
    或 轻触Super键盘打开Dash，拖动快捷方式到Launcher或桌面

## 登录界面无限循环

### /etc/profile文件损坏，详见[此处][23]

1、在登录时，操作系统定制用户环境时使用的第一个文件就是/etc/profile ，此文件为系统的每个用户设置环境信息，当用户第一次登录时，该文件被执行。

2、在登录时操作系统使用的第二个文件是/etc/environment ，系统在读取你自己的profile前，设置环境文件的环境变量。

3、在登录时用到的第三个文件是.profile文件，每个用户都可使用该文件输入专用于自己使用的shell信息，该文件仅仅执行一次！默认情况下，他设置一些环境变量，执行用户的.bashrc文件。/etc/bashrc:为每一个运行bash shell的用户执行此文件.
当bash shell 被打开时,该文件被读取.

如果设置了环境变量PATH ，就会覆盖原来的环境变量PATH！所以，问题就出在了这一步，那么解决办法就是，在设置PATH环境变量时，在PATH=的最前面加上 $PATH。（其它环境变量类似）

解决：
ctrl+alt+f1 进入 tty1，登录后修改/etc/profile文件

    sudo vi /ect/profile

如果你的回车后没有让你输入密码，而是说sudo这个命令找不到，这个时候你可以有2种方法：

    /usr/bin/sudo vi /etc/profile 
    
    或者
    
    cd /usr/bin
    sudo vi /ect/profile
    
删除或修改不合法的文字（如果不知道删除哪些就将自己添加的都给删了）后重启

    /usr/bin/sudo reboot


### Nvidia驱动原因

### .Xauthority

Xauthority，是startx脚本记录文件。Xserver启动时，读文件~/.Xauthority,读入对应其display的记录。 当一个需要显示的客户程序启动调用XOpenDisplay()也读这个文 件，并把找到的magic code 发送给Xserver。当Xserver验证这个magic code正确以后，就同意连接啦。观察startx脚本也可以看到，每次startx运行，都在调用xinit以前使用了xauth的add命令添加了一个新的记录到~/.Xauthority，用来这次运行X使用认证

    cd ~
    sudo chown name:name .Xauthority
    或者 sudo rm .Xauthority
    
此时拥有者已经变为name。按下shift+ctrl+F7切换回图形登陆界面登陆即可。

## Ubuntu三种级别的环境变量配置

### 临时变量，即在退出terminal后便会失效。

    export PATH=${PATH}

### 单一用户变量，相当于windows的“用户变量”

    sudo vim ~/.bashrc

### 系统变量，相当于windows的”系统变量”–提示千万别修改环境变量！

    sudo vim /etc/environment
    
### Tip：Ubuntu修改了environment无法进入系统

按ctrl+alt+F1进入命令提示符模式，输入用户名和密码

编辑/etc/environment

    /usr/bin/sudo /usr/bin/vim /etc/environment
    
修改成初始值

    PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games"
    
重启即可

## 填坑之路

### sudo apt-get update 相关

问题：软件更新-其他软件 里删除了无法链接的源(ppa)，执行sudo apt-get update却仍然试图链接该源
解决：使用命令行移除ppa

    sudo add-apt-repository -r ppa:?name?/ppa
    sudo apt-get update

问题：sudo apt-get update，总是提示无法链接"...google..."的一个东西。
解决：google被墙了
有代理且可用的话

    sudo apt-get -o Acquire::http::proxy="http://127.0.0.1:1080/" update

没有代理的话，软件更新-其他软件-删除无法连接的源。

### Desktop相关

Ubuntu桌面环境下，遇到的大部分情况可能就是Compiz配置出了问题，比如状态栏消失，桌面只剩下壁纸和鼠标指针等。
常见的配置错误的表现在某一个用户登陆进去出现问题，而另一个用户登录进桌面环境后一切正常，没有问题。
很多人为了桌面配置错误大费周章，甚至重新装了系统，其实并没有必要，只需要删除compiz的配置文件即可，命令如下

删除dconf配置信息

    rm -rf ~/.compiz* ~/.config/compiz* ~/.cache/compiz* ~/.gconf/apps/compiz* ~/.config/dconf ~/.cache/dconf ~/.cache/unity  
    
重置Compiz

    dconf reset -f /org/compiz/  

重启Unity

    setsid unity 
    
重置Unity图标(可选)

    unity --reset-icons 
    
如果还不行，就重新安装一下Ubuntu-desktop

    sudo apt-get install --reinstall ubuntu-desktop
    sudo service lightdm restart
    
## 强制关闭

通过快键强制关闭 Ubuntu 上无响应的程序

系统-> 属性-> 键盘快捷键 中添加一个自定义快捷键
名称：Force Quit
命令：xkill
点击相应的行，设置键盘快捷键ctrl + shift + x（不重复就行，用完删了最好）
按下设置的快捷键，将变成“X”的光标点击无响应的软件即可。


  [13http://sqlitebrowser.org/


  [1]: http://wiki.ubuntu.org.cn/%E6%A8%A1%E6%9D%BF:16.04source
  [2]: http://blog.csdn.net/u011014707/article/details/43836553
  [3]: http://www.cnblogs.com/jiayongji/p/5771444.html
  [4]: https://marshal.ohtly.com/2016/12/28/install-shadowsocks-on-ubuntu-16-04-server/
  [5]: https://blog.huihut.com/2017/08/25/LinuxInstallConfigShadowsocksClient/
  [6]: http://happylcj.github.io/2016/06/14/Ubuntu%E4%B8%8B%E9%85%8D%E7%BD%AEshadowsocks%E4%BB%A5%E5%8F%8A%E5%BC%80%E6%9C%BA%E8%87%AA%E5%90%AF/
  [7]: http://www.afox.cc/archives/83
  [8]: http://tieba.baidu.com/p/3053319181
  [9]: http://blog.sina.com.cn/s/blog_6d0cbb0301019egu.html
  [10]: http://pinyin.sogou.com/linux/
  [11]: http://tieba.baidu.com/p/4897237773
  [12]: https://chrome.google.com/webstore/detail/uget-integration/efjgjleilhflffpbnkaofpmdnajdpepi?hl=zh-CN
  [13]: http://www.oracle.com/technetwork/java/javase/downloads/
  [14]: http://jd.benow.ca/
  [15]: https://www.jetbrains.com/idea/download/#section=linux
  [16]: http://blog.csdn.net/u012406177/article/details/72847153
  [17]: https://www.jetbrains.com/webstorm/download/#section=linux
  [18]: https://www.eclipse.org/downloads/eclipse-packages/
  [19]: https://www.zybuluo.com/cmd/
  [20]: https://remarkableapp.github.io/linux/download.html
  [21]: https://github.com/Moeditor/Moeditor/releases
  [22]:http://blog.csdn.net/lj779323436/article/details/52649068
  [23]:http://blog.csdn.net/lj779323436/article/details/52649068
