
### 命令
[参考](http://doc.redisfans.com)
#### key
* keys pattern 查找所有符合给定模式 pattern 的 key
```redis
#匹配数据库中所有key
KEYS *
#匹配 hello ， hallo 和 hxllo 等
KEYS h?llo 
#匹配 hllo 和 heeeeello 等
KEYS h*llo 
#匹配 hello 和 hallo ，但不匹配 hillo 。
KEYS h[ae]llo 
```

************************************************************************************************************************
### 基础
* 开源的redis可视化工具 https://github.com/qishibo/AnotherRedisDesktopManager

* Redis
  * 基于内存亦可持久化的数据库；
  * Key-Value数据库，value可以是String，List，hash(key-value)，set，zset(有序集合)；
  * 也划分数据库，以0,1,2...索引号作标识，可用select命令切换

* Pipeline 管道，用于一次执行多次Redis操作

* 设置Redis内存的大小
  使用命令配置
    ```redis
    config get maxmemory
    config set maxmemory 100mb
    config set maxmemory 3G
    ```

* Redis的字符底层实现：redis自构建的 简单动态字符串SDS（simple dynamic string）。
  [参考文档](https://mp.weixin.qq.com/s/R21MUOv27u3qLFXZaGV7ug)
    * SDS的三个重要属性
        * int free; // buf[]数组未使用字节的数量
        * int len; // buf[]数组所保存的字符串的长度
        * char buf[]; // 保存字符串的数组
    * 未使用底层C语言的字符串的原因有如下几点：
        * C字符串不能很好的支持字符串的变更，会导致数据溢出占用了其他数据的空间。
        * 在获取字符串长度时SDS通过空间换时间效率更高。
        * C字符串在频繁的变更时会引发内存的重新分配，该操作很耗资源。
        * C字符串中已空格符作为字符串结束的标志，所以其无法存储空格符，SDS则无此限制。

#### 淘汰策略
淘汰策略主要是基于访问频率和是否有最近的访问来判断是否淘汰
```redis
config get maxmemory-policy
config set maxmemory-policy [策略名称]
```
| Redis4.0 提供的淘汰策略 | 说明                                    |
|------------------|---------------------------------------|
| noeviction(默认)   | 若是内存的大小达到阀值的时候，所有申请内存的指令都会报错。         |
| allkeys-lru      | 所有key都是使用「LRU算法」进行淘汰。                 |
| volatile-lru     | 所有「设置了过期时间的key使用LRU算法」进行淘汰。           |
| allkeys-random   | 所有的key使用「随机淘汰」的方式进行淘汰。                |
| volatile-random  | 所有「设置了过期时间的key使用随机淘汰」的方式进行淘汰。         |
| volatile-ttl     | 所有设置了过期时间的key「根据过期时间进行淘汰，越早过期就越快被淘汰」。 |
| volatile-lfu     | 所有「设置了过期时间的key使用LFU算法」进行淘汰。           |
| allkeys-lfu      | 所有key都是使用「LFU算法」进行淘汰。                 |
* LRU算法:(Least Recently Used) 即表示最近最少使用，也就是在最近的时间内最少被访问的key，算法根据数据的历史访问记录来进行淘汰数据。
    核心的思想就是：「假如一个key值在最近很少被使用到，那么在将来也很少会被访问」。
    实际上Redis实现的是近似的LRU算法，「通过随机采集法淘汰key，每次都会随机选出5个key，然后淘汰里面最近最少使用的key」。
* LFU算法:(Least Frequently Used)即表示最近频繁被使用，也就是最近的时间段内，频繁被访问的key，它以最近的时间段的被访问次数的频率作为一种判断标准。
  核心思想就是：根据key最近被访问的频率进行淘汰，比较少被访问的key优先淘汰，反之则优先保留。
  针对于LRU算法淘汰的一种弊端：就是假如一个key值在以前都没有被访问到，然而最近一次被访问到了，那么就会认为它是热点数据，不会被淘汰。

#### 删除过期键策略
* 「定时删除」：创建一个定时器，定时的执行对key的删除操作。对内存友好：实时清理内存，对cpu不友好：需要一个cpu去维护一个定时器。
* 「惰性删除」：每次只有再访问key的时候，才会检查key的过期时间，若是已经过期了就执行删除。对cpu友好，对内存不友好。
* 「定期删除」：每隔一段时间，就会检查删除掉过期的key。上面两个方案的折中，根据具体的业务，合理的取一个时间定期的删除key。

#### Redis持久化的方式
* RDB持久化,Redis DataBase,快照持久化.
  * 原理：将某一时刻的内存中的数据刷到磁盘中。
  * 触发机制：
    * 手动触发：save(会阻塞其他命令)、bgsave(Redis会另开一个线程执行持久化)。
    * 自动触发：配置文件 save m n 意为 m 秒内发生 n 次变化时触发 bgsave。多条配置都会生效
    * 优点：文件小，恢复速度快
    * 缺点: 数据完整性低，生成RDB文件消耗CPU、内存资源
* AOF持久化,Append Only File,文件追加持久化.
  * 原理：通过保存下所有客户端的写命令，在回复数据时模拟一个虚拟客户端挨个执行之前的命令实现数据回复。类似于记账。
  * 触发机制：配置文件中配置上 appendonly yes 和如下的配置之一
    * appendfsync always：每次写入都进行刷盘操作，对性能影响最大，占用磁盘 IO 较高，数据安全性最高。
    * appendfsync everysec：1秒刷一次盘，对性能影响相对较小。
    * appendfsync no：按照操作系统的机制进行刷盘，对性能影响最小，数据安全性低。
  * 重写机制：对AOF存储的命令文件进行瘦身的机制
    * auto-aof-rewrite-percentage X：AOF 文件距离上次文件增长超过百分之X后触发重写
    * auto-aof-rewrite-min-size Ymb：AOF 文件体积达到Ymb时触发重写
  * 优点：数据完整性高
  * 缺点: 文件体积大，生成AOF文件时间消耗IO资源
* 混合持久化，综合上述两者的优点。
  * 原理：在写入的时候先把数据以 RDB 的形式写入文件的开头，再将后续的写命令以 AOF 的格式追加到文件中。
  * 触发机制：配置文件中通过 aof-use-rdb-preamble 开启。


    