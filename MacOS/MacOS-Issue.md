# Issue

> Dock栏的自带应用中文乱码

```bash
defaults delete com.apple.dock; killall Dock
```

> 连接 raw.githubusercontent.com 失败：Connection refused

需要走代理，见下文

> 终端走代理

最好不要设置全局的终端代理，可能会影响别的应用，比如 brew；

建议需要时设置，用完了还原或关掉当前终端。

```bash
# 设置代理（socks不行，原因未知；故这里用http/https，亲测可用）
export http_proxy=http://127.0.0.1:1087 
export https_proxy=https://127.0.0.1:1087
# 取消设置代理
unset http_proxy
unset https_proxy
```

> GIT走代理

```bash
# 设置代理（socks不行，原因未知；故这里用http/https，亲测可用）
git config --global http.proxy http://127.0.0.1:1087
git config --global https.proxy https://127.0.0.1:1087
# 查看代理
git config --get http.proxy
git config --get https.proxy
# 取消设置代理
git config --global --unset http.proxy
git config --global --unset https.proxy
```

> 终端和Git代理一键启动/关闭脚本 by terry
 
```bash
function proxy_on() {
        export http_proxy="http://127.0.0.1:1087"
        export https_proxy="https://127.0.0.1:1087"
        echo -e "已开启终端代理："
        echo -e $http_proxy
        echo -e $https_proxy
}

function proxy_cat() {
        echo -e "终端代理如下："
        if [ $http_proxy ]; then
            echo -e $http_proxy
        fi
        if [ $https_proxy ]; then
            echo -e $https_proxy
        fi
}

function proxy_off() {
        unset http_proxy
        unset https_proxy
        echo -e "已关闭终端代理"
}

function proxy_git_on() {
        git config --global http.proxy http://127.0.0.1:1087
        git config --global https.proxy https://127.0.0.1:1087
        echo -e "已开启Git代理："
        git config --get http.proxy
        git config --get https.proxy
}

function proxy_git_cat() {
    echo -e "Git代理如下："
    git config --get http.proxy
    git config --get https.proxy
}

function proxy_git_off() {
        git config --global --unset http.proxy
        git config --global --unset https.proxy
        echo -e "已关闭Git代理"
}
```

> proxychains-ng

```bash
# 安装
brew install proxychains-ng
# 添加代理地址和端口
vim /usr/local/etc/proxychains.conf
socks5 127.0.0.1 1080
# 添加别名
vim ~/.zshrc
alias gfw='proxychains4'
source ~/.zshrc
# 测试代理
curl cip.cc
gfw curl cip.cc
```

> macOS 下 Oh My Zsh 的配置及美化

https://zhuanlan.zhihu.com/p/129287351

安装

```bash
sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"
```

自动补全

```bash
git clone https://github.com/zsh-users/zsh-autosuggestions ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-autosuggestions
```

语法高亮

```bash
git clone https://github.com/zsh-users/zsh-syntax-highlighting.git ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-syntax-highlighting
echo "source {ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-syntax-highlighting/zsh-syntax-highlighting.zsh" >> ${ZDOTDIR:-$HOME}/.zshrc
```

最后 

```bash
source ~/.zshrc

打开一个新的终端查看是否生效
```