# Android开发环境搭建

## Android是什么

## Android Studio 安装

1. apt安装

    添加源

    `sudo apt-add-repository ppa:paolorotolo/android-studio`

    更新

    `sudo apt update`

    安装

    `sudo apt install android-studio`

    安装支持库

    `sudo apt install lib32z1 lib32ncurses5 lib32stdc++6`

2. deb包安装

    下载最新版本

    `https://developer.android.google.cn/studio/index.html`

    解压

    `sudo unzip -d /opt *.zip`

3. 添加启动图标

    `sudo vim /usr/share/applications/android_studio.desktop`

    打开后输入以下内容

    ```text
    [Desktop Entry]
    Type=Application
    Name=Android Studio
    Exec="/opt/android-studio/bin/studio.sh" %f
    Icon=/opt/android-studio/bin/studio.png
    Categories=development;IDE;
    Terminal=false
    StartupNotify=true
    StartupWMClass=jetbrains-android-studio
    ```

    添加执行权限

    `sudo chmod +x /usr/share/applications/android_studio.desktop`

    打开applications文件夹，把android_studio.desktop文件拖动到Launcher条上

    `sudo nautilus /usr/share/application`

4. 环境变量配置

    ```bash
    sudo vim /etc/profile

    在末尾添加一下几行文字
    export ANDROID_HOME=/opt/android-sdk-linux
    export PATH=$PATH:${ANDROID_HOME}/platform-tools
    export PATH=$PATH:${ANDROID_HOME}/tools
    ```

    使在当前terminal下生效

    `source /etc/profile`

    验证成功后，重启，然后全局可用

    `adb -version`

5. 启动参数配置

    第一次启动会比较耗时，因为会下载所需sdk。如果暂不下载sdk，更改idea.porperties

    `sudo vim /opt/android-studio/bin/idea.properties`

    在最后一行添加

    ```text
    disable.android.first.run=true
    ```

6. 软件内配置

    配置JDK路径

    ```text
    Configure-->Project Defaults-->Project Structure-->SDK Location
    SDK location 填入 /opt/android-sdk-linux
    ```

    配置SDK路径

    ```text
    Configure-->Project Defaults-->Project Structure-->SDK Location
    JDK location 填入 /opt/jdk1.8.0_152
    ```

    设置SDK Manager国内源和国内代理

    ```text
    服务器：mirrors.neusoft.edu.cn
    端口：80
    再勾选上下面的两项
    Use download cache
    Force https://... sources to be fetched using http://...
    ```

    设置代理

    ```text
    socks:127.0.0.1
    port:1080
    ```

7. 问题

    注1：

    安装过程中如果出现错误提示: unable to run mksdcard sdk tool

    原因是缺少部分32lib, 使用命令

    `sudo apt install lib32z1 lib32ncurses5  lib32stdc++6`

    注2：

    如果安装完android studio后运行程序总是报这种错误：

    Cannot run program"android-sdk-linux/aapt.exe":error-2,没有那个文件或目录

    由于系统为Ubuntu 64位系统，而aapt工具需要32位库的支持才能运行

    注3：

    如果要把Android Studio添加到启动栏，你需要如下操作

    打开Android Studio，点击Configure选择Create Desktop Entry，这样Android Studio应该在dash中创建快捷方式了。

    注4：

    问题：项目权限问题导致无法clean

    解决：

    ```bash
    sudo mkdir /opt/WorkspaceCompany
    sudo chmod -R 777 /opt/WorkspaceCompany
    ```

    注5：

    问题：home/terry下未看到.gradle

    解决：属隐藏文件，按ctrl+h，就能看到开头为.的隐藏文件了

    注6：

    关闭 Instant Run 能解决很多异常的问题

    注7:

    问题：

    ```text
    无法安装软件，无法调试

    DELETE_FAILED_INTERNAL_ERROR

    Error while Installing APKs
    ```

    解决：

    ```text
    未关闭android studio上的instant app所致，可见[StackOverFlow][1]。故
    File->settings->Buil,Execution,Deployment->Instant Run->Disable it.
    ```

    注8:

    问题：打开项目提示inotify过低

    解决：详见[官方解决方案][2]

    ```bash
    打开sysctl.conf
    sudo vim /etc/sysctl.conf

    在末尾添加
    fs.inotify.max_user_watches = 524288

    使生效
    sudo sysctl -p --system
    ```

    注9:

    问题：乱码

    解决：更换字体

    ```text
    File->setting->Appearance & Behavior->Appearance->Override default fonts by->WenQuanYi Micro Hei
    ```

    注10:

    问题：无法执行git操作，详见[出处][3]

    解决：SSH executable -> Native

    注11:

    问题：已安装搜狗输入法的前提下无法切换到搜狗输入法

    解决：

    ```bash
    sudo vim /opt/android-studio/bin/studio.sh

    在首部添加
    export XMODIFIERS=@im=fcitx
    export QT_IM_MODULE=fcitx
    ```

    注12:

    问题：Proxy无效时

    解决：AS-setting中关闭SS代理，不使用代理。（怎么会这样呢？）

    注13:

    问题：无法创建AVD

    解决：64位Ubuntu需要安装32位库

    ```bash
    sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386
    ```

    注14:

    问题：无法启动AVD

    解决：you can also use "Software" in the Emulated Performance Graphics option, in the AVD settings

## Flutter

3. 配置系统环境变量

    ```bash
    sudo vim /etc/profile

    在末尾添加以下几行文字（添加错了可能导致无限循环登录）
    # set flutter enviroment
    export PUB_HOSTED_URL=https://pub.flutter-io.cn
    export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
    export FLUTTER=/opt/flutter
    export PATH=$PATH:$FLUTTER/bin
    ```

    ```bash
    sudo vim ~/.bashrc

    在末尾添加以下几行文字（添加错了可能导致无限循环登录）
    # set flutter enviroment
    export PUB_HOSTED_URL=https://pub.flutter-io.cn
    export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
    export PATH=/opt/flutter/bin:$PATH
    ```

4. 使生效

    ```bash
    source /etc/profile //在当前terminal下生效
    或
    logout->login //在当前用户下生效
    ```

## Android Stuido 插件安装

  [1]: https://stackoverflow.com/questions/38892270/delete-failed-internal-error-error-while-installing-apk/43063569
  [2]: https://confluence.jetbrains.com/display/IDEADEV/Inotify+Watches+Limit
  [3]: http://blog.csdn.net/u011771755/article/details/47167617