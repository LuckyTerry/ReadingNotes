# JRebel使用指南

## 下载

IDEA中Plugins下载即可。

或者，[JRebel Plugins/Jetbrains](https://plugins.jetbrains.com/plugin/?id=4441)下载后手动安装。

## 激活

[JRebel激活URL](http://jrebel.autoseasy.cn/jrebelServer/db293adf-2076-4917-bdd6-e32271419591)

## 手动热部署（推荐）

选中Project窗口中指定的项目，按Ctrl+Shift+F9（或者右键Rebuild...），重新编译当前项目

选中Project窗口中指定的包名，按Ctrl+Shift+F9（或者右键Rebuild...），重新编译当前包名

选中Project窗口中指定的文件，按Ctrl+Shift+F9（或者右键Rebuild...），重新编译当前文件

当前光标在某个文件中，按Ctrl+Shift+F9（或者右键Rebuild...），重新编译当前文件

右键文件Tab，或者右键文件内容区域，选择Recompile，重新编译当前文件

右键Toolbar任一个包名，选择Rebuild...，重新编当前包名下的所有文件

注：修改配置文件，同样能生效。具体操作，不是重编译配置文件，而是重编译使用配置文件的文件。

## 自动热部署（不推荐）

略

## Jrebel Panel（无需使用）

用于远程热部署，不需要使用。

推荐关闭：JRebel -> Remote Servers -> Synchronize onbuild