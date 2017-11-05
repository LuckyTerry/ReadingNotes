# Ubuntu Android Studio 练级指南

[TOC]

---

## 第一次启动的时候

    不要设置代理

## 无法安装软件，无法调试

    DELETE_FAILED_INTERNAL_ERROR 
    Error while Installing APKs

解决方案一：

未关闭android studio上的instant app所致，可见[StackOverFlow][1]。故
File->settings->Buil,Execution,Deployment->Instant Run->Disable it.

解决方案二：


## 打开项目提示inotify过低

详见[官方解决方案][2]

打开sysctl.conf

    sudo vim /etc/sysctl.conf
    
在末尾添加

    fs.inotify.max_user_watches = 524288
    
使生效
    
    sudo sysctl -p --system
    
## 乱码

更换字体

    File->setting->Appearance & Behavior->Appearance
    ->Override default fonts by->WenQuanYi Micro Hei
    
## 无法执行git操作，详见[出处][3]

    SSH executable -> Native
    
## 已安装搜狗输入法的前提下无法切换到搜狗输入法

    sudo vim /opt/android-studio/bin/studio.sh
    
    在首部添加
    export XMODIFIERS=@im=fcitx
    export QT_IM_MODULE=fcitx

## Proxy无效时

AS-setting中关闭SS代理，不使用代理。（怎么会这样呢？）

## 无法创建AVD

64位Ubuntu需要安装32位库

    sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386


  [1]: https://stackoverflow.com/questions/38892270/delete-failed-internal-error-error-while-installing-apk/43063569
  [2]: https://confluence.jetbrains.com/display/IDEADEV/Inotify+Watches+Limit
  [3]: http://blog.csdn.net/u011771755/article/details/47167617