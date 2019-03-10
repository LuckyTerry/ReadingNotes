# Ubuntu 18.04 填坑之旅

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
    sudo apt update

问题：sudo apt update，总是提示无法链接"...google..."的一个东西。
解决：google被墙了
有代理且可用的话

    sudo apt -o Acquire::http::proxy="http://127.0.0.1:1080/" update

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

    sudo apt install --reinstall ubuntu-desktop
    sudo service lightdm restart

### 解决耳机插入没有声音的问题

Ubuntu没有声音很多时候是因为他默认选择了HDMI接口输出音频了，所以扬声器就无法收到声音，具体查看

```bash
aplay -l
```

会显示出如下的信息

```bash
**** PLAYBACK 硬體裝置清單 ****
card 0: PCH [HDA Intel PCH], device 0: ALC1150 Analog [ALC1150 Analog]
  子设备: 1/1
  子设备 #0: subdevice #0
card 1: NVidia [HDA NVidia], device 3: HDMI 0 [HDMI 0]
  子设备: 1/1
  子设备 #0: subdevice #0
card 1: NVidia [HDA NVidia], device 7: HDMI 1 [HDMI 1]
  子设备: 1/1
  子设备 #0: subdevice #0
card 1: NVidia [HDA NVidia], device 8: HDMI 2 [HDMI 2]
  子设备: 1/1
  子设备 #0: subdevice #0
card 1: NVidia [HDA NVidia], device 9: HDMI 3 [HDMI 3]
  子设备: 1/1
  子设备 #0: subdevice #0
```

找到PCH [HDA Intel PCH] 和 ALC283 Analog这两个参数，他们对应的card和device就是我们需要制定的输出设备。

创建一个文件

```bash
vi /etc/asound.conf
```

在里面加入

```bash
defaults.pcm.card 0
defaults.pcm.device 0
```

保存，重启系统即可

## 强制关闭

通过快键强制关闭 Ubuntu 上无响应的程序

系统-> 属性-> 键盘快捷键 中添加一个自定义快捷键
名称：Force Quit
命令：xkill
点击相应的行，设置键盘快捷键ctrl + shift + x（不重复就行，用完删了最好）
按下设置的快捷键，将变成“X”的光标点击无响应的软件即可。