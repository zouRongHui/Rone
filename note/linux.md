
### 基础
#### 系统目录结构
[参考](https://www.runoob.com/linux/linux-system-contents.html)
* /bin：bin是Binary的缩写, 这个目录存放着最经常使用的命令。
* /boot：这里存放的是启动Linux时使用的一些核心文件，包括一些连接文件以及镜像文件。
* /dev ：dev是Device(设备)的缩写, 该目录下存放的是Linux的外部设备，在Linux中访问设备的方式和访问文件的方式是相同的。
* /etc：这个目录用来存放所有的系统管理所需要的配置文件和子目录。
* /home：用户的主目录，在Linux中，每个用户都有一个自己的目录，一般该目录名是以用户的账号命名的。
* /lib：这个目录里存放着系统最基本的动态连接共享库，其作用类似于Windows里的DLL文件。几乎所有的应用程序都需要用到这些共享库。
* /lost+found：这个目录一般情况下是空的，当系统非法关机后，这里就存放了一些文件。
* /media：linux系统会自动识别一些设备，例如U盘、光驱等等，当识别后，linux会把识别的设备挂载到这个目录下。
* /mnt：系统提供该目录是为了让用户临时挂载别的文件系统的，我们可以将光驱挂载在/mnt/上，然后进入该目录就可以查看光驱里的内容了。
* /opt： 这是给主机额外安装软件所摆放的目录。比如你安装一个ORACLE数据库则就可以放到这个目录下。默认是空的。
* /proc：这个目录是一个虚拟的目录，它是系统内存的映射，我们可以通过直接访问这个目录来获取系统信息。
  这个目录的内容不在硬盘上而是在内存里，我们也可以直接修改里面的某些文件。
* /root：该目录为系统管理员，也称作超级权限者的用户主目录。
* /sbin：s就是Super User的意思，这里存放的是系统管理员使用的系统管理程序。
* /srv： 该目录存放一些服务启动之后需要提取的数据。
* /sys： 这是linux2.6内核的一个很大的变化。该目录下安装了2.6内核中新出现的一个文件系统 sysfs 。
* /tmp：这个目录是用来存放一些临时文件的。
* /usr：这是一个非常重要的目录，用户的很多应用程序和文件都放在这个目录下，类似于windows下的program files目录。
    * /usr/bin：系统用户使用的应用程序。
    * /usr/sbin：超级用户使用的比较高级的管理程序和系统守护程序。
    * /usr/src：内核源代码默认的放置目录。
* /var：这个目录中存放着在不断扩充着的东西，我们习惯将那些经常被修改的目录放在这个目录下。包括各种日志文件。
* /run：是一个临时文件系统，存储系统启动以来的信息。当系统重启时，这个目录下的文件应该被删掉或清除。如果你的系统上有 /var/run 目录，应该让它指向 run。
* /selinux： 这个目录是Redhat/CentOS所特有的目录，Selinux是一个安全机制，类似于windows的防火墙，但是这套机制比较复杂，这个目录就是存放selinux相关的文件的。


#### 给用户添加sudo权限
* 切换到root用户下
* 添加sudo文件的写权限,命令是:
  `chmod u+w /etc/sudoers`
* 编辑sudoers文件
  ```
  vi /etc/sudoers
  找到这行 root ALL=(ALL) ALL,在他下面添加xxx ALL=(ALL) ALL (这里的xxx是你的用户名)
  ps:这里说下你可以sudoers添加下面四行中任意一条
  youuser            ALL=(ALL)                ALL				:允许用户youuser执行sudo命令(需要输入密码).
  %youuser           ALL=(ALL)                ALL				:允许用户组youuser里面的用户执行sudo命令(需要输入密码).
  youuser            ALL=(ALL)                NOPASSWD: ALL	:允许用户youuser执行sudo命令,并且在执行的时候不输入密码.
  %youuser           ALL=(ALL)                NOPASSWD: ALL	:允许用户组youuser里面的用户执行sudo命令,并且在执行的时候不输入密码.
  ```
* 撤销sudoers文件写权限,命令:
  `chmod u-w /etc/sudoers`
* tips:增加一个用户组，给该用户组授权使用sudo命令，再将目标用户添加到该用户组中


#### firewall 防火墙
* 查看防火墙状态 `systemctl status firewalld`
* 开启防火墙 `systemctl start firewalld`
* 关闭防火墙 `systemctl stop firewalld`
* 开放端口 `firewall-cmd --zone=public --add-port=22/tcp --permanent    示例为开放22端口`
* 重载设置 `firewall-cmd --reload`
* 查看已开放的端口 `firewall-cmd --zone=public --list-ports`
* 关闭端口 `firewall-cmd --zone=public --remove-port=22/tcp --permanent 示例为关闭22端口`


************************************************************************************************************************
### Shell
[参考文档](https://www.runoob.com/linux/linux-shell.html)
#### 基础语法
* 变量定义：变量名 = 变量值
* 使用变量：$变量名、${变量名}
* 删除变量：unset 变量名
* 条件判断：if [[条件1]]; then 执行语句; elif [[条件]]; then 执行语句; else 执行语句; fi
* 字符串运算符，假设 a="abc" b="efg"
  * ==	检测两个字符串是否相等，相等返回 true   [ $a=$b ] 返回 false
  * !=	检测两个字符串是否相等，不相等返回 true  [ $a!=$b ] 返回 true
  * -z	检测字符串长度是否为0，为0返回 true   [ -z $a ] 返回 false
  * -n	检测字符串长度是否为0，不为0返回 true  [ -n "$a" ] 返回 true
  * $	检测字符串是否为空，不为空返回 true    [ $a ] 返回 true
  * =~  检测字符串是否包含，包含返回 true [ $a =~ $b ] 返回 false


#### solutions
##### 判断文件/文件夹是否存在

| 操作符     | 说明                                       | 举例                     |
|---------|------------------------------------------|------------------------|
| -b file | 检测文件是否是块设备文件，如果是，则返回 true。	              | [ -b $file ] 返回 false。 |
| -c file | 检测文件是否是字符设备文件，如果是，则返回 true。	             | [ -c $file ] 返回 false。 |
| -d file | 检测文件是否是目录，如果是，则返回 true。	                 | [ -d $file ] 返回 false。 |
| -f file | 检测文件是否是普通文件（既不是目录，也不是设备文件），如果是，则返回 true。 | [ -f $file ] 返回 true。  |
| -g file | 检测文件是否设置了 SGID 位，如果是，则返回 true。	          | [ -g $file ] 返回 false。 |
| -k file | 检测文件是否设置了粘着位(Sticky Bit)，如果是，则返回 true。	  | [ -k $file ] 返回 false。 |
| -p file | 检测文件是否是有名管道，如果是，则返回 true。	               | [ -p $file ] 返回 false。 |
| -u file | 检测文件是否设置了 SUID 位，如果是，则返回 true。	          | [ -u $file ] 返回 false。 |
| -r file | 检测文件是否可读，如果是，则返回 true。	                  | [ -r $file ] 返回 true。  |
| -w file | 检测文件是否可写，如果是，则返回 true。	                  | [ -w $file ] 返回 true。  |
| -x file | 检测文件是否可执行，如果是，则返回 true。	                 | [ -x $file ] 返回 true。  |
| -s file | 检测文件是否为空（文件大小是否大于0），不为空返回 true。	         | [ -s $file ] 返回 true。  |
| -e file | 检测文件（包括目录）是否存在，如果是，则返回 true。	            | [ -e $file ] 返回 true。  |
```shell
#如果文件夹不存在，则创建文件夹
tempPath="/home/parasaga/blank"
if [ ! -d "$tempPath" ]; then
	mkdir $blankPath
fi
#如果文件不存在，则创建文件
tempFile="/home/parasaga/blank/error.log"
if [ ! -f "$tempFile" ]; then
	touch $tempFile
fi
```


##### 判断当前的IP地址是否为目标ip
```shell
targetIp="127.0.0.1"
localIp=`ifconfig`
if [[ ${localIp} =~ ${targetIp} ]]
then
echo "当前服务器ip确实为：${targetIp}"
fi
```


************************************************************************************************************************
### vi文本编辑器
* 语法：vi 文件地址
    #在命令行中输入vim,进入vim编辑器
* 编辑模式，按 i 进入 esc 退出
* 常用的命令
* :w #在编辑的过程中保存文件,相当于word中的ctrl+s
* :wq #保存文件并退出
* :q! #强制退出,不保存
* shift+g #光标转跳到文本最后一行
* g+g #光标转跳到文档首行
* 查找
  * /目标字符串，从开头查起
  * ?目标字符串，从结尾处查起
  * n 下一个
  * N 上一个
* 逐字符移动：
  * h: 左
  * l: 右
  * j: 下
  * k: 上
* 翻屏
  * Ctrl+f: 向下翻一屏
  * Ctrl+b: 向上翻一屏
  * Ctrl+d: 向下翻半屏
  * Ctrl+u: 向上翻半屏
* :set number(set nu) #使编辑中的文件显示行号
* 删除内容
  * “.”表示当前行
  * “$”表示最后一行
  * “d”表示删除
  * “1,.d”表示删除第1行到当前行
  * “1,$d”表示删除第1行到最后一行，也就是所有内容
* 移动光标
  * 按数字「0」：移到文章的开头。
  * 按「G」：移动到文章的最后。
  * 按「$」：移动到光标所在行的"行尾"。
  * 按「^」：移动到光标所在行的"行首"
  * 按「w」：光标跳到下个字的开头
  * 按「e」：光标跳到下个字的字尾
  * 按「b」：光标回到上个字的开头
  * 按「#l」：光标移到该行的第#个位置，如：5l,56l。
* 查看并配置文件编码格式
  * 查看  :set fileencoding
  * 设置  :set fileencoding=utf-8


************************************************************************************************************************
### 命令
#### 用户
* 新建用户组: groupadd 组名
  `groupadd rone`
* 新建用户: useradd [-c comment] [-d home] [-g group] [-r] name
  * -c：加上备注文字，备注文字保存在passwd的备注栏中。
  * -d：指定用户登入时的主目录，替换系统默认值/home/<用户名>
  * -g：指定用户所属的群组。值可以使组名也可以是GID。用户组必须已经存在的，期默认值为100，即users。
  * -r：建立系统账号。
  `useradd rone`
* 设置密码: passwd -username
  `passwd rone`
* 删除: userdel -r [username]
  `userdel rone`
* 用户列表文件：/etc/passwd
  一行记录对应着一个用户，每行记录又被冒号(:)分隔为7个字段，其格式如下：
  `用户名:口令:用户标识号:组标识号:注释性描述(用户名):主目录:登录Shell`
* 用户组列表文件：/etc/group
  `组名：密码：GID：该用户组中的用户列表`
* 查看系统中有哪些用户：cut -d : -f 1 /etc/passwd
* 查看可以登录系统的用户：cat /etc/passwd | grep -v /sbin/nologin | cut -d : -f 1
* 查看用户操作：w命令(需要root权限)
* 查看某一用户：w 用户名
* 查看登录用户：who
* 查看用户登录历史记录：last


#### 文件
* 新建文件夹
  `mkdir [-p] [-m] 文件夹名`
  * -p：递归创建所有层级的文件。
    `mkdir -p /home/rone/test/20200529`
  * -m：目标权限：建立目录时，同时设置目录权限。
    `mkdir -m 755 test`
* 新建文件
  `touch 文件名`
* 删除文件夹(只能删除空的文件夹)
  `rmdir 文件夹名`
* 删除文件
  `rm [-d] [-f] [-i] [-r] 文件名`
  * -d: 删除空白文件夹。
  * -f: 强制删除文件或文件夹。
  * -i: 删除已有文件或文件夹之前先询问用户。
  * -r: 递归处理，将指定文件夹下的所有文件和子目录一并处理。
* 复制，若想要重命名并复制，第二参数为一个文件 cp rone.txt ./snow.txt
  `cp [-r] 源文件或目录 目标文件或目录`
  * -r: 递归处理，用来复制包含的文件夹。
* 重命名、移动
  `mv 旧文件路径 新文件路径`
* 创建软连接
  `ln -s 源文件 连接文件`
* 查看文件的权限
  `ll [文件名条件]` or `ls -l`
  * 文件名条件，ll j* 查看以j开头的文件
  * 详细信息详解：
    `一位字母(文件类型)九位字母(操作权限) 文件数量 拥有者 拥有用户组 文件大小(单位byte) 最近修改时间 文件名`
    * 文件类型：
      * ”-”,普通文件.
      * ”d”目录,字母”d”,是dirtectory(目录)的缩写.
      * “l”符号链接。请注意,一个目录或者说一个文件夹是一个特殊文件,这个特殊文件存放的是其他文件和文件夹的相关信息.
      * “b”块设备文件。
      * “c”字符设备文件。
    * 九位操作权限，分为三组，分别为拥有者的权限，拥有用户组，其他用户
      * r: 读取权限，无为 -
      * w: 写权限，无为 -
      * x: 执行权限，无为 -
    `drwxr-xr-x  2 root  root  4096 May 28 14:35 test`
* 改变文件或目录的访问权限
  *  数字设定法
    `chmod [-R] 三位权限数字   文件`
    * -R 　递归处理，将指定目录下的所有文件及子目录一并处理。
    * 三位权限数字：rwx权限编程二进制。1：拥有权限，0：没有权限，然后转换成10进制。第一位权限数字：文件的拥有者、创建者，第二位权限数字：文件拥有者同组的用户，第三位数字：其他用户。
      * rwx 111 7
      * rw- 110 6
      * r-x 101 5
      * r-- 100 4
      * --- 000 0
      `chmod 777 test`
* 更改文件属主，也可以同时更改文件属组
  `chown ［-R］ 用户[:用户组] 文件`
  * -R 　递归处理，将指定目录下的所有文件及子目录一并处理。
  `chown -R rone test`
  `chown -R rone:rone test`


#### tail 查看文件
`tail [-f] [-n] 文件`
* -f 该参数用于监视File文件增长。
* -n Number 从倒数 Number 行位置读取指定文件，制定显示多少行数据。
* `tail -f filename        说明：监视filename文件的尾部内容（默认10行，相当于增加参数 -n 10），刷新显示在屏幕上。退出，按下CTRL+C。`
* `tail -n 20 filename     说明：显示filename最后20行。`


#### find 查找文件
`find [查找起始路径] 查找条件`
* 查找起始路径: 就是个目录路径，相对和绝对都可以。默认当前。
* 名称条件
  `-name/-iname(忽略大小写) 文件名(*:任意内容,?:任意一个字符,[]任意一个中括号内的字符)`
  * 查找是忽略没有权限的文件夹，在最后加上 2>/dev/null
  `find / -name rone.txt 2>/dev/null`


#### grep 用于查找文件里符合条件的字符串
`grep 目标字符串 文件地址路径`
* 文件地址，支持绝对路径和相对路径，支持 * 等通配符。
  `grep "失败" ./2023-08-*/rone.log`


#### date 查看/设置 系统时间
```shell
date    #查看系统时间
date -s "20210312 17:00:00" #设置系统时间
```


#### history 历史记录
`history`
* ctrl + R 在历史记录中的命令


#### others
* ping ipaddress(IP地址、域名、主机名) 查看网络是否通
* rpm -q 查询已安装的软件包 
  * rpm -q Name   查找指定的软件包
  * rpm -qa       查找所有
  * rpm -qi       显示软件包的信息
  * rpm -ql       显示软件包的文件列表
  * rpm -qs       显示包中文件的状态
  * rpm -qd       显示被标注为文档的文件列表
* 查看端口使用情况
  `netstat -tunlp|grep 端口号`
  * 根据pid干掉进程：kill -9 进程号
* ps -ef |grep 服务名   查询某服务相关的进程
* df -hl  查看磁盘剩余空间信息
  ```
  Filesystem      Size Used Avail Use% Mounted on
  文件系统          容量  已用  可用  已用%  挂载点
  ```
* 查看host文件配置 `vi /etc/hosts`


************************************************************************************************************************
### 安装软件
#### jdk
  ```shell
  # 下载文件
  # 解压缩，并存放与要安装的路径下(通常为/usr/bin/)。使用tar命令解压缩
  # 配置环境变量
  vi ~/.bashrc
  ## 在末尾追加：
  export JAVA_HOME=/usr/lib/jdk1.8.0_162
  export JRE_HOME=${JAVA_HOME}/jre  
  export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib  
  export PATH=${JAVA_HOME}/bin:$PATH
  ## 使环境变量马上生效
  source ~/.bashrc
  # 设置系统默认jdk
  update-alternatives --install /usr/bin/java java /usr/lib/jdk1.8.0_162/bin/java 300  
  update-alternatives --install /usr/bin/javac javac /usr/lib/jdk1.8.0_162/bin/javac 300  
  update-alternatives --install /usr/bin/jar jar /usr/lib/jdk1.8.0_162/bin/jar 300   
  update-alternatives --install /usr/bin/javah javah /usr/lib/jdk1.8.0_162/bin/javah 300   
  update-alternatives --install /usr/bin/javap javap /usr/lib/jdk1.8.0_162/bin/javap 300 
  ## 然后执行:
  update-alternatives --config java
  # 测试是否安装成功
  java -version
  ```


#### redis
  ```
  # 下载、解压、移动到安装目录(我是放在/opt/下)
  wget http://download.redis.io/releases/redis-5.0.7.tar.gz
  tar -zxvf redis-5.0.7.tar.gz
  # 编译(在解压后Redis目录下)、进入src路径下进行安装
  make
  make install
  # 在src路径下启动服务redis-server，已成功安装
  redis-server
  # 修改配置，**/redis-5.0.7/redis.conf
  ## 解除ip绑定,让外网用户也能访问
  ### 注释掉 bind 127.0.0.1
  ## 修改设置为后台启动(守护程序)
      daemonize yes
  ## 增加设置密码
      requirepass 密码
  ## 修改设置日志文件路径
      logfile "日志文件路径"
  # 指定配置文件启动,，指定到上一步修改的配置文件
  redis-server ../redis.conf
  # 关闭服务
  ## 本机关闭
  redis-cli	链接上Redis
  shutdown [nosave|save]
  ## 远程关闭
  redis-cli -h ip  -p 端口 -a 密码  shutdown
  ```
