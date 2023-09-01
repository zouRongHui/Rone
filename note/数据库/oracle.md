

### 函数
#### decode() 条件判断
`decode(条件, 值1,返回值1, 值2,返回值2,… 值n,返回值n, 缺省值)`
```
-- 等同于如下的java代码
if (条件==值1) {
    return 返回值1;
} else if (条件==值2) {
    return 返回值2;
} 
...
else if (条件==值n) {
    return 返回值n;
} else {
    return 缺省值;
}

-- 示例：用来实现按照name字段的['=1','(1,10]','(10,20]','(20,30]','(30,40]','(40,50]','>50', 其他]顺序排序
order by "decode"(name, '=1', 1, '(1,10]', 2, '(10,20]', 3, '(20,30]', 4, '(30,40]', 5, '(40,50]', 6, '>50', 7, 99);
```

#### to_char() 转换成字符串
日期、数字转换成字符串
* 日期对象转字符串，to_char(日期对象, 日期的格式(详见to_date()说明))
  `select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') as nowTime from dual;`
* 数字对象转字符串，to_char(数字，格式)，各个oracle版本语法不仅一致

| 格式 | 说明                               |
|----|----------------------------------|
| 9  | 指定位数，不足的情况，小时点前用空格展示，小数点后的用 0 展示 |
| 0  | 指定位数，不足的用 0 展示                   |
| .  | 小数点                              |
| ,  | 分组（千）分隔符                         |
| PR | 尖括号内负值                           |
| S  | 显示正负号，其余格式下负号会正常显示，但正号会用空格展示     |
```oracle
-- 此处案例为Oracle 19c
select
    to_char(485,'999'), -- ' 485'
    to_char(-485,'999'), -- '-485'
    to_char(12,'9990999.9'), -- '    0012.0'
    to_char(0.1,'0.9'), -- ' 0.1'
    to_char(0.1,'9.9'), -- '  .1'
    to_char(-0.1,'0.9'), -- '-0.1'
    to_char(-0.1,'99.99'), -- '  -.10'
    to_char(148.5,'999.999'), -- ' 148.500'
    to_char(1485,'9,999'), -- ' 1,485'
    to_char(-485,'999PR'), -- '<485>'
    to_char(485,'999PR'), -- ' 485 '
    to_char(485,'S999'), -- '+485'
    to_char(-485,'S999') -- '-485'
from dual;
```

#### to_number() 转换成数字
字符串转换成数字
```oracle
-- to_number(字符) 直接返回字符代表的数字，若字符非数字格式若报错ORA-01722
select to_number('00345.89') from dual; -- 345.89
-- to_number(字符, 格式(详见to_char()中数字格式)) 字符中的数字格式必须满足后面定义的格式，否则报错ORA-01722
select to_number('00123456.78', '99999999.99') from dual; -- 123456.78
```

#### to_date() 转日期
日期字符串转日期对象，to_date(日期字符串, 格式)

| 格式   | 说明        |
|------|-----------|
| yy   | 年份后两位，20  |
| yyyy | 四位年，2020  |
| mm   | 两位月，04    |
| dd   | 当月第几天，27  |
| ddd  | 当年第几天     |
| hh24 | 24小时进制，15 |
| mi   | 60进制，45   |
| ss   | 60进制，33   |
```oracle
select
    to_date('2023-08-17 14:31:25','yyyy-mm-dd hh24:mi:ss'), -- 2023-08-17 14:31:25
    to_date('23-100 10','yy-ddd hh:mi:ss') -- 2023-04-10 10:00:00
from dual;
```

#### round() 四舍五入
* 对小数点后位数四舍五入，round(数字, 保留的小数点位数)
```oracle
select
    round(123.456, 0), -- 123
    round(123.456, 1) -- 123.5
from dual;
```
* 对日期数据，round(日期, 格式)
```oracle
select
    -- year、yyyy    舍入到某年的1月1日，即前半年舍去，后半年则年数+1
    round(to_date('2020-04-27 17:25:33', 'yyyy-mm-dd hh24:mi:ss'), 'year'), -- 2020-01-01 00:00:00
    -- month   舍入到最近的月初
    round(to_date('2020-04-27 17:25:33', 'yyyy-mm-dd hh24:mi:ss'), 'month'),-- 2020-05-01 00:00:00
    -- ddd、dd     舍入到最近的一天
    round(to_date('2020-04-27 17:25:33', 'yyyy-mm-dd hh24:mi:ss'), 'ddd'),-- 2020-04-28 00:00:00
    -- day、d     舍入到最近的周的周日
    round(to_date('2020-04-27 17:25:33', 'yyyy-mm-dd hh24:mi:ss'), 'day'),-- 2020-04-26 00:00:00
    -- hh24、hh 摄入到最近的小时
    round(to_date('2020-04-27 17:25:33', 'yyyy-mm-dd hh24:mi:ss'), 'hh24'),-- 2020-04-27 17:00:00
    -- mi  舍入到最近的分钟
    round(to_date('2020-04-27 17:25:33', 'yyyy-mm-dd hh24:mi:ss'), 'mi') -- 2020-04-27 17:26:00
from dual;
```

#### nvl()、nvl2() 判空
* nvl(expr1, expr2)，若expr1不为空(null、空字符)则返回expr1，否则返回expr2
* nvl2(expr1, expr2, expr3)，若expr1不为空(null、空字符)则返回expr2，否则返回expr3
```oracle
select
    nvl(null, 'expr2'), -- expr2
    nvl('expr1', 'expr2'), -- expr1
    nvl2(null, 'expr2', 'expr3'), -- expr3
    nvl2('expr1', 'expr2', 'expr3') -- expr2
from dual;
```

#### substr() 字符串截取
substr(string,start,length) 截取数据库某个字段中的一部分。与java不同，这里是从索引从1起始
```oracle
select
    substr('123456',3,2), -- 34
	substr('123456',-3,2), -- 45
	substr('123456',0,2), -- 12，从0开始会自动调整为从1开始
	substr('123456',1,2) -- 12
from dual;
```

************************************************************************************************************************
### solutions
#### 根据一张表去更新另一张表
`update t1 set (t1.UNIT,t1.FIRST,t1.SECOND) = (select t2.UNIT,t2.FIRST,t2.SECOND from t2 where t1.WORK_NUMBER=t2.MANAGER_NO) where t1.UNIT is null;`

#### 排序中null值的问题
* Nulls first：表示null值的记录将排在最前
* Nulls last：表示null值的记录将排在最后

#### 递归查询
product_level_id:当前节点id，parent_level_id:父节点的id
* 向下递归，从根开始：`select * from cbpms_product_tree t start with t.product_level_id = 'CZ00300' connect by prior t.product_level_id = t.parent_level_id;`
* 向上递归，从某个叶子开始：`select * from cbpms_product_tree t start with t.product_level_id = 'CZ00357' connect by prior t.parent_level_id = t.product_level_id;`

#### 建立索引
* 语法：create index 索引名 on 表名(列名);
  `create index rone_idx on table_rone (id);`

#### 批量插入
推荐使用示例1
```oracle
create table Rone (
  id varchar2(20),
  name varchar2(20),
  sex varchar2(20),
  age number(4,0),
  tel varchar2(20)
);
-- 示例1：
insert all into Rone(id, name, sex, age, tel)
  into Rone(id, name, sex, age, tel) values ('12', 'jack1', '男', 12, '13345674567' )
  into Rone(id, name, sex, age, tel) values ('13', 'jack2', '男', 13, '13345674567')
  select '14', 'jack', '男', 13, '13345674567' from dual;
-- 示例2：
insert into Rone(id, name, sex, age, tel)
select '24', 'jack', '男', 22, '13345674567' from dual
union select '25', 'jack', '男', 22, '13345674567' from dual
union select '26', 'jack', '男', 32, '13345674567' from dual
```

************************************************************************************************************************
### 坑
* 在Oracle Database中，VARCHAR2字段类型，最大值为4000，SQL参考手册中也明确指出VARCHAR2的最大大小为4000

* Oracle中空字符串自动转为null，故sql语句中 attr != '' 是没有匹配结果的

* Oracle中属性名与关键字重复的处理方案为用""标注而不是``

* Oracle中
```oracle
	SELECT 1 AS name FROM dual; -- 结果属性名为大写
	SELECT 1 AS "name" FROM dual; -- 结果属性名为""中的内容
```

* 单引号转义
```oracle
-- 需要往表中插入 ='rone' 这样的字符串
INSERT INTO dual VALUES ('=''rone''');
```

************************************************************************************************************************
### 序列
[参考文档](https://blog.csdn.net/love___code/article/details/79182455)
* 序列是oracle提供的用于产生一系列唯一数字的数据库对象。
* 一般用于作为自增的主键、全局唯一的流水号之类
* 使用示例
  ```oracle
  -- 定义
  -- create sequence 序列名 [increment by 增长数] [start with 起始值] [minvalue 最小值] [maxvalue 最大值] [nocycle/cycle] [nocache/cache 100];
  -- 创建一个序列，名叫 rone_seq，从1001开始，每次增长1，最大值为99999，最小值为1001，不缓存，不循环。
  create sequence rone_seq increment by 1 start with 1001 maxvalue 99999 minvalue 1001 nocycle nocache;
  -- 查询数值
  select rone_seq.nextval from dual; -- 生成一个新的值并返回
  select rone_seq.currval from dual; -- 返回当前序列的值
  -- 修改序列，基本就是将 create 改为了 alter，其中已使用过的序列不可修改其 start with 属性
  alter sequence rone_seq increment by 2 maxvalue 100000 minvalue 2002 cache 100 cycle;
  -- 删除序列
  drop sequence rone_seq;
  ```
* 裂缝，在使用序列的过程中可能会出现裂缝，也就是序列不连续了，比如本来是主键值依此应该是1，2，3，4，出现裂缝的意思就是主键值是1，3，4，5。
  产生的主要原因如下： 
  * 回滚数据导致的，我们知道可以使用rollback可以对我们刚刚插入的数据进行回滚，但是我们要知道的一点就是序列不会回滚也就是这个值我们已经使用过了，
    尽管现在不用了，但是虚拟指针已经指向了后面的位置了，不可能回退了，所一展现给我们的也就产生了裂缝。 
  * 系统异常：也就是我们插入数据时候系统异常了，数据并没有插入进去， 但是序列值已经使用了，所以下次在此调用序列时就是后面一个序列了，也产生了裂缝。 
  * 多个表使用同一个序列，因为有些值在其他表显示，所以在这个表上面看就出现了裂缝。
* 关于cache，为了避免序列在运用层实现序列而引起的性能瓶颈，oracle序列就允许提前生成好序列然后存入到内存中，这样当用户申请序列时，我们直接从内存中获取，提高执行效率。
  在这里我们一次性缓存的数据不能太大，因为数据库重启时，会清空完所有的缓存序列，然后启动后就从上次缓存的最大值加一重新缓存，如果太大，就会使很多序列没有被使用，也就是产生前面的裂缝。

    
    

