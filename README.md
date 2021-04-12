# 基于Vue框架的前端项目开发

# 安装Vue脚手架

## 安装nvm 管理不同版本node

1. [下载 nvm](https://github.com/coreybutler/nvm-windows/releases)

   下载第三个包：nvm-setup.zip

2. 在C盘解压nvm-setup.zip并运行 nvm-setup.exe

3. （可选）修改源

   修改C:\Users\Dell\AppData\Roaming\nvm\setting.txt

   ```
   root: C:\Users\Dell\AppData\Roaming\nvm
   path: C:\Program Files\nodejs
   arch: 64 
   proxy: none
   node_mirror: http://npm.taobao.org/mirrors/node/
   npm_mirror: https://npm.taobao.org/mirrors/npm/
   ```

   修改后翻墙无用，谨慎！

4. nvm 下载node 14.16.1版本 

   ```
   nvm install [node 版本号] [arch 操作系统位]
   ```

   大概10分钟，安装过程中一定不能终止，否则无法正常使用；

   出现以下信息则下载完毕，注意必须node.js 和npm都成功

   ![](E:\web_development\notes\imgs\nvm安装node.png)

5. 运行某个版本的node

   ```
   nvm use [node 版本号]
   node -v
   npm -v
   ```

   ![](E:\web_development\notes\imgs\npm.png)

   

   **一定先use**, 否则 直接 node 会提示 node: command not found

## 全局安装路径

C:\Node.js\node_global

## npm安装脚手架

1. 安装最新版本脚手架

```
npm install -g @vue/cli 
```

2. 把全局路径添加到系统变量的path下

![](E:\web_development\notes\imgs\vue_cli4.5.png)

3. **一定一定重启电脑**，否则依旧报错 **vue:command not found**



## Git 版本管理开发

- 创建repository

- clone 到本地 D:\GitHub\下

  git clone https://github.com/eva-yx-wang/coalScheme.git

- 分支管理

  一般不在master主干上开发，而是在dev分支上开发