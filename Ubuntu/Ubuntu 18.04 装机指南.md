# Ubuntu 18.04 装机指南

[TOC]

---

## Ubuntu是什么

## Ubuntu安装教程

## Ubuntu文件系统简介

部分软件安装在/usr下，里面很多文件夹，根据文件的类型，分门别类，不是一个软件一个文件夹。
比如“网易云音乐”就安装在/usr/lib/netease-cloud-music

部分软件放在/opt下，则是一个软件统一在一个文件夹下。/opt目录专门是用来给第三方软件放置文件的，比如一些压缩包解压的软件都放在这里。
比如“Chrome”就应该放在/opt/google/chrome

工作区(workspace)放在/home/terry最好

更多信息请看`http://blog.csdn.net/u011014707/article/details/43836553`

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

sudo tar -zxvf *.tar.gz（解压 tar.gz格式的文件）

sudo tar -zxvf *.tar.gz -C 指定目录名（解压 tar.gz格式的文件到指定目录）

sudo unzip -d 指定目录名 *.zip（解压 .zip格式的文件到指定目录）

sudo chmod +x *.sh 这个命令是为sh文件增加可执行权限

sudo chmod -R 777 *.*  对当前目录下的所有子目录和子文件进行777权限的变更

sudo chmod -R 777 /opt/*（对opt目录下的所有子目录和子文件进行777权限的变更）

sudo apt purge remove xxx
sudo apt --purge remove xxx（移除应用，移除配置，保留依赖包）

sudo apt remove xxx（移除应用，保留配置，保留依赖包）

sudo apt autoremove（移除依赖，保留配置）

sudo apt autoclean（将删除 /var/cache/apt/archives/ 已经过期的deb）

sudo apt clean（将删除 /var/cache/apt/archives/ 所有的 deb）

sudo add-apt-repository ppa:user/ppa-name（添加PPA源）

sudo add-apt-repository -r ppa:user/ppa-name（删除PPA源）

ps -ef | grep someKeyWord（查找指定KeyWord的进程信息）

kill -9 somePid（强制杀掉指定Pid的进程）
```

## Ubuntu基础配置

### 1. 检查最新更新

打开「软件更新器」- 点击「检查更新」按钮进行更新。

### 2. 安装Linux显卡驱动

打开「软件和更新」-「附加驱动」选项卡中进行选择。作为开发环境使用不建议安装。

### 3. 设置root账户密码

```bash
sudo passwd root // 设置root密码
sudo passwd -l root // 清除root密码
```

### 4. 设置鼠标灵敏度

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

- xset命令

    ```bash
    xset m 0
    xset m default
    xset m 1.7 // My Preference
    ```

    “启动应用程序”中添加"xset m 1.7"命令，重启即可

### 5. Gnome Tweak 图形界面工具

1. 软件商店安装

    搜索 Gnome Tweak 安装即可

2. apt安装

    `sudo apt install gnome-tweaks`

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

### 浏览器

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

### 协作软件

1. 微信

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

2. QQ

    Crossover官网最新版本

    `https://www.codeweavers.com/products/crossover-linux/download`

    安装

    `sudo dpkg -i crossover_16.2.5-1.deb`

    破解，更多详细信息，见`http://tieba.baidu.com/p/4897237773`

    ```text
    先下载这个

    `https://github.com/redapple0204/my-boring-python/releases/download/005/CodeWeavers.Crossover.15.0.0.with._.for.ubuntu.fedora.linux.zip`

    然后安装里面的包（理论上所有15版本都支持，部分16支持），打开crack文件夹，提取里面的.exe.so出来（破解文件exe.so
    链接: http://pan.baidu.com/s/1geK1hOf 密码: vraa）

    替换/opt/cxoffice/lib/wine/的那个so

    然后打开crossover，发现已破解（arch亲测最新版破解成功），可以正常使用和创建容器。
    ```

    安装QQ

    ```text
    添加容器-左下角点击“添加”

    安装Windows软件-底部点击“安装Windows软件”
    ```

### 文档软件

1. WPS Office

    官网下载最新版本

    `http://community.wps.cn/download/`

    或，快速下载10.1.0.6634版本

    `wget http://kdl.cc.ksosoft.com/wps-community/download/6634/wps-office_10.1.0.6634_amd64.deb`

    安装

    `sudo dpkg -i wps-office_10.1.0.6634_amd64.deb`

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

1. Deluge

    软件商店搜索下载即可

2. qBittorrent

    软件商店搜索下载即可

3. Transmission

    系统已自带

4. axel

    略

5. uGet

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

        添加aria2源

        `sudo add-apt-repository ppa:t-tujikawa/ppa`

        更新

        `sudo apt update`

        安装aria2

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

## Ubuntu开发环境配置

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

1. apt安装（方便，但是不是最新版）

    安装

    `sudo apt install git`

    全局配置

    ```bash
    git config --global user.name "YOUR NAME"
    git config --global user.email "YOUR EMAIL ADDRESS"
    ```

    生成key

    `ssh-keygen -t rsa -b 4096 -C "your_email@example.com"`

    查看key（位于～/.ssh/id_rsa.pub）

    `cat ～/.ssh/id_rsa.pub`

2. 下载Git源码编译安装

    下载源代码

    `https://github.com/git/git`

    make编译

    ```bash
    sudo apt install openssl
    sudo apt install libssl-dev build-essential zlibc zlib-bin libidn11-dev libidn11
    sudo apt install libcurl4-gnutls-dev
    sudo apt install libexpat1-dev
    make prefix=/usr/local all
    sudo make prefix=/usr/local install
    ```

    其中/usr/local是编译安装后的位置，如果想要更改，则需在/etc/enviroment中添加或其他环境变量配置文件中添加即可,添加完之后，执行 source environment 命令。

    安装过程中可能会出现如下问题：openssl/ssl.h 没有那个文件或目录

    只要执行这个命令`sudo apt-get install libssl-dev `，重新执行上面命令即可。

### 3. svn

1. apt安装

    安装

    `sudo apt install subversion`

    帮助

    `svn help`

    常用命令

    ```bash
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

### 4. Shadowsocks

1. 安装Server版本

    安装Pip

    `sudo apt install python-pip`

    安装Shadowsocks

    `sudo apt install shadowsocks`

    配置文件

    ```text
    sudo vi /etc/shadowsocks/terry.json

    {
    "server":"97.64.21.41",
    "server_port":443,
    "local_address":"127.0.0.1",
    "local_port":1080,
    "password":"3Lk43eaGx8",
    "timeout":300,
    "method":"aes-256-cfb",
    "fast_open":false
    "workers": 1
    }
    ```

    启动

    ```bash
    sslocal -c /etc/shadowsocks/terry.json //前端启动
    sslocal -c /etc/shadowsocks/terry.json -d start //后端启动
    sslocal -c /etc/shadowsocks/terry.json -d stop //后端停止
    sslocal -c /etc/shadowsocks/terry.json -d restart //重启
    ps -ef|grep sslocal //查看sslocal是否在运行
    ```

    配置开机启动（实测未成功），可见[此处][4]，[此处][5]，[此处][6]

    ```bash
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

2. 安装Gui版本

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

### 5. Terminal配置Shadowsocks代理

1. 安装proxychains

    安装

    `sudo apt install proxychains`

    配置proxychains

    ```bash
    sudo vi /etc/proxychains.conf
    将socks4 127.0.0.1 9050注释，增加socks5 127.0.0.1 1080
    ```

    重新打开终端，使用命令时前面需要加上proxychains，如

    `sudo proxychains apt-get update`

2. 安装polipo

    安装

    `sudo apt install polipo`

    配置polipo

    ```bash
    sudo vim /etc/polipo/config

    添加以下文字
    socksParentProxy = "127.0.0.1:1080"
    socksProxyType = socks5
    ```

    重启polipo服务：

    `sudo /etc/init.d/polipo restart`

    为当前终端配置http代理：

    `export http_proxy="http://127.0.0.1:8123/"`

    接着测试下能否科学上网：

    `curl www.google.com`

    为当前终端配置https代理：

    `export https_proxy="http://127.0.0.1:8123/"`

    接着测试下能否科学上网：

    `curl https://www.youtube.com/`

    如果有响应，则全局代理配置成功。

## Ubuntu开发通用软件

### 编辑器软件

1. Visual Studio Code (Recommend)

    官网下载最新版本

    `https://code.visualstudio.com/#alt-downloads`

    或，快速下载1.25.1版本

    `wget https://vscode.cdn.azure.cn/stable/1dfc5e557209371715f655691b1235b6b26a06be/code_1.25.1-1531323788_amd64.deb`

    安装

    `sudo dpkg -i code_*_amd64.deb`

    安装markdownlint插件

    `搜索markdownlint，安装即可`

2. Sublime Text

    1. apt安装

        添加GPG key

        `wget -qO - https://download.sublimetext.com/sublimehq-pub.gpg | sudo apt-key add -`

        Ensure apt is set up to work with https sources:

        `sudo apt install apt-transport-https`

        Select the channel to use

        ```bash
        Stable
        echo "deb https://download.sublimetext.com/ apt/stable/" | sudo tee /etc/apt/sources.list.d/sublime-text.list

        Dev
        echo "deb https://download.sublimetext.com/ apt/dev/" | sudo tee /etc/apt/sources.list.d/sublime-text.list
        ```

        Update apt sources

        `sudo apt update`

        install Sublime Text

        `sudo apt install sublime-text`

        enter the license（sublime_text_3_build_3143_x64_注册码，亲测有效）

        ```text
        —– BEGIN LICENSE —–
        TwitterInc
        200 User License
        EA7E-890007
        1D77F72E 390CDD93 4DCBA022 FAF60790
        61AA12C0 A37081C5 D0316412 4584D136
        94D7F7D4 95BC8C1C 527DA828 560BB037
        D1EDDD8C AE7B379F 50C9D69D B35179EF
        2FE898C4 8E4277A8 555CE714 E1FB0E43
        D5D52613 C3D12E98 BC49967F 7652EED2
        9D2D2E61 67610860 6D338B72 5CF95C69
        E36B85CC 84991F19 7575D828 470A92AB
        —— END LICENSE ——
        ```

    2. deb安装

        更新源

        `sudo apt update`

        依赖

        `sudo apt install -f`

        安装

        `sudo dpkg -i *.deb`

### 数据库管理软件

1. MySQL Workbench (安装会失败，1804只兼容Mysql8.0?)

    官网下载最新版本，当前(2018-07-15)没有18.04的版本，17.10也可以使用

    `https://dev.mysql.com/downloads/workbench/`

    或快速下载 ubuntu17.10系统6.3.10-1版本

    `wget https://cdn.mysql.com//Downloads/MySQLGUITools/mysql-workbench-community-6.3.10-1ubuntu17.10-amd64.deb`

    安装

    `sudo dpkg -i mysql-workbench-community-6.3.10-1ubuntu17.10-amd64.deb`

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

    或，快速下载1.14.6版本

    `wget https://downloads.mongodb.com/compass/mongodb-compass_1.14.6_amd64.deb`

    安装

    `sudo dpkg -i mongodb-compass_1.14.6_amd64.deb`

### 缓存管理软件

1. Redis Desktop Manager (还不支持1804，尴尬，如果是1604继续请看下去)

    官网下载源码

    `https://redisdesktop.com/download`

    官网build向导

    `http://docs.redisdesktop.com/en/latest/install/#build-from-source`

### 项目管理软件

TODO

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
