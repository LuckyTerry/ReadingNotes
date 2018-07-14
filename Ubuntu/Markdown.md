# Markdown

## Markdown是什么

## Markdown基本语法

## Markdown编辑器

1. Visual Studio Code (Recommend)

    官网下载最新版本

    `https://code.visualstudio.com/#alt-downloads`

    或，快速下载1.25.1版本

    `wget https://vscode.cdn.azure.cn/stable/1dfc5e557209371715f655691b1235b6b26a06be/code_1.25.1-1531323788_amd64.deb`

    安装

    `sudo dpkg -i code_*_amd64.deb`

    安装markdownlint插件

    `搜索markdownlint，安装即可`

2. Cmd Markdown

    官网下载最新版本

    `https://www.zybuluo.com/cmd/`

3. Remarkable

    官网下载最新版本

    `https://remarkableapp.github.io/linux/download.html`

    安装

    `sudo dpkg -i remark*`

    若出现依赖问题，解决依赖后再执行上面的命令

    `sudo apt install -f`

4. Moeditor

    Github下载最新版本

    `https://github.com/Moeditor/Moeditor/releases`

    安装p7zip

    `sudo apt install p7zip`

    解压

    `sudo 7z x *.7z -r -o/opt`

    更名

    `sudo mv Moeditor-* Moeditor`

    添加快捷方式

    ```bash
    sudo vim /usr/share/applications/moeditor.desktop

    将下面的内容粘贴到 moeditor.desktop 文件中：
    [Desktop Entry]
    Encoding=UTF-8
    Name=Moeditor
    Comment=Moeditor Markdown
    Exec=/opt/Moeditor/Moeditor
    Icon=/opt/Moeditor/Moeditor.png
    Terminal=false
    StartupNotify=true
    Type=Application
    Categories=Application;Development;

    添加执行权限
    sudo chmod +x /usr/share/applications/moeditor.desktop

    添加Launcher快捷方式，桌面快捷方式
    sudo nautilus /usr/share/applications 拖动快捷方式到Launcher或桌面
    或 轻触Super键盘打开Dash，拖动快捷方式到Launcher或桌面
    ```