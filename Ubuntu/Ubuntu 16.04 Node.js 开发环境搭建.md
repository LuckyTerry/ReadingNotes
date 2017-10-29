# Ubuntu 16.04 Node.js 开发环境搭建

标签（空格分隔）： 未分类

---

## 安装nodejs

### apt安装1

更新ubuntu软件源

    sudo apt-get update
    sudo apt-get install -y python-software-properties software-properties-common
    sudo add-apt-repository ppa:chris-lea/node.js
    sudo apt-get update
    
安装nodejs

    sudo apt-get install nodejs
    sudo apt install nodejs-legacy
    sudo apt install npm

更新npm的包镜像源，方便快速下载

    sudo npm config set registry https://registry.npm.taobao.org
    sudo npm config list
    
全局安装n管理器(用于管理nodejs版本)

    sudo npm install n -g
    
安装最新的nodejs（stable版本）

    sudo n stable
    sudo node -v
    
### apt安装2

    curl -sL https://deb.nodesource.com/setup_5.x | sudo -E bash -
    sudo apt-get install -y nodejs
    node -v

### deb安装

[官网][1]下载最新LTS版本Nodejs，另附[官方仓库][2]和[镜像仓库][3]

解压

    sudo xz -d node*.tar.xz
    sudo tar -xvf  node*.tar -C /opt
    
更名

    sudo mv /opt/node* node
    
安装 npm 和 node 命令到系统命令

    sudo ln -s /opt/node/bin/node /usr/local/bin/node 
    sudo ln -s /opt/node/bin/npm /usr/local/bin/npm
    
验证： 

    node -v
    npm -v

设置 npm 使用淘宝源

    在 ~/.bashrc 中添加（请先备份 cp ~/.bashrc ~/.bashrc.bak）
    alias cnpm="npm --registry=https://registry.npm.taobao.org \
    --cache=$HOME/.npm/.cache/cnpm \
    --disturl=https://npm.taobao.org/dist \
    --userconfig=$HOME/.cnpmrc"
    
    使修改立即生效，输入，回车 
    source ~/.bashrc
    
使用淘宝镜像安装 npm 包 [name]

    cnpm install [name]
    
### 配置环境变量（未拷贝执行文件到/usr/local/bin时才需要配置）

    sudo vim /etc/profile

    在末尾添加以下三行：
    export NODE_HOME=/opt/node
    export PATH=$PATH:$NODE_HOME/bin
    export NODE_PATH=$NODE_HOME/lib/node_modules
    
    使在当前终端立刻生效
    source /etc/profile
    
    验证
    node -v
    npm -v
    
    使root账户生效
    vim /root/.bashrc
    在文件末尾添加
    source etc/profile
    
    
### 附录A - npm命令

全局安装

    npm install -g 软件包名
    
全局安装的路径可以通过下面的命令查看

    npm config get prefix

全局安装的路径可以通过下面的命令修改

    npm config set prefix "目录"

局部安装（将模块下载到当前命令行所在目录），不推荐

    npm install 软件包名
    
### 附录B - npm源

[淘宝镜像及使用说明][4]

### QA

#### Q: apt-get update找不到文件

    // error code
    W: The repository 'http://ppa.launchpad.net/chris-lea/node.js/ubuntu xenial Release' does not have a Release file.
    N: Data from such a repository can't be authenticated and is therefore potentially dangerous to use.
    N: See apt-secure(8) manpage for repository creation and user configuration details.
    E: Failed to fetch http://ppa.launchpad.net/chris-lea/node.js/ubuntu/dists/xenial/main/binary-amd64/Packages  404  Not Found
    E: Some index files failed to download. They have been ignored, or old ones used instead.
    
    // A: 修改node相关文件：/etc/apt/sources.list.d/chris-lea-ubuntu-node_js-xenial.list
    deb http://ppa.launchpad.net/chris-lea/node.js/ubuntu trusty main
    deb-src http://ppa.launchpad.net/chris-lea/node.js/ubuntu trusty main

#### Q: node -v异常

    node -v
    -bash: /usr/local/bin/node: Permission denied
    
    sudo node -v
    Segmentation fault (core dumped)
    
    // A: 重新安装nodejs
    手动移除 /usr/local/lib/node_modules整个目录
    手动移除 /usr/local/bin/node
    手动移除 /usr/local/bin/n
    手动移除 /usr/local/bin/npm

## 安装 WebStorm 

### 安装

详见[传送门][5]

### 配置 Node interpreter

    File->setting  输入 node.js 设置 Node interpreter 路径

### HelloWorld

新建项目HelloWorld

    File->New Project->Node.js ExpressApp
    
安装相关环境

    右键package.json->run "npm install"

启动服务

    法1： 使用 IDE 环境的设置（已尝试成功）
    Edit Configurations
    点击"+"新增"Node.js"
    working directory:~/WebstormProjects/HelloWorld/bin
    JavaScriptFile:www
    点击 apply ---> ok
    选中"www"，运行
    在浏览器中输入http://localhost:3000/"
    
    法2： 使用 IDE 环境的终端输入命令启动（已尝试成功）
    打开 Terminal ---> npm start ，回车即可，在浏览器打开  localhost：3000
    
    法3： 使用 系统 环境的终端输入命令启动（已尝试成功）
    Ctrl+Alt+T 打开 Terminal---> npm start ，回车即可，在浏览器中打开  localhost：3000
    
调试

    待补充

## 安装 Visual Studio Code

  [1]: https://nodejs.org/en/
  [2]: https://nodejs.org/dist/
  [3]: https://npm.taobao.org/mirrors/node
  [4]: https://npm.taobao.org/
  [5]: https://github.com/LuckyTerry/ReadingNotes/blob/master/Ubuntu/Ubuntu%2016.04%20WebStorm%20%E5%BC%80%E5%8F%91%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.md