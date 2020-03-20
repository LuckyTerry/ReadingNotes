# Manjaro主题篇

折腾一些好看的主题。

# 主题资源介绍

1. 全局主题

位于 ~/.local/share/plasma/desktoptheme

2. Plama样式

位于 ~/.local/share/plasma/look-and-feel/

3. 应用程序风格

位于 todo

4. 颜色

位于 todo

5. 图标

位于~/.local/share/icons/

6. 字体

位于 todo

7. 光标

位于 todo

# 我的主题

## 预览

- 全局主题：Adapta
- Plasma样式主题：Adapta
- 颜色：Adapta Nokto
- 图标：Papirus-Dark

## Adapta主题

### 系统设置安装

    系统设置-外观-图标-获取新图标主题-搜索adapta-安装（不慢，主题较小，包含了全局/Plasma/颜色）

## papirus图标主题

### Github地址

[PapirusDevelopmentTeam/papirus-icon-theme](https://github.com/PapirusDevelopmentTeam/papirus-icon-theme/)

### 系统设置安装

    系统设置-外观-图标-获取新图标主题-搜索papirus-安装最新日期的版本（较慢，耐心）

### ocs-url安装

    yay -S ocs-url
    kde-store 点击 install 即可触发下载
    
    要打开 xdg-open 吗？
    https://store.kde.org 想打开此应用。

                取消      打开 xdg-open

    选择打开即可开始下载。

### Papirus Installer

使用脚本直接从当前仓库安装最新版本（无关发行版）：

**NOTE**: Use the same script to update icon themes.

**HOME directory for KDE**
    
    wget -qO- https://git.io/papirus-icon-theme-install | DESTDIR="$HOME/.local/share/icons" sh

**Uninstall**

    wget -qO- https://git.io/papirus-icon-theme-uninstall | sh

### yay安装

    yay -S papirus-icon-theme
    yay -S papirus-icon-theme-git

### 下载压缩包，手动安装

    解压压缩包，拷贝图标文件夹到~/.local/share/icons/
    图标文件夹长这样：里面是 8x8，16x16这样子的子文件夹。 

安装 Adapta 全局主题
安装 Papirus-Icon-Theme-Vectorel-Folders 图标，40多M，下载

## 配置下

右键latte-dock，点击添加部件，选择：应用程序面板、、、

# 网友篇

## Mac篇