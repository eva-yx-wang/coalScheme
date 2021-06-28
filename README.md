

# 基于Vue和SSM的配煤管理系统（全栈）开发过程说明书

（说明书部分持续更新中，尽情期待~~）

# 1 开发环境

## 1.1 安装Vue脚手架

### 1.1.1 安装nvm 管理不同版本node

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

Node.js的全局安装路径

C:\Node.js\node_global

### 1.1.2 npm安装脚手架

1. 安装最新版本脚手架

```
npm install -g @vue/cli 
```

2. 把全局路径添加到系统变量的path下

![](E:\web_development\notes\imgs\vue_cli4.5.png)

3. **一定一定重启电脑**，否则依旧报错 **vue:command not found**

## 1.2 maven工程管理SSM

### 1.2.1 安装 and 更换JDK

因为本项目需要用到外部32位dll开发，因此需要安装32位JDK，而JDK（Java Development Kit ）包含JRE（Java Runtime Environment）, JVM(解释器，即解释.class文件的虚拟机)等。

尽管网上有关于[修改本地JDK版本的方法](https://cloud.tencent.com/developer/article/1370960)，但其实**在IDEA 中修改JDK不用这么麻烦，只需两步**

1. File>Settings>Build>Complier>Java Complier>

   ![](D:\GitHub\coalScheme\imgs\修改JDK1.png)

   选择JDK版本

2. Edit>Configuration

   ![](D:\GitHub\coalScheme\imgs\修改JDK2.png)

   选择安装的JDK路径即可

### 1.2.2 创建Maven工程项目

因为项目需用到SSM框架，为了方便，父模块coal_scheme_back只导入坐标<groupId>org.springframework.boot</groupId>, 子模块包括服务器server和实体类代码生成器generator



## 1.3 Git 版本管理开发

### 1.3.1 创建分支

1. 在github创建repository

2. clone 到本地 D:\GitHub\下

git clone https://github.com/eva-yx-wang/coalScheme.git

3. 分支管理

一般不在master主干上开发，而是在dev分支上开发

如果repository内没有任何文件，则必须先add & commit 一个文件

```
git add README.md
git commit -m "commit a README.md" README.md
```

然后再创建分支dev-1.0

![](E:\web_development\notes\imgs\git_branch_dev-1.0.png)

==设置push到的分支位置，之后直接git push xxx 就可以了==

```
git push --set-upstream origin dev-1.0  //设置push origin的分支为dev-1.0
```

创建本地功能分支并推到远程

分支结构

- dev-1.0

  - feature-coalScheme-v1.0-20210412

    分支类型-项目名-版本名-创建分支时间


```
git checkout -b feature-coalScheme-v1.0-20210412  //创建本地分支并切入该分支
git push --set-upstream origin feature-coalScheme-v1.0-20210412  //设置push origin的分支为dev-1.0
git branch -a  //查看本地和远程所有分支
```

![](E:\web_development\notes\imgs\本地分支管理.png)

### 1.3.2 上传到分支

1. 查看working area(工作区), stage(暂存区) 状态

```c++
git status
```

显示：Untracked files—-存在文件夹中但未被git追踪的文件，即no committed files

只有含有文件的文件夹才会标红，commit可提交整个文件夹

# 2 需求分析

## 2.1 系统功能需求

1. 系统访问安全

2. 煤仓数据增删改查

3. 混煤数据计算

4. 混煤标准检查

5. 配煤方案初始化和优化

5. 方案分析对比

## 2.2 按功能划分模块

1. 用户登录和权限管理<——（系统）
2. 煤仓数据管理<——（数据增删改查）
3. 煤质分析和配比选择<——（混煤数据计算）
4. 标准输入和自动检查<——（混煤标准检查）
5. 通过方案初始化和未通过方案优化<——（配煤方案初始化和优化）
6. 可视化扇形比例图和折线图<——（方案分析对比）

# 3 数据库的设计与实现

数据库包含5张表，使用Navicat并转存成coalScheme_db_null.sql文件，表关系如图：

![](D:\GitHub\coalScheme\imgs\coalScheme_db structure_map.jpg)

表设计原则参考[《阿里巴巴Java开发手册》](https://kangroo.gitee.io/ajcg/#/)

# 3 前端工程——基于Vue

版本：Vue 3.x

脚手架版本：Vue CLI4.5

系统分层架构：

![](D:\GitHub\coalScheme\imgs\前端分层架构.png)

## 3.1 创建项目并初始化

1. 使用VScode, file>add to workspace> 刚才git clone下来的repository 文件夹>下方terminal中输出指令检查node和vue是否正常安装

   ![](D:\GitHub\coalScheme\imgs\检查node和vue是否成功安装.jpg)

2. 输入创建指令并逐项配置

   此步可在可视化网页中完成，本文展示终端创建方式

   ```
   vue create coal-scheme  //项目名coal-scheme, 不能有大写
   ```

   ![](D:\GitHub\coalScheme\imgs\vue_proj_conf7_保存配置并命名.png)

   可以看到项目的本地访问和外网访问ip

   ![](D:\GitHub\coalScheme\imgs\vue_proj_conf8_启动项目.png)

### 项目结构

- src (source code)

  - node_modules  ——[依赖包]

  - assets 静态资源

    图片，图标等

  - component 公共组件

  - router 路由 (用于页面跳转)

  - main.js 入口函数文件

  - package.json

    记录项目运行**局部 package command的指令转换**的文件

    ```
    devDependencies //运行时依赖
    dependencies  //开发时依赖
    ```

  - package-lock.json：版本管理使用的文件



## 3.x 前端工程中报错汇总

1. terminal中运行vue，显示未签名报错

   - 查看执行脚本方式

     ```
     get-ExecutionPolicy
     ```

   - 修改执行脚本方式

     ```
     set-ExecutionPolicy RemoteSigned
     ```

     ![](D:\GitHub\coalScheme\imgs\vscode执行vue的未签名报错.png)

   