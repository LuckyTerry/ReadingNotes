# 文本编辑器

## VsCode

推荐
    
## Calibre

进入[下载页面](https://calibre-ebook.com/download_linux) 

运行安装脚本

```
sudo -v && wget -nv -O- https://download.calibre-ebook.com/linux-installer.py | sudo python -c "import sys; main=lambda:sys.stderr.write('Download failed\n'); exec(sys.stdin.read()); main()"
```

运行Calibre：

```
calibre
```

## Sublime Text

### apt安装

添加GPG key

    wget -qO - https://download.sublimetext.com/sublimehq-pub.gpg | sudo apt-key add -
    
Ensure apt is set up to work with https sources:

    sudo apt-get install apt-transport-https
    
Select the channel to use

    Stable
    echo "deb https://download.sublimetext.com/ apt/stable/" | sudo tee /etc/apt/sources.list.d/sublime-text.list
    
    Dev
    echo "deb https://download.sublimetext.com/ apt/dev/" | sudo tee /etc/apt/sources.list.d/sublime-text.list
    
Update apt sources

    sudo apt-get update

install Sublime Text
    
    sudo apt-get install sublime-text
    
enter the license（sublime_text_3_build_3143_x64_注册码，亲测有效）

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

### deb安装

更新源

    sudo apt-get update
    
依赖

    sudo apt-get install -f
    
安装

    sudo dpkg -i *.deb