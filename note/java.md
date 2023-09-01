### JVM
#### 简介
* JVM的内存空间主要有以下：
  * 方法区，是各个线程共享的区域，存放类信息、常量、静态变量。
  * Java堆，也是线程共享的区域，我们的类的实例就放在这个区域，可以想象你的一个系统会产生很多实例，
    因此java堆的空间也是最大的。如果java堆空间不足了，程序会抛出OutOfMemoryError异常。
  * Java栈，是每个线程私有的区域，它的生命周期与线程相同，一个线程对应一个java栈，
    每执行一个方法就会往栈中压入一个元素，这个元素叫“栈帧”，而栈帧中包括了方法中的局部变量、用于存放中间状态值的操作栈。
    如果java栈空间不足了，程序会抛出StackOverflowError异常，递归容易诱发此类错误，递归如果深度很深，
    就会执行大量的方法，方法越多java栈的占用空间越大。
  * 本地方法栈，和java栈类似，只不过它是用来表示执行本地方法的，本地方法栈存放的方法调用本地方法接口，
    最终调用本地方法库，实现与操作系统、硬件交互的目的。

* 指令重排序: 编译器优化重排序 -> 指令级并行重排序 -> 内存系统重排序
  第一步属于编译器重排序，会按JMM的规范严格进行，换言之编译器重排序一般不会对程序的正确逻辑造成影响。
  第二、三步属于处理器重排序，会要求java编译器在生成指令时加入内存屏障。
  内存屏障把不能重排序的java指令保护起来，那么处理器在遇到内存屏障保护的指令时就不会对它进行重排序了。

* JVM中自带的线程：
    * Signal Dispatcher：Attach Listener 线程接收外部jvm命令，当命令接收成功后，会交给signal dispather线程去进行分发到各个不同的模块处理命令，并且返回处理结果。signal dispather线程也是在第一次接收外部jvm命令时，进行初始化工作。
    * Attach Listener：Attach Listener 线程是负责接收到外部的命令，而对该命令进行执行的并且把结果返回给发送者。通常我们会用一些命令去要求jvm给我们一些反馈信息，如：java -version、jmap、jstack等等。如果该线程在jvm启动的时候没有初始化，那么，则会在用户第一次执行jvm命令时，得到启动。
    * DestroyJavaVM：执行main()的线程在main执行完后调用JNI中的 jni_DestroyJavaVM() 方法唤起DestroyJavaVM 线程。JVM在服务器启动之后，就会唤起DestroyJavaVM线程，处于等待状态，等待其它线程（java线程和native线程）退出时通知它卸载JVM。线程退出时，都会判断自己当前是否是整个JVM中最后一个非daemon线程，如果是，则通知DestroyJavaVM 线程卸载JVM。
    * Finalizer：这个线程也是在main线程之后创建的，其优先级为10，主要用于在垃圾收集前，调用对象的finalize()方法。
    * Reference Handler：JVM在创建main线程后就创建Reference Handler线程，其优先级最高，为10，它主要用于处理引用对象本身（软引用、弱引用、虚引用）的垃圾回收问题。

* 在JAVA中，有六个不同的地方可以存储数据，就速度来说，有如下关系：寄存器 < 堆栈 < 堆 < 其他。
  * 寄存器（register）。这是最快的存储区，因为它位于不同于其他存储区的地方——处理器内部。
    但是寄存器的数量极其有限，所以寄存器由编译器根据需求进行分配。你不能直接控制，也不能在程序中感觉到寄存器存在的任何迹象。
  * 堆栈（stack）。位于通用RAM中，但通过它的“堆栈指针”可以从处理器哪里获得支持。堆栈指针若向下移动，则分配新的内存；
    若向上移动，则释放那些 内存。这是一种快速有效的分配存储方法，仅次于寄存器。
    创建程序时候，JAVA编译器必须知道存储在堆栈内所有数据的确切大小和生命周期，因为它必须生成 相应的代码，以便上下移动堆栈指针。
    这一约束限制了程序的灵活性，所以虽然某些JAVA数据存储在堆栈中——特别是对象引用，但是JAVA对象不存储其 中。
  * 堆（heap）。一种通用性的内存池（也存在于RAM中），用于存放所以的JAVA对象。
    堆不同于堆栈的好处是：编译器不需要知道要从堆里分配多少存储区 域，也不必知道存储的数据在堆里存活多长时间。
    因此，在堆里分配存储有很大的灵活性。当你需要创建一个对象的时候，只需要new写一行简单的代码，当执行 这行代码时，会自动在堆里进行存储分配。
    当然，为这种灵活性必须要付出相应的代码。用堆进行存储分配比用堆栈进行存储存储需要更多的时间。
  * 静态存储（static storage）。这里的“静态”是指“在固定的位置”。静态存储里存放程序运行时一直存在的数据。
    你可用关键字static来标识一个对象的特定元素是静态的，但JAVA对象本身从来不会存放在静态存储空间里。
  * 常量存储（constant storage）。常量值通常直接存放在程序代码内部，这样做是安全的，因为它们永远不会被改变。
    有时，在嵌入式系统中，常量本身会和其他部分分割离开，所以在这种情况下，可以选择将其放在ROM中
  * 非RAM存储。如果数据完全存活于程序之外，那么它可以不受程序的任何控制，在程序没有运行时也可以存在。


#### JVM参数
* JVM参数的三种类型
    * 标准参数（-）：所有的JVM实现都必须实现这些参数的功能，而且向后兼容。  
      例子：-verbose:class，-verbose:gc，-verbose:jni……
    * 非标准参数（-X）：默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容。  
      例子：-Xms20m，-Xmx20m，-Xmn20m，-Xss128k……
    * 非稳定的参数（-XX）：此类参数各个jvm实现会有所不同（用的最多：JVM调优），将来可能会随时取消，需要慎重使用。  
      例子：-XX:+PrintGCDetails，-XX:-UseParallelGC，-XX:+PrintGCTimeStamps……

* 参数数值类型
  * 尔类型。如-XX:+PrintGCDetails，其中 + 和 - 分别表示开启/关闭某个属性，PrintGCDetails表示打印GC详情。
  * KV设值类型。如-XX:NewSize=256M，设置年轻代空间大小为256M。

* 常见配置
  * 堆设置
    * -Xms	-X,memory,size	内存大小	-Xms20m	设置jvm初始化堆大小为20m，一般与-Xmx相同避免垃圾回收完成后jvm重新分。
    * -Xmx	-X,memory,max	内存最大	-Xmx20m	设置jvm最大可用内存大小为20m。
    * -Xmn	-X,memory,new	新生代内存	-Xmn10m	设置新生代大小为20m。整个堆大小=年轻代大小 + 年老代大小 + 持久代大小。持久代一般固定大小为64m，所以增大年轻代后，将会减小年老代大小。此值对系统性能影响较大，Sun官方推荐配置为整个堆的3/8。
    * -Xss	-X,stack,size	栈大小		-Xss128k：设置每个线程的栈大小为128k。
    * -XX:MaxPermSize=n	设置持久代大小，JVM8.0已经没有持久代，若设置则会警告
    * -XX:NewSize=n	设置年轻代大小
    * -XX:NewRatio=n	设置年轻代和年老代的比值.如:为3,表示年轻代与年老代比值为1:3,年轻代占整个年轻代年老代和的1/4
    * -XX:SurvivorRatio=n	年轻代中Eden区与两个Survivor区的比值.注意Survivor区有两个.如:3,表示Eden:Survivor=3:2,一个Survivor区占整个年轻代的1/5
  * 收集器设置
    * -XX:+UseSerialGC	设置串行收集器
    * -XX:+UseParallelGC	设置并行收集器。此配置仅对年轻代有效。即上述配置下，年轻代使用并发收集，而年老代仍旧使用串行收集。
    * -XX:+UseParalledlOldGC	设置并行年老代收集器
    * -XX:+UseConcMarkSweepGC	设置并发收集器，使用CMS收集器
  * 并行收集器设置
    * -XX:ParallelGCThreads=n	设置并行收集器收集时使用的CPU数。并行收集线程数。此值最好配置与处理器数目相等。
    * -XX:MaxGCPauseMillis=n	设置并行收集最大暂停时间
    * -XX:GCTimeRatio=n	设置垃圾回收时间占程序运行时间的百分比。公式为1/(1+n)
  * 并发收集器设置
    * -XX:+CMSIncrementalMode	设置为增量模式。适用于单CPU情况。
    * -XX:ParallelGCThreads=n	设置并发收集器年轻代收集方式为并行收集时，使用的CPU数。并行收集线程数。
    * -XX:CMSInitiatingOccupancyFraction=80 CMS gc，表示在老年代达到80%使用率时马上进行回收
  * 垃圾回收统计信息
    * -XX:+PrintGC
    * -XX:+PrintGCDetails	打印GC详情
    * -XX:+PrintGCTimeStamps	打印时间戳
    * -Xloggc:/usr/local/gc_%t_%p.log：将gc信息打印到指定的文件中，通过时间戳生成文件名


#### 堆、栈
* 堆、栈(也叫堆栈)，是JVM用来存放数据的。

| 栈                                    | 堆                  |
|--------------------------------------|--------------------|
| 存放的数据要指明其数据大小和生存周期，相应的代码执行结束，内存就会被释放 | 无需指明，由系统在合适的时候自动回收 |
| 存放基本数据类型的数据和变量的引用                    | 存放具体的变量内容          |
| 里面的数据共享                              | 各管各的               |
* 栈，数据共享的示例
  ```
  int i = 3;//栈首先会查看有没有 3 这个数据，没有就创建一个 3 ，并将i指向他。
  int j = 4;//创建一个 4 数据将指向。
  int k = 3;//栈检查到已有 3 ，直接将 k 指向了3.
  i = 5;//这里并不是将i指向的 3 修改为 5 ，而是在栈中检查有没有 5 ，没有的话就创建一个并指向，k的值还是 5 。
  i = 4;//检查到有 4 ，直接指向。
  ```


#### GC
* 基本流程: 当该对象没有被引用(强引用、软引用)、该对象被弱引用or虚引用 时标记为可回收。
    对象被回收前会检查其 finalize() 方法是否有必要执行，如果有必要执行会执行该方法，若在执行中该对象被重新引用则稍后不会被销毁。


#### 运行状态
* jmap，是JDK自带的一个命令行工具，用于生成Java堆转储文件（heap dump）和查看Java堆转储文件中的内存使用情况。
  * jmap的常用选项包括
    * -heap：打印Java进程的堆内存信息 
    * -dump：生成Java堆转储文件，可选参数包括文件名和格式。使用 JDK 自带的或 MAT 等工具分析。
    * -histo：输出Java堆中各个类的实例数量和占用内存大小。
    * -finalizerinfo：输出等待终止的对象信息。
    * -permstat：输出永久代中各个类的实例数量和占用内存大小。
    * -F：在生成Java堆转储文件时，强制执行，即使进程不响应。
    * -h：显示帮助信息。
  * 使用jmap生成Java堆转储文件的命令示例：`jmap -dump:format=b,file=heap.bin <pid>`。其中，format参数指定转储文件格式，b表示二进制格式；file参数指定转储文件名；<pid>指定Java进程ID。
  * 使用jmap查看Java堆转储文件中的内存使用情况的命令示例：`jmap -histo <pid>`。其中，<pid>指定Java进程ID。该命令将输出Java堆中各个类的实例数量和占用内存大小

* jstack，通过分析 jstack 生成的线程快照，你可以了解每个线程的状态、当前执行的方法调用栈，以及线程之间的锁竞争情况。这有助于识别线程死锁、线程阻塞、线程等待等问题，并进行相应的调优和修复。
  * 语法：jstack [ options ] <pid> [ > 输出的文件地址 ]
    * options : 可选参数，用于指定 jstack 命令的选项，多个参数用空格相隔。
        * -l : 输出关于锁的附加信息。
        * -F : 当目标进程没有响应时，强制生成线程快照。
        * -m : 输出 Java 和本地堆栈信息。
        * -h : 显示帮助信息。
    * pid : 必需参数，指定要生成线程快照的 Java 进程的进程 ID。
  * 生成的线程快照包含以下信息：
      * 线程 ID（Thread ID）：每个线程的唯一标识符。
      * 线程状态（Thread State）：显示线程的当前状态，常见的状态有：
        * NEW（新建）：线程已经被创建，但尚未启动。
        * RUNNABLE（运行中）：线程正在执行或准备执行。可能里面还能看到locked字样，表明它获得了某把锁。
        * BLOCKED（阻塞中）：线程在等待锁资源，被其他线程持有的锁阻塞。
        * WAITING（等待中）：线程在等待其他线程的通知或信号，无限期等待。
        * TIMED_WAITING（计时等待中）：线程在指定时间内等待其他线程的通知或信号。
        * TERMINATED（终止）：线程已经执行完毕或被中止。
      * 线程调用栈（Thread Stack）：显示线程当前执行的方法调用栈，从方法调用栈顶部到底部。可以看到方法名、类名、行号等信息，帮助你定位问题所在。
      * 线程锁信息（Thread Lock Information）：如果线程被阻塞在某个锁上，jstack 会显示该锁的信息，包括拥有锁的线程和等待该锁的线程。


************************************************************************************************************************
### Web
#### 异常
##### MultipartException
org.springframework.web.multipart.MultipartException: Could not parse multipart servlet request; nested exception is java.io.IOException：The temporary upload location [/tmp/tomcat.276123452235494.8080/work/Tomcat/localhost/ROOT] is not valid  
* reason: 在Linux系统中，springboot应用服务在启动（java -jar 命令启动服务）的时候，默认会在操作系统的/tmp目录下生成一个tomcat*的文件目录，
    部分上传/下载的文件先要转换成临时文件保存在这个文件夹下面。由于临时/tmp目录下的文件，在长时间没有使用的情况下，就会被Linux系统机制自动删除掉，
    导致下载文件时系统找不到文件路径导致IO异常。所以如果系统长时间无人问津的话，就可能导致上面这个问题。

* solutions: 修改这个临时文件目录到一个固定目录，不会被系统清除掉.
  * 启动脚本加上 -Djava.io.tmpdir = /home/cbpms(自定义个目录) 参数
  * 在系统中配置临时文件目录到一个不会被Linux自动回收的目录上
  * 其他方案可网上搜索


#### freemarker
* [官方手册](http://freemarker.foofun.cn/toc.html)、[其他参考文档](https://www.cnblogs.com/JealousGirl/p/6914122.html)

* 对于数字默认千分位分隔
  * 全局处理：在application.properties文件中加入
    `spring.freemarker.settings.number_format=0.##`
  * 仅在当前位置处理：${num?c} 此时num不能为null否则报错故可优化为
    `<#if num??>${num?c}</#if>`

* <#list>遍历时，获取索引，通过counter来获取
  `<#list datas as data> ${data?counter} </#list>`


************************************************************************************************************************
### 第三方组件
##### hibernate
* hibernate.cfg.xml 配置文件。`rone-core/src/main/resources/hibernate/hibernate.cfg.xml`

* hibernate中数据的四种状态。
  * 临时态：新建的对象，不在session缓存中，也不在数据表中
  * 持久化态：在session缓存中，也在数据表中
  * 游离态：不在session缓存中，但在数据表中
  * 删除态：被删除的对象，不存在

* session主要方法。
  * void session.flush();根据session缓存中的数据更新数据表中的记录(可能是insert、delete或update)
  * void session.refresh(Object);根据数据表中的记录去更新session缓存中的数据
  * void session.clear();清空session缓存
  * Serializable session.save(Object);将Object加入session缓存进入持久化状态
  * Object session.get(Class, Serializable);根据指定的ID从数据库获取一个持久化对象，没有时返回null，立即检索
  * void/Object session.load(Class, Serializable);根据指定的ID从数据库获取一个持久化对象，没有时抛出异常，延时检索
  * void session.update(Object);将一个游离状态的对象转变成持久化对象
  * void session.saveOrUpdate(Object);根据对象的OID去判断该对象是游离状态(save操作)还是临时状态(update操作)
  * Object session.merge(Object);首先判断对象是游离态还是临时态，1)临时态:持久化该对象；2)游离态：判断session缓存中是否有同OID的持久化对象
  *   ①有的话将持久化对象属性更新为该参数属性，返回引用，②否则判断数据表中是否有该OID的记录，没有的话同1)，有的话先从数据表加载该对象然后同①
  * void session.delete(Object);根据Object(游离态or持久化态)的OID去数据表中删除记录，若OID在数据表中不存在会抛出异常
  * void session.evict(Object);将一个持久化对象从session缓存中删除，变成游离态

* 自关联，@ManyToOne、@OneToMany。
  `rone-core/src/main/java/org/rone/core/util/hibernate/entity/People.java`
  * fetch 当一个实体类配置了 @ManyToOne、@OneToMany 等关联实体时，其默认的fetch获取方式(FetchType.EAGER)为主类被加载时关联的属性类数据也会立即被加载。
    而当我们把设置 fetch=FetchType.LAZY 时，主类数据加载后属性类数据不会立即被加载，这样可以提高主类的加载性能。
    当我们需要主类的性能又想在部分地方查询主类时属性类的数据也会立即加载时可以使用 left join fetch 语句来实现。
    ```
    from Item i left join fetch i.order left join fetch i.product where i.order.id=:paras0 order by i.sort
    ```

* HQL hibernate的数据库操作语法与具体的数据库无关，hibernate会自动解析成具体数据库的SQL语句。
  `rone-core/src/main/java/org/rone/core/util/hibernate/HibernateDemo.java`

* Hibernate乐观锁，能自动检测多个事务对同一条数据进行的操作，并根据先胜原则，提交第一个事务，其他的事务提交时则抛出org.hibernate.StaleObjectStateException异常。
  原理就是通过一个字段来判断当前数据表中的数据是否是当前事务读数时的数据，如果不是则报错，如果是则事务提交成功
  实现语法：数据表中新增一个字段(类型只能是 long,integer,short,timestamp,calendar，数字或timestamp)用来做版本控制，在实体类中用 @Version 标注该属性用于版本控制。
  当两个事务同一时间获取到数据A，事务1提交成功后，事务2提交时，由于事务2中版本控制字段的值与数据库中的值不一致(因为事务1提交成功后版本控制字段值会更新)，会抛出org.hibernate.StaleObjectStateException异常。

* 常见的异常
  * PersistentObjectException，org.hibernate.PersistentObjectException 持久化对象的时候出问题了，也就是说，数据记录在插入数据库的时候出现异常
    * 可能原因：主键设置为自生成，而插入时主键已设置
  * LazyInitializationException，org.hibernate.LazyInitializationException: could not initialize proxy - no Session 不能初始化代理——没有session
    * 原因：本身数据加载出来了，而使用@ManyToOne等关联的数据使用的是懒加载策略，当访问关联的数据时session已经关闭而导致的异常。
    * 解决方案：
      * 方案1：把这个类的延迟加载禁掉，fetch=FetchType.EAGER
      * 方案2：在hql语句中使用 left join强制去接在关联的数据。


************************************************************************************************************************
### 其他基础概念
#### I/O
* 字节、字符、字节流、字符流
  * Bit 最小的二进制单位 ，是计算机的操作部分。取值 0 或者 1
  * Byte（字节）是计算机操作数据的最小单位由 8 位 bit 组成 取值（-128-127）
  * Char（字符）是用户的可读写的最小单位，在 Java 里面由 16 位 bit 组成 取值（0-65535）
  * 字节流：操作 byte 类型数据，主要操作类是 OutputStream、InputStream 的子类；不用缓冲区，直接对文件本身操作。
  * 字符流：操作字符类型数据，主要操作类是 Reader、Writer 的子类；使用缓冲区缓冲字符，不关闭流就不会输出任何内容。

* 同步、异步、阻塞、非阻塞。同步、异步，是描述被调用方的。阻塞，非阻塞，是描述调用方的。
  * 同步，B 在接到 A 的调用后，会立即执行要做的事。A 的本次调用可以得到结果。
  * 异步，B 在接到 A 的调用后，不保证会立即执行要做的事，但是保证会去做，B在做好了之后会通知 A。A 的本次调用得不到结果，但是 B 执行完之后会通知 A。
  * 阻塞，A 在发出调用后，要一直等待，等着 B 返回结果。
  * 非阻塞，A 在发出调用后，不需要等待，可以去做自己的事情。

* Linux 5 种 IO 模型
  * 阻塞式 IO 模型，最传统的一种 IO 模型，即在读写数据过程中会发生阻塞现象。
    当用户线程发出 IO 请求之后，内核会去查看数据是否就绪，如果没有就绪就会等待数据就绪，
    而用户线程就会处于阻塞状态，用户线程交出 CPU。当数据就绪之后，内核会将数据拷贝到用户线程，
    并返回结果给用户线程，用户线程才解除 block 状态。
  * 非阻塞 IO 模型，在非阻塞 IO 模型中，用户线程需要不断地询问内核数据是否就绪，也就说非阻塞 IO 不会交出 CPU，而会一直占用 CPU。
    当用户线程发起一个 read 操作后，并不需要等待，而是马上就得到了一个结果。如果结果是一个 error 时，
    它就知道数据还没有准备好，于是它可以再次发送 read 操作。一旦内核中的数据准备好了，
    并且又再次收到了用户线程的请求，那么它马上就将数据拷贝到了用户线程，然后返回。
  * IO 复用模型，Java NIO 实际上就是多路复用 IO。
    在多路复用 IO 模型中，会有一个线程不断去轮询多个 socket 的状态，只有当socket 真正有读写事件时，
    才真正调用实际的 IO 读写操作。因为在多路复用 IO 模型中，只需要使用一个线程就可以管理多个 socket，
    系统不需要建立新的进程或者线程，也不必维护这些线程和进程，并且只有在真正有 socket 读写事件进行时，
    才会使用 IO 资源，所以它大大减少了资源占用。
  * 信号驱动 IO 模型。
    在信号驱动 IO 模型中，当用户线程发起一个 IO 请求操作，会给对应的 socket 注册一个信号函数，
    然后用户线程会继续执行，当内核数据就绪时会发送一个信号给用户线程，用户线程接收到信号之后，
    便在信号函数中调用 IO 读写操作来进行实际的 IO 请求操作。
  * 异步 IO 模型，是比较理想的 IO 模型。
    当用户线程发起 read 操作之后，立刻就可以开始去做其它的事。而另一方面，从内核的角度，
    当它受到一个asynchronous read 之后，它会立刻返回，说明 read 请求已经成功发起了，
    因此不会对用户线程产生任何 block。然后，内核会等待数据准备完成，然后将数据拷贝到用户线程，
    当这一切都完成之后，内核会给用户线程发送一个信号，告诉它 read 操作完成了。
    也就说用户线程完全不需要实际的整个 IO 操作是如何进行的，只需要先发起一个请求，
    当接收内核返回的成功信号时表示 IO 操作已经完成，可以直接去使用数据了。


#### 运算符
* ^ 位异或运算，运算规则：两个数转为二进制，然后从高位开始比较，如果相同则为0，不相同则为1。
* & 位与运算符，运算规则：两个数都转为二进制，然后从高位开始比较，如果两个数都为1则为1，否则为0。
* | 位或运算符，运算规则：两个数都转为二进制，然后从高位开始比较，两个数只要有一个为1则为1，否则就为0。
* ~ 位非运算符，运算规则：如果位为0，结果是1，如果位为1，结果是0。在Java中数据用补码表示，所以 ~11=-12
* 移位运算符
  ```
  <<  : 左移运算符, num << 1,相当于num乘以2
  >>  : 右移运算符, num >> 1,相当于num除以2
  >>> : 无符号右移, 忽略符号位, 空位都以0补齐, 只对32位和64位的值有意义
  ```


#### 原码、补码、反码
* 原码：数值的二进制表示；
* 反码：正数同原码，负数将原码的非符号位取反；
* 补码：正数同原码，负数将反码+1；
  ```
  int a = 3;
  原码：00000000 00000000 00000000 00000011
  反码：00000000 00000000 00000000 00000011
  补码：00000000 00000000 00000000 00000011
  int b = -5;
  原码：10000000 00000000 00000000 00000101
  反码：11111111 11111111 11111111 11111010
  补码：11111111 11111111 11111111 11111011
  ```
* 在Java中，所有数据的表示方法都是以补码的形式表示


#### others
* 项目启动时报错：Unsupported major.minor version 52.0
  意为当前环境不支持使用jdk1.8开发的项目，要么将项目中使用到1.8特性的内容移除(包含引入的jar包)，要么升级当前运行环境
  ```
  jdk对应的版本信息如下：
  1.8 = 52
  1.7 = 51
  1.6 = 50
  1.5 = 49
  1.4 = 48
  1.3 = 47
  1.2 = 46
  1.1 = 45
  ```

* final,finally,finalize
  * final用来修饰类、方法、变量，Final类不能被继承方法，Final方法不能被重写，Final变量是不可变的；
  * finally是和try{}catch{}配套使用，一般用来释放一些占用的资源；
  * finalize是Object类的一个方法，用来实现对资源的回收。

* && 和 &，|| 和 | 也是一样
  * && 只要不满足前面的条件，后面的条件就没有去判断
  * & 不管前面的条件满足与否，他会判断所有的条件

* static{} 静态代码块、{} 构造代码块、构造器constructor。
  执行循序：static{} -> {} -> constructor
  其中 static{} 在该类被加载时即执行，例如在工具类中，在首次调用起静态方法前执行，后续调用该类的静态方法也不会再次执行 static{} 代码块
  `rone-core/src/main/java/org/rone/core/jdk/ClassInitDemo.java`

* 静态方法不会被子类重写。
  静态方法是通过类名调用，当父类和子类都有一个同名的静态方法(其参数一致)，此时在子类中父类的静态方法被隐藏。

* 自动装箱、自动装箱
  * 自动装箱，可以简单的理解为将基本数据类型封装为对象类型，来符合java的面向对象。
    ```
    // 声明一个Integer对象
    Integer num = 10;
    // 以上的声明就是用到了自动的装箱：解析为
    Integer num = new Integer(10);
    ```
  * 自动拆箱，就是将对象重新转化为基本数据类型。
  * 坑
    * 三目运算符的nullPointerException，由于基础数据类型的自动拆箱装箱机制导致的异常场景，详见阿里巴巴java开发手册1.8.4
      ```
      boolean falseBoolean = false;
      Boolean nullBoolean = null;
      boolean trueBoolean = true;
      Boolean result = trueBoolean ? nullBoolean : falseBoolean;
      System.out.println(result);
      ```







