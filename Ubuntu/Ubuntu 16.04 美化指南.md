# Ubuntu 16.04 美化指南

## 主题设置
软件中心安装Unity Tweak Tool，进入Theme，一般我个人将主题换为Radiance，图标换为Ubuntu-mono-light。
更多Tweak Tool美化方式参看[此处][8]
美化Bash界面和参数参考[此处][9]

### Mac主题

切换到root用户：

    sudo su
    
更新源：

    apt-get update
    
下载一些必要的工具：

    # 下载工具
    apt-get install wget

    # 抓取工具
    apt-get install curl

    # 编辑器之神
    apt-get install vim
    
下载mac壁纸：

    http://pan.baidu.com/s/1skQCq2T
    
添加源：

    # 添加源
    add-apt-repository ppa:noobslab/macbuntu

    # 更新源
    apt-get update

下载图标和主题：

    # 下载图标
    apt-get install macbuntu-os-icons-lts-v7

    # 下载主题
    apt-get install macbuntu-os-ithemes-lts-v7

    # 卸载命令
    cd /usr/share/icons/mac-cursors && sudo ./uninstall-mac-cursors.sh
    apt-get remove macbuntu-os-icons-lts-v7 macbuntu-os-ithemes-lts-v7

安装 Slingscold：

    apt-get install slingscold
    
安装Albert Spotlight：

    apt-get install albert
    
安装 Plank Dock：

    # 安装plank
    apt-get install plank

    # 安装plank主题
    apt-get install macbuntu-os-plank-theme-lts-v7
    
替换面板上的Ubuntu Desk：

    cd && wget -O Mac.po http://drive.noobslab.com/data/Mac/change-name-on-panel/mac.po
cd /usr/share/locale/en/LC_MESSAGES
msgfmt -o unity.mo ~/Mac.po
rm ~/Mac.po
cd

    #还原默认
    cd && wget -O Ubuntu.po http://drive.noobslab.com/data/Mac/change-name-on-panel/ubuntu.po
    cd /usr/share/locale/en/LC_MESSAGES
    msgfmt -o unity.mo ~/Ubuntu.po
    rm ~/Ubuntu.po
    cd
    
修改启动器的logo：

    wget -O launcher_bfb.png http://drive.noobslab.com/data/Mac/launcher-logo/apple/launcher_bfb.png
    mv launcher_bfb.png /usr/share/unity/icons/

    # 恢复默认
    wget -O launcher_bfb.png http://drive.noobslab.com/data/Mac/launcher-logo/ubuntu/launcher_bfb.png
    mv launcher_bfb.png /usr/share/unity/icons/
    
安装修改工具：
    
    apt-get install unity-tweak-tool
    apt-get install gnome-tweak-tool
    
修改主题

    主题选择Macbuntu-os

    图标选择Macbuntu-os

    指针选择Mac-cursors
    
### 卸载主题

进入主题目录

    cd /usr/share/themes
 
查看主题文件夹

    ls -l
 
把不想要的主题文件夹删除即可

    rm -rf xx

### 卸载图标

进入图标目录

    cd /usr/share/icons
 
查看主题文件夹

    ls -l
 
把不想要的主题文件夹删除即可

    rm -rf xx

### 卸载指针

### 卸载字体

## 中文字体

### 文泉驿-微米黑

    sudo apt-get install ttf-wqy-microhei  

### 文泉驿-正黑

    sudo apt-get install ttf-wqy-zenhei

### 文泉驿-点阵宋体

    sudo apt-get install xfonts-wqy