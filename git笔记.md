# Git笔记

[git分支详解（约10分钟掌握分支80%操作），git-branch，git分支管理，git分支操作，git分支基础和操作，2023年git基础使用教程_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV16M411z7uH/?spm_id_from=333.337.search-card.all.click&vd_source=665a7571caf896d1a1d52b41d248af9a)

### 注意

1.在终端中点击右键是粘贴

2.用git add .上传到中转站时，add和.之间要有空格

3.报错error: failed to push some [refs](https://so.csdn.net/so/search?q=refs&spm=1001.2101.3001.7020) to ‘https://gitee.com/

原因：新建了一个远程仓库，准备把本地内容上传时，忘记把远程仓库的redme.[md文件](https://so.csdn.net/so/search?q=md文件&spm=1001.2101.3001.7020)同步到本地出错

解决方法：git pull --rebase gitee/github  master(将redme.md文件同步到本地)，然后再次执行git push origin master就好了

git pull --rebase github  master

**解决方法**：

**git pull --rebase origin master**

**将redme.md文件同步到本地，然后再次执行****git push origin master就好了**

### 1.介绍

Git 是一个免费开源的**分布式版本控制**系统，可以快速高效地处理从小大型项目。

Git 易于学习性能极快。 它具有廉价的本地库，方便的暂存区域和多个工作流**分支**等特性。

就是**方便管理文件**，可以**个人**过渡到**团队**开发

#### git和github关系：

github：是一个面向开源及私有软件项目的代码托管平台。在github建立一个网上的仓库，每次提交别人都可以看到你的代码，非常方便程序员之间的交流学习(像github，gitee这些才有了远程仓库的概念，下面讲的git提到的仓库都是本地仓库，不涉及github托管平台)

```
拉取远程仓库的git代码 ：
git clone +githurb复制的地址
```

git：是一个开源的分布式版本控制系统，可以有效高速地处理项目版本管理。

### 2.Git常用指令

```
查用户名：git config user.name
查邮箱：git config user.email
```

```
初始化本地仓库 (会在对应文件夹下创建一个".git文件夹"，用来管理自己的代码，这个文件就是本地仓库，同一级的位置叫工作区)：
git init 
```

```
查看本地库状态：(红色文件代表检测到文件有修改有变化，绿色文件代表已经放到暂存区，已提交的文件不会显示)
git status
```

```
添加到暂存区(设置为准备提交状态)：
git add "文件名" 	 //只将对应文件名的文件放到暂存区
git add . 所有文件  //add和.之间一定要有一个空格
```

```
如果写的文件出现问题：
git checkout HEAD "文件123.java"  从远程仓库，把最近一次提交的"文件123.java"覆盖到工作区(.git文件夹同一级的位置)
```

```
提交到本地库，"xxx"是对这次提交的备注(如功能1已完成)：
git commit -m "xxx" 
```

```
查看历史记录:
git reflog  查看版本信息
git log 查看版本详细信息
```

```
版本穿梭：
git reset --hard 版本号
在提交前，将add到暂存区的对应文件撤销：
git reset "文件123.java" 
```

### 3.分支管理

#### 1.概念

> 在版本控制过程中，同时推进多个任务，为每个任务创建每个任务的单独分支。
>
> 从开发主线上分离开来，开发自己分支的时 候，不会影响主线分支的运行。

<img src="D:\桌面下载的文件\Git\git图片\Snipaste_2023-08-02_17-15-53.jpg" alt="Snipaste_2023-08-02_17-15-53"  />

#### 2.分支操作

```
创建分支 git branch xxx  ( 同时创建本地仓库和分支 git init -b xxx)
切换分支 git checkout xxx

组合：创建分支并切换 git checkout -b xxx

查看分支 git branch

分支重命名 git branch -m 原名字 新名字

分支合并 git merge 分支a (处在分支b中，执行git merge xxx,则分支a和分支b就进行了合并)

删除分支 git branch -d xxx(用于删除已经合并的分支，未合并的要-D强制删除)
```



#### 3.代码冲突

冲突产生的表现：后面状态为 **MERGING**

冲突产生的原因： 合并分支时，两个分支在**同一个文件的同一个位置**有两套完全不同的修改。Git 无法替 我们决定使用哪一个。必须**人为决定**新代码内容。

#### 4.解决冲突

```
<<<<<<< HEAD 当前分支的代码 **====** 合并过来的代码 **>>>>>>>** 分支名
```

修改后，git commit

### 4.GitHub操作

1.远程库操作

| 命令                             | 作用                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| git clone 远程地址               | 将远程仓库的内容克隆到本地                                   |
| git remote -v                    | 查看当前所有远程地址别名                                     |
| git remote add 别名 远程地址     | 给远程地址起别名，同时也是将本地仓库和远程仓库进行关联的语句 |
| git push 远程地址别名 本地分支名 | 推送本地分支上的内容到远程仓库                               |
| git pull 远程地址别名 本地分支名 | 将远程仓库对于分支最新内容拉下来后与 当前本地分支直接合并    |
| git remote remove 远程地址别名   | 将与本地仓库关联的远程地址删除                               |

### 5.团队内协作

![img](https://img-blog.csdnimg.cn/img_convert/6ff37d55340aefa55e826077c0192c46.png)

总结：先将修改后的代码添加到暂存区，然后提交到本地库。最后通过 git push 链接地址 master 命令push到远程库中

过程中有一个请求发送，只有是一个团队才能推送代码，我们需要将他拉入到团队中才能够成功push

操作如下

![img](https://img-blog.csdnimg.cn/3c53cd6baf154ee490fc37ac0176eb0c.png)

![img](https://img-blog.csdnimg.cn/7fe17be2260545218a1f977e34f68fa1.png) 

然后push方接受邀请就加入团队了

### 6.总结

先将修改后的代码添加到暂存区，然后提交到本地库。最后通过 git push 链接地址 master 命令push到远程库中

假设在分支a上，add并commit -m到本地仓库分支a 了某个文件demo，之后再创建一个新分支b，此时在分支a 修改demo文件add并commit -m到本地仓库，切换分支 git checkout b，会发现b分支仍保存的是没被更改过的demo文件，因为在创建分支b的时候已经有了两条时间线了

### <img src="D:\桌面下载的文件\Git\git图片\Snipaste_2023-08-02_19-09-34.jpg" alt="Snipaste_2023-08-02_19-09-34" style="zoom:50%;" />

### 7.用idea将项目更新到github上

**1.通过File-settings打开设置，配置刚才已经安装好的git.exe，点击Test测试连接是否成功。**

如图所示：

<img src="https://img-blog.csdnimg.cn/ca84ecca8d3840a8bc7cc5eec92cf064.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hhbmdmZW5neGlh,size_20,color_FFFFFF,t_70,g_se,x_16" alt="img" style="zoom: 80%;" />

**2.从远程库拉取项目：点击File-New-Project from Version Control弹出如下窗口，输入远程库地址以及空的本地目录点击Clone按钮**

<img src="https://img-blog.csdnimg.cn/6d5cec82265e45d18ccd7cf3452cdc22.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hhbmdmZW5neGlh,size_20,color_FFFFFF,t_70,g_se,x_16" style="zoom:80%;" />

<img src="https://img-blog.csdnimg.cn/283722837e804c868c1e008031758196.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hhbmdmZW5neGlh,size_20,color_FFFFFF,t_70,g_se,x_16" alt="img" style="zoom:67%;" />

Github还要通过token进行与idea的绑定登录

<img src="https://img-blog.csdnimg.cn/6f461ba16ce149578f79e14ae11aae3a.png" alt="在这里插入图片描述" style="zoom:50%;" />

**3.切换到开发分支（develop）**
鼠标移上菜单栏的Git，点击Branches，弹框如图，

远程分支同步到本地：假设本地只有一个master主分支，远程分支已经有master主分支、develop开发分支，点击远程develop-checkout即可在本地创建并切换到一个develop分支并将远程develop分支内容更新到本地develop分支。

本地分支同步到远程：远程库没有develop分支，可以点击弹窗中的New Branch新建一个本地develop分支，通过push推送到远端

注：Local Branches为本地分支，Remote Branches为远程分支。

![img](https://img-blog.csdnimg.cn/7952367d8cd64ffabf843c6407da5118.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hhbmdmZW5neGlh,size_20,color_FFFFFF,t_70,g_se,x_16)

 <img src="https://img-blog.csdnimg.cn/991b2f6e08a043c1bd1ebd8e2dfe859e.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hhbmdmZW5neGlh,size_11,color_FFFFFF,t_70,g_se,x_16" alt="img" style="zoom: 80%;" /><img src="https://img-blog.csdnimg.cn/6b288eb84f184ee5a27c832eb14d9b70.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hhbmdmZW5neGlh,size_20,color_FFFFFF,t_70,g_se,x_16" alt="img" style="zoom: 67%;" />

 **4.协同合作常用流程：**

先更新(Update) 再提交(Commit) 再推送(Push)
本地写完代码想要推送到远程，需要先（Update Projet）将最新远程代码同步到本地，再Commit自己代码部分（没有add的话需要先Git add）输入提交备注（Commit）提交后（Push）推送到远程库，或直接点击（Commit And Push）。

![img](https://img-blog.csdnimg.cn/fcf10e84ce07408b8e64f048be9cc5b8.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hhbmdmZW5neGlh,size_6,color_FFFFFF,t_70,g_se,x_16)

或者：

![img](https://img-blog.csdnimg.cn/44817312689c4fbf81c36c33500c98a6.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAc2hhbmdmZW5neGlh,size_14,color_FFFFFF,t_70,g_se,x_16)

 

### 其他指令

看本地代码状态（git status）
暂存本地代码（git stash save ‘name_date’）
查看暂存区列表（git stash list）
恢复暂存区代码（git stash pop stash@{index}）
删除暂存区代码（git stash drop stash@{index}）
删除全部（git stash clear）

Tags
查看tag（git tag）
在某个commit上打tag（git tag test_tag c809ddbf83939a89659e51dc2a5fe183af384233）
本地tag推送到线上（git push origin test_tag）
本地删除tag（git tag -d test_tag）
本地tag删除了，再执行该句，删除线上tag（git push origin :refs/tags/test_tag）

