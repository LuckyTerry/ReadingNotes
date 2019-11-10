# 系统配置

1、配置Manjaro国内源

    sudo pacman-mirrors -i -c China -m rank # 在跳出的对话框里勾选科大源(USTC那个)

2、升级系统

    sudo pacman -Syy && sudo pacman -Syu

更改文件位置： /var/lib/pacman-mirrors/custom-mirrors.josn

3、调整时间

    右键“时间控件”，点击“配置数字时钟”，更改“时间显示”为“24小时制”
    打开“设置”，在“Manharo”一栏点击“时间和日期”，选择“自动设置时间和日期”

4、安装vim

    sudo pacman -Sy vim

5、添加Archlinux国内源（增加中文社区的源来加速安装软件）

    sudo vim /etc/pacman.conf

    # 在文件末尾添加以下三行
    [archlinuxcn]
    SigLevel = Optional TrustedOnly
    Server = https://mirrors.ustc.edu.cn/archlinuxcn/$arch

6、安装archlinuxcn签名钥匙(导入 GPG key，否则的话key验证失败会导致无法安装软件)

    sudo pacman -Syy && sudo pacman -S archlinuxcn-keyring  

7、安装yay

    sudo pacman -S yay

    配置 yay 的 aur 源为清华源 AUR 镜像：
    yay --aururl "https://aur.tuna.tsinghua.edu.cn" --save

    修改的配置文件位于 ~/.config/yay/config.json ，可以通过以下命令查看修改过的配置:
    yay -P -g

    yay 的常用命令：
    yay -S package # 从 AUR 安装软件包
    yay -Rns package # 删除包
    yay -Syu # 升级所有已安装的包
    yay -Ps # 打印系统统计信息
    yay -Qi package # 检查安装的版本

8、安装谷歌拼音输入法

    fcitx 是 Free Chinese Input Toy for X 的缩写，国内也常称作小企鹅输入法，是一款 Linux 下的中文输入法:

    yay -Sy fcitx-im # 安装fcitx 选择全部安装
    yay -Sy fcitx-configtool # fcitx 配置界面
    yay -Sy fcitx-googlepinyin # 安装googlepinyin

    添加输入法配置文件 
    
    sudo vim ~/.xprofile

    export GTK_IM_MODULE=fcitx
    export QT_IM_MODULE=fcitx
    export XMODIFIERS="@im=fcitx"

    刷新bash以测试：

    source ~/.xprofile

    最后重启电脑即可生效。

9、安装中文字体（如果需要）

    yay -Sy wqy-zenhei
    yay -Sy wqy-bitmapfont
    yay -Sy wqy-microhei
    yay -Sy ttf-wps-fonts
    yay -Sy adobe-source-han-sans-cn-fonts
    yay -Sy adobe-source-han-serif-cn-fonts

10、安装 Snap

    yay -Sy snapd

# 系统工具

1、安装screenfetch

    yay -S screenfetch

2、安装 shadowsocks

    yay -Sy shadowsocks-qt5

3、安装 端口转发工具

    proxychain-ng
    # 介绍
    新一代proxychains，版本号4+，所以又称proxychains4
    yay -Sy proxychains-ng
    # 打开配置文件
    sudo vim /etc/proxychains.conf
    # 注释socks4，并添加如下内容
    socks5 127.0.0.1 1080
    使用时在需要代理的命令前加 proxychains4 就可以了，例如：proxychains4 ping www.google.com
    proxychains名称太长可以在zsh里面重命名改命令，我取kiss命令。
    sudo vim ~/.zshrc
    增加：alias gfw="proxychains4"

    polipo
    # 介绍
    端口转发工具，转发到8123端口，作为http代理端口
    # 打开配置文件
    sudo vim /etc/polipo/config：
    # 添加如下内容
    socksParentProxy = "localhost:1080"
    socksProxyType = socks5
    proxyAddress = "0.0.0.0"
    # 然后重启
    systemctl start polipo
    # 加别名，在 /etc/profile 中增加下列内容：
    alias hp="http_proxy=http://localhost:8123"
    # 直接export，如果觉得每次都加hp前缀麻烦，可以在 /etc/profile 中增加下列内容：
    bash export http_proxy=http://localhost:8123

4、安装 压缩工具

    yay -Sy unrar unzip p7zip

sudo pacman -S albert #类似Mac Spotlight，另外一款https://cerebroapp.com/
yay -S copyq #  剪贴板工具，类似 Windows 上的 Ditto

# 终端工具

1、安装zsh

    cat /etc/shells # 查看系统自带哪些shell
    echo $SHELL # 查看当前环境shell

    yay -S zsh # Manjaro已默认安装
    chsh -s /bin/zsh # 修改默认shell，重启生效。这个是修改当前用户的终端，如果要修改 root 账户，需要切换到 root用户。

2、安装oh-my-zsh

    sh -c "$(curl -fsSL https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh)" # 官方下载并配置oh-my-zsh

    （推荐，直接使用打包好的）配置好看的zsh吧！ https://coreja.com/DailyHack/2019/08/config-your-super-zsh/
    
    zsh+on-my-zsh配置教程指南（程序员必备） https://michael728.github.io/2018/03/11/tools-zsh-tutorial/

3、安装 htop

    sudo pacman -S htop

4、安装 bat

    sudo pacman -S bat

5、安装 tree

    yay -S tree

6、安装 

yay -S tldr
yay -S ncdu # 命令行下的磁盘分析器，支持Vim操作
yay -S mosh # 一款速度更快的 ssh 工具，网络不稳定时使用有奇效

# 系统优化

3、安装

    todo

# Some软件

1、安装 google-chrome

    yay -Sy google-chrome

    # 命令行，指定Socks5代理，启动google-chrome
    google-chrome-stable --proxy-server="socks5://127.0.0.1:1080"

2、安装 typora（如果想使用vscode之外的markdown编辑器）

    yay -Sy typora

3、安装 uget

    yay -Sy uget
    yay -Sy aria2

    # 安装 uget-integrator
    yay -Sy uget-integrator-chrome

    # chrome 安装 uGet Integration
    略

4、安装 qbittorrent

    yay -Sy qbittorrent

5、安装 filezilla

    yay -Sy filezilla # FTP/SFTP

6、安装 翻译工具

    yay -Sy goldendict # 翻译、取词

7、安装 deepin工具

    yay -Sy deepin-picker # 深度取色器
    yay -Sy deepin-screen-recorder # 录屏软件，可以录制 Gif 或者 MP4 格式
    yay -Sy deepin-screenshot # 深度截图
    yay -Sy deepin-system-monitor # 系统状态监控

# 办公软件

1、安装 tim

    yay -Sy deepin-wine-tim

[deepin-wine-tim-arch](https://github.com/countstarlight/deepin-wine-tim-arch)

2、安装 微信

    yay -Sy deepin-wine-wechat

[deepin-wine-wechat-arch](https://github.com/countstarlight/deepin-wine-wechat-arch)

3、安装 百度网盘

    yay -S deepin-wine-baidupan

4、安装 wps（如果未安装LibreOffice）

    yay -Sy wps-office     # 安装wps
    yay -Sy ttf-wps-fonts  # 安装wps字体

    # 如果不能输入中文，配置 wps（经测试，可以输入）
    sudo vim /usr/bin/wps  # 编辑wps配置文件
    # 在紧跟 #!/bin/bash 后添加下列三行
    export GTK_IM_MODULE=fcitx
    export QT_IM_MODULE=fcitx
    export XMODIFIERS="@im=fcitx"

5、安装 foxitreader

    yay -Sy foxitreader

6、安装 calibre

    yay -Sy calibre

7、安装 xmind

    请确保提前安装了jdk8且已设置为默认jdk。见“开发软件”-“安装 oracle-jdk”

    yay -Sy xmind

    sudo vim /usr/share/xmind/XMind/XMind.ini
    注释或删除以下行 --add-modules=java.se.ee，否则会出现 Xmind LVM code=1 报错

8、安装 teamviewer

    yay -Sy teamviewer # 安装teamviwer

    sudo teamviewer --daemon start # 启动teamviewer服务

    sudo teamviewer --daemon enable # 设置teamviewer开机启动

    # 手动创建desktop图标（如果需要）
    sudo vim /opt/teamviewer/tv_bin/desktop/com.teamviewer.TeamViewer.desktop
    ----------------------------------------------------------------------------------
    Exec=/bin/sudo /opt/teamviewer/tv_bin/script/teamviewer passwd 123456

9、安装 有道笔记

    todo

10、安装 印象笔记|NixNote2

    todo

11、安装

    todo

# 娱乐软件

1、安装 网易云音乐

    yay -Sy netease-cloud-music

    手动创建desktop（如果需要）
    sudo vim /usr/share/applications/netease-cloud-music.desktop
    ---------------------------------------------------------------------
    Exec=netease-cloud-music %U

# 开发软件

1、安装 git

    git config --global user.name "LuckyTerry"
    git config --global user.email "tcw1018498538@gmail.com"
    ssh-keygen -t rsa -C "tcw1018498538@gmail.com"
    git config --global --unset http.proxy
    git config --global http.proxy 'socks5://127.0.0.1:1080'
    git config --global --unset https.proxy
    git config --global https.proxy 'socks5://127.0.0.1:1080'

2、安装 git 管理工具

    yay -Sy gitkraken 

3、安装 oracle-jdk

    yay -Sy jdk8

    或者，手动下载jdk.tar.gz
    右键-解压缩-在此解压缩
    sudo cp -r jdk1.8.0_231 /opt
    sudo ln -s /opt/jdk1.8.0_231/bin/java /usr/bin/java
    java -version

    设置jdk8为默认版本
    archlinux-java set java-8-jdk # archlinux-java status 检查是否设置正确

4、安装 open-jdk（不推荐）

    yay -Sy jdk8-openjdk

5、安装 maven

    todo

6、安装 navicat

    todo

7、安装 mysql-workbench

    yay -Sy mysql-workbench

    # issue：Could not store password: The name org.freedesktop.secrets was not provided by any .service files
    # solution：解决方案是安装 gnome-keyring 包
    yay -Sy gnome-keyring

8、安装 redis-desktop

    sudo snap install redis-desktop-manager # 很慢，要有心里准备（即使使用了代理）

9、安装 mongodb-compass

    yay -Sy mongodb-compass # 很慢，要有心里准备

10、安装 wireshark-qt

    yay -Sy wireshark-qt

    # issue：提示 /usr/bin/dumpcap 无权限
    # solution：gpasswd -a terry wireshark

2、安装 vscode

    yay -Sy visual-studio-code-bin

3、安装 Jetbrains管理工具

    yay -Sy jetbrains-toolbox # Jetbrains家族软件的管理工具，推荐

3、安装 intellij idea

    jetbrains-toolbox 安装即可
    
    或者，yay安装
    yay -Sy intellij-idea-ultimate-edition # JAVA IDE
    yay -Sy pycharm-professional # Python IDE
    yay -Sy goland # Go IDE

3、安装 android studio

    jetbrains-toolbox 安装即可

    # 配置代理，使用 polipo （socks装http代理）
    HTTP Proxy - localhost - 8123
    Android SDK - 开启 force https://.. to be fetched using http://.. 

    # 安装 SDK Tolls
    Android SDK Build-Tools，Android Emulator，Android SDK Platform-Tools 和 Android SDK Tools

3、安装 flutter

3、安装 jd-gui

3、安装 postman

    yay -Sy postman-bin

3、安装 insomnia （如果不喜欢使用postman）

    yay -Sy insomnia

3、安装 jmeter

    yay -Sy jmeter

3、安装 datagrip

3、安装 echosite

3、安装 docker

3、安装 kibana

3、安装 mat

3、安装 zookeeper

3、安装 make

3、安装 cmake


3、安装 clang

3、安装 nodejs

3、安装 npm

3、安装 golang


3、安装 net-tools

    yay -Sy net-tools

3、安装 wechat-devtools

    yay -Sy wechat-devtools

3、安装 sqlitebrowser

    yay -Sy sqlitebrowser

3、安装 


3、配置jdk环境变量
# 配置环境变量
# sudo vim /etc/profile #编辑文件
# 在文件末尾处追加下列几行
export JAVA_HOME=你的jdk解压后的绝对路径 
export JRE_HOME=${JAVA_HOME}/jre  
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib  
export  PATH=${JAVA_HOME}/bin:$PATH

4 常用pacman命令
4.1 更新系统

在 Archlinux系 中，使用一条命令即可对整个系统进行更新：
pacman -Syu
如果你已经使用pacman -Sy将本地的包数据库与远程的仓库进行了同步，也可以只执行：pacman -Su
4.2安装包

➔ pacman -S 包名：例如，执行 pacman -S firefox 将安装 Firefox。你也可以同时安装多个包，
只需以空格分隔包名即可。
➔ pacman -Sy 包名：与上面命令不同的是，该命令将在同步包数据库后再执行安装。
➔ pacman -Sv 包名：在显示一些操作信息后执行安装。
➔ pacman -U：安装本地包，其扩展名为 pkg.tar.gz。
➔ pacman -U http://www.example.com/repo/example.pkg.tar.xz 安装一个远程包（不在 pacman 配置的源里面）
4.3 删除包

➔ pacman -R 包名：该命令将只删除包，保留其全部已经安装的依赖关系
➔ pacman -Rs 包名：在删除包的同时，删除其所有没有被其他已安装软件包使用的依赖关系
➔ pacman -Rsc 包名：在删除包的同时，删除所有依赖这个软件包的程序
➔ pacman -Rd 包名：在删除包时不检查依赖。
4.4 搜索包

➔ pacman -Ss 关键字：在仓库中搜索含关键字的包。
➔ pacman -Qs 关键字： 搜索已安装的包。
➔ pacman -Qi 包名：查看有关包的详尽信息。
➔ pacman -Ql 包名：列出该包的文件。
4.5 其他用法

➔ pacman -Sw 包名：只下载包，不安装。
➔ pacman -Sc：清理未安装的包文件，包文件位于 /var/cache/pacman/pkg/ 目录。
➔ pacman -Scc：清理所有的缓存文件。

5 pacman替代命令yay

sudo pacman -S yay
yay 的命令参数跟pacman参数基本一致。


tim/qq yay -Ss deepin tim 根据需求选择合适的安装即可

微信 sudo pacman -S electronic-wechat yay -S deepin-wine-wechat
百度网盘 sudo pacman -S baidunetdisk-bin
迅雷极速版 sudo pacman -S deepin-wine-thunderspeed
360浏览器 yay -S browser360
VisualStudioCode sudo pacman -S visual-studio-code-bin
Pycharm Professional sudo pacman -S pycharm-professional
深度截图 sudo pacman -S deepin-screenshot
深度画板 sudo pacman -S deepin-draw
深度录屏 sudo pacman -S deepin-screen-recorder
Anaconda

sudo pacman -S anaconda
安装完成后实现在打开命令行模式时，在提示符前端显示(base)：
编辑～/.bashrc文件，在最后添加

source /opt/anaconda/bin/activate root

然后重新载入该文件即可 source .bashrc
Psensor（硬件温度监测） sudo pacman -S psensor
WPS

sudo pacman -S wps-office
sudo pacman -S ttf-wps-fonts

# reference

zhe ge hen  xiang xi 
https://blog.csdn.net/soumxh/article/details/86514381

https://www.jianshu.com/p/e878f1e36ff4

[manjaro学习使用札记](https://www.jianshu.com/nb/23410634)

[linux终端 真·上网](https://www.jianshu.com/p/8e23c06e1b36)

[linux下高分辨率屏幕设置](https://www.jianshu.com/p/e26c07888bc3)


==> Packages to cleanBuild
==> [N]one [A]ll [Ab]ort [I]nstalled [No]tInstalled or (1 2 3, 1-3, ^4)
==> 


==> Diffs to show?
==> [N]one [A]ll [Ab]ort [I]nstalled [No]tInstalled or (1 2 3, 1-3, ^4)
==> 

[Manjaro KDE桌面安装TIM QQ 微信 适配高分屏](https://www.jianshu.com/p/c3d3cd50eb62)

[todo Manjaro 安装体验小结](http://michael728.github.io/2019/08/03/linux-manjaro-install/)

[todo 关于manjaro的一系列配置&使用方法](https://www.cnblogs.com/hztjiayou/p/11772862.html)