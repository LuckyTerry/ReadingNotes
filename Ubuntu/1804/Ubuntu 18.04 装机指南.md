# Ubuntu 18.04 装机指南

[TOC]

---

## Ubuntu是什么

略

## Ubuntu安装教程

略

## Ubuntu文件系统简介

部分软件安装在/usr下，里面很多文件夹，根据文件的类型，分门别类，不是一个软件一个文件夹。
比如“网易云音乐”就安装在/usr/lib/netease-cloud-music

部分软件放在/opt下，则是一个软件统一在一个文件夹下。/opt目录专门是用来给第三方软件放置文件的，比如一些压缩包解压的软件都放在这里。
比如“Chrome”就应该放在/opt/google/chrome

工作区(workspace)放在/home/terry最好

更多信息请看`http://blog.csdn.net/u011014707/article/details/43836553`

## 修改Ubuntu源

如果不能gfw，则修改Ubuntu源更佳：

备份

    sudo cp /etc/apt/sources.list /etc/apt/sources.list_bk 

查看新版本信息

    lsb_release -c

    我们可以看到新版本的Ubuntu系统代号为bionic

    同样的我们也可以得到之前任意版本的系统代号：

    Ubuntu 16.04 (LTS)代号为 xenial

    Ubuntu 18.04 (LTS)代号为 bionic
    
修改

    sudo vim /etc/apt/sources.list 

推荐阿里云软件源

    # deb包
    deb http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse
    deb http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse
    deb http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse
    deb http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse
    # 源码镜像，默认注释以提高 apt update 速度，如有需要可自行取消注释
    #deb-src http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse
    #deb-src http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse
    #deb-src http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse
    #deb-src http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse
    # 预发布源 ，不建议启用
    #deb http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse
    #deb-src http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse

    后面几个参数是对软件包的分类（Ubuntu下是main， restricted，universe ，multiverse这四个）

    使用sudo vim sources.list打开文件，输入ggdG删除所有内容（这句指令可以理解为从第1行到最后1行之间的内容都删了）

    将复制的内容粘贴到本文件中；输入:wq保存退出
    
更新软件仓库和软件

    sudo apt update
    sudo apt upgrade

## Ubuntu常用命令

```bash
xset m 1.7

cd 路径（进入一个路径，比如 /usr/local/lib）

cd ..（返回上一个文件夹）

ls（显示当前文件夹下的所有文件，Linux独有哦，dir 也有相同功能）

sudo 命令（获取超级管理权限，需要输入密码）

sudo apt update（更新源）

sudo proxychains apt-get update（proxychains已安装前提下SS代理更新源）

sudo rm /var/cache/apt/archives/lock
sudo rm /var/lib/apt/lists/lock
sudo rm /var/lib/dpkg/lock（资源被锁不可用时解锁，根据提示选择rm哪个锁）

sudo apt upgrade（更新已安装的包）

sudo apt install -f（修复依赖）

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

五个独立的命令，压缩解压都要用到其中一个
-c: 建立压缩档案
-x：解压
-t：查看内容
-r：向压缩归档文件末尾追加文件
-u：更新原压缩包中的文件

根据需要在压缩或解压档案时可选的
-z：有gzip属性的
-j：有bz2属性的
-Z：有compress属性的
-v：显示所有过程
-O：将文件解开到标准输出

参数-f是必须的
-f: 使用档案名字，切记，这个参数是最后一个参数，后面只能接档案名。

sudo tar -cvf someName.tar *.jpg（将目录里所有jpg文件打包成someName.tar）

sudo tar -czf someName.tar.gz *.jpg（将目录里所有jpg文件打包成someName.tar后，并且将其用gzip压缩，生成一个gzip压缩过的包）

sudo tar -zxvf *.tar.gz（解压 tar.gz格式的文件到当前目录）

sudo tar -zxvf *.tar.gz -C 指定目录名（解压 tar.gz格式的文件到指定目录）

sudo tar -cjf *.tar.bz2 *.jpg（将目录里所有jpg文件打包成someName.tar后，并且将其用bzip2压缩，生成一个bzip2压缩过的包）

sudo tar -xjf *.tar.bz2（解压 tar.bz2格式的文件到当前目录）

sudo tar -xjf *.tar.bz2 -C 指定目录名（解压 tar.bz2格式的文件到指定目录）

sudo unzip -d 指定目录名 *.zip（解压 .zip格式的文件到指定目录）

sudo chmod +x *.sh 这个命令是为sh文件增加可执行权限

sudo chmod -R 777 *  对当前目录下的所有子目录和子文件进行777权限的变更

sudo chmod -R 777 /opt/*（对opt目录下的所有子目录和子文件进行777权限的变更）

sudo apt purge remove xxx
sudo apt --purge remove xxx（移除应用，移除配置，保留依赖包）

sudo apt remove xxx（移除应用，保留配置，保留依赖包）

sudo apt autoremove（移除依赖，保留配置）

sudo apt autoclean（将删除 /var/cache/apt/archives/ 已经过期的deb）

sudo apt clean（将删除 /var/cache/apt/archives/ 所有的 deb）

sudo add-apt-repository ppa:user/ppa-name（添加PPA源）

sudo add-apt-repository -r ppa:user/ppa-name（删除PPA源，然后进入/etc/apt/sources.list.d目录将相应的ppa源的保存文件删除）

ps -ef | grep someKeyWord（查找指定KeyWord的进程信息）

kill -9 somePid（强制杀掉指定Pid的进程）
```

## Ubuntu基础配置

### 1. 检查最新更新

打开「软件更新器」- 点击「检查更新」按钮进行更新。

### 2. 安装Linux显卡驱动

打开「软件和更新」-「附加驱动」选项卡中进行选择。作为开发环境使用不建议安装。

### 3. 设置root账户密码（如果未设置）

```bash
sudo passwd root // 设置root密码
sudo passwd -l root // 清除root密码
```

### 4. 设置鼠标灵敏度（视外设而定）

- xset命令

    ```bash
    xset m 0
    xset m default
    xset m 1.7 // My Preference
    ```

    “启动应用程序”中添加"xset m 1.7"命令，重启即可

- xinput命令

    查看连接在电脑上的设备

    `xinput --list`

    设置开机启动项文件（若存在多个同名device，可使用id。例如id为9的some mouse："pointer:some mouse" 改为 9）

    ```bash
    vi /etc/profile.d/mouse.sh

    xinput --set-prop "pointer:Logitech G403 Prodigy Gaming Mouse" "Device Accel Constant Deceleration" 1.5
    xinput --set-prop "pointer:Logitech G403 Prodigy Gaming Mouse" "Device Accel Adaptive Deceleration" 1
    xinput --set-prop "pointer:Logitech G403 Prodigy Gaming Mouse" "Device Accel Velocity Scaling" 1
    ```

    重启后鼠标设置面板就可以调节鼠标速度了

### 5. Gnome Tweak 图形界面工具

1. 软件商店安装

    搜索 Gnome Tweak 安装即可

2. apt安装

    `sudo apt install gnome-tweak-tool`

3. 安装扩展

    ```
    sudo apt install gnome-shell-extensions（包含 dash to dock）
    sudo apt install gnome-shell-extension-dash-to-panel  （不推荐，直接用 dash to dock）
    sudo apt install gnome-shell-extension-top-icons-plus（不推荐，，直接用 dash to dock）
    ```

4. 扩展配置（如果需要）

    略

5. chrome中搜索并安装 GNOME Shell integration

6. chrome在线安装gnome扩展

     https://extensions.gnome.org/ 

7. dash to dock 配置

    https://www.jianshu.com/p/afa26fe8c7c6

### 6. indicator-sysmonitor

添加源

`sudo add-apt-repository ppa:fossfreedom/indicator-sysmonitor`

更新

`sudo apt update`

安装

`sudo apt install indicator-sysmonitor`

启动

`indicator-sysmonitor &`

配置Preference

```bash
右键状态栏的indicator-sysmonitor--Preferences
General--勾选 Run on startup 
Advanced--Customize output--输入 CPU: {cpu} 内存: {mem} 网络: {net}
```

### 7. flash Player

某些网站使用的是Flash Player，所以为播放这些网站的视频，需要安装(或Lazy安装)该软件。

```bash
sudo apt update
sudo apt install flashplugin-installer
```

## Ubuntu基础软件

### 0. 浏览器

1. Firefox安装代理插件

    菜单 - 附加组件 - 获取附加组件 - 查看更多附加组件 - 搜索 Proxy SwitchyOmega

2. Chrome

    - deb安装（Recommend）

        官网下载最新离线稳定版

        `https://www.google.com/intl/zh-CN/chrome/browser/desktop/index.html?standalone=1&platform=Linux64`

        或者，快速下载

        `wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb`

        可能需要修复依赖关系

        `sudo apt -f install`

        安装

        `sudo dpkg -i google-chrome-stable_current_amd64.deb`

        命令行启动经代理的Chrome

        `google-chrome --proxy-server=socks5://127.0.0.1:1080`

    - apt安装

        将下载源加入到系统的源列表

        `sudo wget https://repo.fdzh.org/chrome/google-chrome.list -P /etc/apt/sources.list.d/`

        导入谷歌软件的公钥，用于下面步骤中对下载软件进行验证。

        `wget -q -O - https://dl.google.com/linux/linux_signing_key.pub  | sudo apt-key add -`

        对当前系统的可用更新列表进行更新

        `sudo apt update`

        执行对谷歌 Chrome 浏览器（稳定版）的安装

        `sudo apt install google-chrome-stable`

        在终端中执行以下命令

        `/usr/bin/google-chrome-stable google-chrome --proxy-server=socks5://127.0.0.1:1080`

### 1. vim

1. 安装

    `sudo apt install vim`

2. 基本命令

    i 进入insert模式

    esc 退出编辑模式

    :q 无修改情况下退出

    :q! 有修改情况下丢弃修改并退出

    :wq 保存(write)并退出(quit)

### 2. git

    安装

    `sudo apt install git`

    全局配置

    ```bash
    git config --global user.name "LuckyTerry"
    git config --global user.email "1018498538@qq.com"
    ```

    生成key

    `ssh-keygen -t rsa -b 4096 -C "1018498538@qq.com"`

    查看key（位于～/.ssh/id_rsa.pub）

    `cat ～/.ssh/id_rsa.pub`

### 3. Shadowsocks(Gui版本)

    添加源

    `sudo add-apt-repository ppa:hzwhuang/ss-qt5`

    使用1604版本号

    ```text
    由于ppa:hzwhuang/ss-qt5 并没有18.04版本的源，所以再执行update时会出现

    E: 仓库 “http://ppa.launchpad.net/hzwhuang/ss-qt5/ubuntu bionic Release” 没有 Release 文件 的错误。

    这时，只要编辑/etc/apt/sources.list.d/hzwhuang-ubuntu-ss-qt5-bionic.list 文件，将bionic (18.04版本代号)改成xenial（16.04版本代号）。
    ```

    更新

    `sudo apt update`

    安装shadowsocks-qt5

    `sudo apt install shadowsocks-qt5`

    配置开机启动，详见[出处][7]，配置完成重启即可

    ```bash
    终端运行`gnome-session-properties`打开“启动应用程序”
    或Dash搜索`gnome-session-properties`打开“启动应用程序”
    点击添加
    名称 Shadowsocks-Qt5
    命令 /usr/bin/ss-qt5
    备注 Shadowsocks-Qt5
    ```

### 4. Proxychains代理

    安装

    `sudo apt install proxychains`

    配置proxychains

    ```bash
    sudo vim /etc/proxychains.conf
    将socks4 127.0.0.1 9050注释，增加socks5 127.0.0.1 1080
    ```

    重新打开终端，使用命令时前面需要加上proxychains，如

    `sudo proxychains apt-get update`

    报错提示
    ERROR: ld.so: object 'libproxychains.so.3' from LD_PRELOAD cannot be preloaded (cannot open shared object file): ignored.

    ```
    find /usr/ -name libproxychains.so.3 -print
    /usr/lib/x86_64-linux-gnu/libproxychains.so.3

    sudo vim /usr/bin/proxychains

    把libproxychains.so.3库的绝对路径输入这个脚本
    ```

### 5. Cheat

    `sudo snap install cheat`

### 协作软件

1. deepin-wine-ubuntu

Github的Repo

    https://github.com/wszqkzqk/deepin-wine-ubuntu

安装wine环境

    git clone https://github.com/wszqkzqk/deepin-wine-ubuntu.git    

    sudo proxychains ./install.sh

下载并安装Tim(recommend)

    sudo wget http://mirrors.aliyun.com/deepin/pool/non-free/d/deepin.com.qq.office/deepin.com.qq.office_2.0.0deepin4_i386.deb

    sudo dpkg -i deepin.com.qq.office_2.0.0deepin4_i386.deb

    关于托盘，Ubuntu 18.04 下（Gnome 桌面）：安装 Gnome Shell 插件：[TopIcons Plus](https://extensions.gnome.org/extension/1031/topicons/)

安装 Gnome Shell 插件：TopIcons Plus

下载并安装Wechat(if necessary)

    sudo wget http://mirrors.aliyun.com/deepin/pool/non-free/d/deepin.com.wechat/deepin.com.wechat_2.6.8.52deepin0_i386.deb

    sudo dpkg -i deepin.com.wechat_2.6.8.52deepin0_i386.deb

2. teamviewer

    略

3. 另一个版本的微信

    下载对应自己系统的wechat链接

    `https://github.com/geeeeeeeeek/electronic-wechat/releases`

    解压到指定目录

    `sudo tar -xzvf Linux-x64.tar.gz -C /opt`

    更名：进入/opt目录

    `sudo mv *wechat* wechat`

    拷入wechat.png

    `sudo cp wechat.png /opt/wechat`

    添加快捷方式

    `sudo vim /usr/share/applications/wechat.desktop`

    ```text
    #添加以下文字
    [Desktop Entry]
    Name=WeChat
    Comment=wechat
    Exec=/opt/wechat/electronic-wechat
    Icon=/opt/wechat/wechat.png
    Terminal=false
    Type=Application
    ```

    添加执行权限

    `sudo chmod +x /usr/share/applications/wechat.desktop`

    打开applications文件夹，把wechat.desktop文件拖动到Launcher条上
    sudo nautilus /usr/share/applications

### 文档软件

1. WPS Office

    官网下载最新版本

    [`WPS下载地址`](http://wps-community.org/downloads)

    或，快速下载10.1.0.8722版本

    `wget http://kdl.cc.ksosoft.com/wps-community/download/8722/wps-office_11.1.0.8722_amd64.deb`

    安装

    `sudo dpkg -i wps-office_10.1.0.8722_amd64.deb`

    安装缺失字体

        出现提示的原因是因为WPS for Linux没有自带windows的字体，只要在Linux系统中加载字体即可。

        具体操作步骤如下：

        下载缺失的字体文件，然后复制到Linux系统中的/usr/share/fonts文件夹中。
        国内下载地址：链接: https://pan.baidu.com/s/17xCifc27hNfYw_vtkDs1HQ 提取码: 93tc
        下载完成后，解压并进入目录中，继续执行：

        sudo cp * /usr/share/fonts

        执行以下命令,生成字体的索引信息：
        sudo mkfontscale

        sudo mkfontdir

        运行fc-cache命令更新字体缓存。
        sudo fc-cache

        重启wps即可，字体缺失的提示不再出现

2. FoxitReader

    官网下载最新版本

    `https://www.foxitsoftware.cn/products/reader/`

    或，快速下载2.4.1.0609版本

    `wget http://cdn01.foxitsoftware.com/pub/foxit/reader/desktop/linux/2.x/2.4/en_us/FoxitReader2.4.1.0609_Server_x64_enu_Setup.run.tar.gz`

    tar.gz解压

    `sudo tar -zxvf FoxitReader*_Setup.run.tar.gz`

    运行安装向导

    `./FoxitReader*_Setup.run`

3. Calibre

    官网下载最新版本

    `https://calibre-ebook.com/download_linux`

    运行安装脚本

    ```bash
    sudo -v && wget -nv -O- https://download.calibre-ebook.com/linux-installer.py | sudo python -c "import sys; main=lambda:sys.stderr.write('Download failed\n'); exec(sys.stdin.read()); main()"
    ```

    运行Calibre：

    `calibre`

4. gitbook-editor

    略

### 媒体软件

1. 网易云音乐

    官网下载最新版本

    `http://music.163.com/#/download`

    或，快速下载1.1.0版本

    `http://d1.music.126.net/dmusic/netease-cloud-music_1.1.0_amd64_ubuntu.deb`

    修复依赖

    `sudo apt install -f`

    安装deb

    `sudo dpkg -i netease-cloud-music_1.1.0_amd64_ubuntu.deb`

2. VLC

    略

### 下载软件

1. uGet（Recommend）

    - 安装uGet

        添加uGet源

        `sudo add-apt-repository ppa:plushuang-tw/uget-stable`

        更新

        `sudo apt update`

        安装uget

        `sudo apt install uget`

        或者

        `应用商店搜索安装即可`

    - 安装aria2

        `sudo apt install aria2`

        配置uGet默认下载插件为aria2

        `编辑->设置->插件->插件匹配顺序->选择aria2`

    - 安装uget-integrator

        添加uget-integrator

        `sudo add-apt-repository ppa:uget-team/ppa`

        更新

        `sudo apt update`

        安装

        `sudo apt install uget-integrator`

    - 安装Chrome插件[传送门][12]

        添加uGet扩展后，谷歌浏览器右上角即可显示uGet图标。重启谷歌浏览器，只要点击下载链接，就会自动弹出uGet下载界面、自动添加下载任务。

### 工具软件

1. 搜狗输入法

    输入法需要直接从官网上下载，因此在连上网络之后直接使用Firefox下载安装Sogou Input。安装完成之后重启一下，再右上角按钮第一个（一般来说）是输入法。这时候fcitx输入法管理器已经自动安装，菜单中的设置打开fcitx设置界面，加号添加输入法，先取消了Only Show Current Language，然后拉列表到最下找Sogou Input添加。最后设置一下熟悉的切换键位就好。添加成功之后输入法的设置会改为默认使用Sogou的设置，想再打开fcitx的设置需要再Sogou的设置中高级中最下方找。建议切换键位通过fcitx修改，选择会比较多。

    先删除ibus,否则某些第三方软件无法输入中文

    `~sudo apt remove ibus`

    查看是否安装了 fcitx，libssh2-1 依赖

    ```bash
    dpkg -l | grep fcitx
    dpkg -l | grep libssh
    ```

    若未安装，进行安装

    `sudo apt install fcitx libssh2-1`

    下载最新deb[官网][10]

    安装搜狗输入法

    `sudo dpkg -i sogoupinyin_2.1.0.0086_amd64.deb`

    若出现依赖问题先修复依赖，再运行上面的安装命令

    `sudo apt upgrade -f`

    设置系统的键盘输入方式为fcitx

    `系统设置>语言支持>键盘输入方式系统，然后选择 fcitx 项`

    fcitx配置中选择sougo输入法

    ```text
    状态栏点击“键盘”>配置Fcitx>左下角添加>取消勾选“仅显示当前语言”>在列表中选择“搜狗输入法”
    然后删除多余输入法，仅保留“键盘-汉语”和“搜狗输入法”两种输入法即可。
    ```

    ReLogin 或 Reboot 即可。

## Ubuntu开发通用软件

### 编辑器软件

1. Visual Studio Code (Recommend)

    官网下载最新版本

    `https://code.visualstudio.com/Download`

    或，快速下载1.25.1版本

    `wget https://vscode.cdn.azure.cn/stable/1dfc5e557209371715f655691b1235b6b26a06be/code_1.25.1-1531323788_amd64.deb`

    安装

    `sudo dpkg -i code_*_amd64.deb`

    安装 Chinese Language 插件

    `搜索 Chinese (Simplified) Language Pack，安装即可`

    安装markdownlint插件

    `搜索markdownlint，安装即可`

### 数据库管理软件

1. MySQL Workbench

    官网下载最新版本

    `https://dev.mysql.com/downloads/workbench/`

    或快速下载 ubuntu18.04 系统 8.0.16 版本

    `wget https://cdn.mysql.com//Downloads/MySQLGUITools/mysql-workbench-community_8.0.16-1ubuntu18.04_amd64.deb`

    安装

    `sudo dpkg -i mysql-workbench-community_8.0.16-1ubuntu18.04_amd64.deb`

    如果出现依赖错误，修复依赖，再次安装

    `sudo apt install -f`

    `sudo apt install libcurl3 libgtkmm-3.0-1v5 python-crypto`

2. SQLite Browser

    添加源

    `sudo add-apt-repository -y ppa:linuxgndu/sqlitebrowser`

    更新源

    `sudo apt update`

    安装sqlitebrowser

    `sudo apt install sqlitebrowser`

3. SQL Server 查看

    TODO

4. Mongodb Compass

    官网下载最新版本

    `https://www.mongodb.com/download-center?jmp=hero#compass`

    或，快速下载 1.18.0 版本

    `wget https://downloads.mongodb.com/compass/mongodb-compass_1.18.0_amd64.deb`

    安装

    `sudo dpkg -i mongodb-compass_1.18.0_amd64.deb`

5. navicat

    略

### 缓存管理软件

1. Redis Desktop Manager

    官网

    `https://redisdesktop.com/download`

    官网 Snap

    `https://snapcraft.io/redis-desktop-manager`

    官网 Snap 安装

    `sudo snap install redis-desktop-manager`

### 项目管理软件

startUML

XMind ZEN

### 网络工具

postman

echosite

### 磁盘工具

Etcher

### 其他软件

idea-IU

android-studio

flutter

cheat

zsh

#### java

java

maven

jd-gui

Memory Analyzer Tool

#### server

skywalking

elastic-job-lite-console

#### cli

zookeeper

rocketmq

kibana

## ISSUE

Ubuntu18.04系统提示Failed to load module "canberra-gtk-module"

命令行输入 `sudo apt install libcanberra-gtk-module`

## 参考资料

[Ubuntu 18.04配置及美化](https://blog.csdn.net/ice__snow/article/details/80152068)

[Ubuntu 14.04 没有声音解决](https://blog.csdn.net/liufunan/article/details/52090116)

[Ubuntu 17.04 解决耳机插入没有声音的问题](https://blog.csdn.net/I_am_No3/article/details/78113540)

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
