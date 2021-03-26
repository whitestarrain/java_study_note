
# Java 基础

## 基础

- equals方法使用注意
  - `常量.equals`
  - `Objects.equals` **推荐**
  - hashcode和equals

- 包装类
  - Integer当数值在-128 ~127时，会将创建的 Integer 对象缓存起来
  - 包装数据类型不能用 equals 来判断
- BigDecimal
  - 浮点数之间的等值判断，基本数据类型不能用==来比较（精度丢失）
  - 使用 BigDecimal 来定义浮点数的值，再进行浮点数的运算操作。
  - 推荐使用String作为参数传入BigDecimal构造方法
- 类型选取
  - 所有的 POJO 类属性必须使用包装数据类型。
  - RPC 方法的返回值和参数必须使用包装数据类型。
  - 所有的局部变量使用基本数据类型。
- Arrays.asList
  - Arrays.asList()将数组转换为集合后,底层其实还是数组
  - 传递的数组必须是对象数组，而不是基本类型。<br />当传入一个原生数据类型数组时，Arrays.asList() 的真正得到的参数就不是数组中的元素，而是数组对象本身
  - 使用集合的修改方法:`add()、remove()、clear()`会抛出异常。<br />Arrays.asList() 方法返回的并不是 java.util.ArrayList ，而是 java.util.Arrays 的一个内部类,
  - 如何正确的将数组转换为ArrayList?
    - `new ArrayList<>(Arrays.asList("a", "b", "c"))`(推荐)
    - 使用 Java8 的Stream(推荐)
    - 使用 Guava(推荐)
    - 使用 Apache Commons Collections
    - 使用 Java9 的 List.of()方法
- [fail-fast、fail-safe机制](https://juejin.cn/post/6879291161274482695)
- 枚举
  - 本质
  - 常见用法
- 静态内部类与非静态内部类
- 什么是Java多态
- 常用关键字
- 什么是反射
- 静态代理+JDK/CGLIB 动态代理
- 常见的 IO 模型有哪些？Java 中的 BIO、NIO、AIO 有啥区别?

## 集合

- 为什么要使用集合
- HashMap
  - 1.7
    - 存储结构：数组+链表
    - 头插
    - [resize死循环](https://juejin.cn/post/6844903554264596487)
  - 1.8
    - 存储结构：数组+链表+红黑树。红黑树结构转换条件
    - 尾插：为什么改了
    - 如何解决的resize死循环
  - 线程不安全：put的时候导致的多线程数据不一致
  - 初始化大小
  - 扩容机制，LoadFactory
  - 容量为什么要2的幂
  - 有什么同步容器/并发容器


- ConcurrentHashMap
  - 安全失败
  - 1.7
    - 数组+链表
    - **segment分段锁** -- HashBucket--HashEntry
      - 继承了ReentrantLock
      - 尝试获取锁存在并发，竞争，阻塞
    - get高效，volatile修饰，不需要加锁
  - 1.8
    - 数组+链表+红黑树
    - CAS+synchronized
      - CAS失败自旋保证成功
      - 再失败就sync保
    - node
- ArrayList
  - 底层结构：数组
  - 初始大小与扩容机制
  - RandomAccess 接口
  - `System.arraycopy()` 和 `Arrays.copyOf()`方法
  - 有什么同步容器/并发容器
- LinkedList
  - 底层结构：双向链表
- Set
  - comparable 和 Comparator 的区别
  - 无序性和不可重复性的含义是什么
  - 有什么同步容器/并发容器

- 集合比较问题
  - List,Set,Map 三者的区别？
  - Arraylist 和 Vector 的区别?
  - Arraylist 与 LinkedList 区别?
  - ConcurrentHashMap 和 Hashtable 的区别
  - 比较 HashSet、LinkedHashSet 和 TreeSet 三者的异同
  - HashMap和HashTable区别
  - HashMap与HashSet区别（HashSet底层基于HashMap）
  - HashMap和TreeMap区别

## 多线程

- 并发编程三大特性
  - 原子性
  - 可见性
  - 有序性

- JMM
  - 基础结构
  - JMM与Java内存区域划分的区别与联系
  - 原子操作
  - 重排序
    - 组成
      - 编译器优化重排
      - 指令并行重排
      - 内存系统重排
  - as-if-serial
  - happens-before

- volatile
  - 保证内存可见性
    - 说明
    - 原理:<br /> MESI缓存一致性协议<br /> cpu总线嗅探机制<br /> lock
  - 禁止重排序
    - 说明
    - 原理:<br />内存屏障
  - 不保证原子性：原因

- 锁的变迁
  - jdk1.5之前:synchronized重量锁
  - jdk1.5
    - 原因
    - 变化:增加
      - Lock锁
      - 并发容器
      - 线程池
    - 依据原理
      - Atomic--UnSafe--CAS
      - AQS
      - LockSupport
      - volatile
  - jdk1.6
    - 变化:synchronized锁的升级
    - 原因
  - jdk1.8:增加StampedLock

- synchronized
  - 使用位置
    - 代码块
      - 自己指定对象锁
      - 本质：
        - monitorenter
        - monitorexit
        - 程序计数器
    - 成员方法
      - this对象锁
      - 本质：ACC_SYNCHRONIZED
    - 静态方法
      - .class类锁
      - 本质：ACC_SYNCHRONIZED，ACC_STATIC
  - 锁的升级(不可逆)
    - 无锁(CAS)
    - 偏向锁
      - 原理
      - 锁的性质
      - 对象头中的内容
      - 升级过程
    - 轻量锁
      - 原理
      - 锁的性质
      - 对象头中的内容
      - 升级过程
    - 重量锁
      - 原理
      - 锁的性质
      - 对象头中的内容
      - 阻塞的好处(cpu)与代价(内核态)
  - 和Lock锁的区别

- CAS
  - 乐观锁与悲观锁
  - 概念
  - 底层原理
  - CAS的应用：UnSafe和Atomic
  - 问题
    - ABA问题
      - AtomicStampedReference
      - AtomicMarkableReference
    - 循环时间长开销大
      - 解决思路是让JVM支持处理器提供的pause指令
    - 只能保证一个共享变量的原子操作
      - 使用JDK 1.5开始就提供的AtomicReference类保证对象之间的原子性，把多个变量放到一个对象里面进行CAS操作；
      - 使用锁。锁内的临界区代码可以保证只有当前线程能操作。

- AQS
  - 概念
  - 底层
    - Unsafe(提供CAS操作)
    - LockSupport(提供park/unpark操作)
  - 数据结构
  - 资源共享模式/同步方式
  - **模版设计模式**
  - 源码分析
    - 获取资源流程
    - 释放资源流程
  - 其他
    - AOS
    - AQLS

- LockSupport
  - 为什么LockSupport也是核心基础类? AQS框架借助于两个类：Unsafe(提供CAS操作)和LockSupport(提供park/unpark操作)
  - 写出分别通过wait/notify和LockSupport的park/unpark实现同步?
  - LockSupport.park()会释放锁资源吗? 那么Condition.await()呢? 
  - Thread.sleep()、Object.wait()、Condition.await()、LockSupport.park()的区别? **重点**
  - 如果在wait()之前执行了notify()会怎样? 如果在park()之前执行了unpark()会怎样? 

- 锁、通信工具类<br />和底层使用
  - AQS实现的锁(实现Lock接口)
    - ReentrantLock
      - 内部类Sync继承AQS
      - Condition底层使用LockSupport
    - ReentrantReadWriteLock
      - 读锁和写锁都有继承AQS的内部类Sync
  - AQS通信工具类
    - Semaphore
      - 内部有一个继承了AQS的同步器Sync
    - CountDownLatch
      - 内部有一个继承了AQS的同步器Sync
    - CyclicBarrier
      - 内部使用ReentrantLock
  - 非AQS的通信工具类
    - Exchanger
      - LockSupport
      - CAS(Atomic)
    - Phaser
      - 有使用LockSupport

- 锁的种类
  - 锁的有无
    - 乐观锁
    - 悲观锁
  - synchronized的锁
    - 无锁
    - 偏向锁
    - 轻量锁
    - 重量锁
  - 锁的性质分类
    - 可重入锁和非可重入锁
    - 公平锁与非公平锁
    - 读写锁和排它锁

- 并发集合容器
  - [什么是同步容器和并发容器](https://juejin.cn/post/6844903954719965192)
    ```
    markmap中会隐藏
    什么是同步容器？
    同步容器通过synchronized关键字修饰容器保证同一时刻内只有一个线程在使用容器，从而使得容器线程安全。
    synchronized的意思是同步，它体现在将多线程变为串行等待执行。
    （但注意一点，复合操作不能保证线程安全。举例：A线程第一步获取尾节点，
    第二步将尾结点的值加1，但在A线程执行完第一步的时候，B线程删除了尾节点，在A线程执行第二步的时候就会报空指针）

    什么是并发容器？
    并发容器指的是允许多线程同时使用容器，并且保证线程安全。
    而为了达到尽可能提高并发，Java并发工具包中采用了多种优化方式来提高并发容器的执行效率，
    核心的就是：锁、CAS（无锁）、COW（读写分离）、分段锁。
    ```
  - 同步容器
    - vector:在面对多线程下的复合操作的时候也是需要通过客户端加锁的方式保证原子性
    - HashTable
    - Collections下的各种 SynchronizedXXX
  - 并发容器
    - Queue
      - BlockingQueue
        - ArrayBlockingQueue
        - LinkedBlockingQueue
        - DelayQueue
        - Priority BlockingQueue
        - SynchronousQueue
        - LinkedBlockingDeque
        - LinkedTransferQueue
      - ConcurrenLinkedQueue
        - [LinkedBlockingQueue与ConcurrentLinkedQueue的区别](https://blog.csdn.net/lzxlfly/article/details/86710382)
    - ConcurrentMap
      - ConcurrentHashMap
      - ConcurrentNavigableMap
      - ConcurrentSkipListMap
    - CopyOnWrite
      - CopyOnWriteArrayList
      - CopyOnWriteArrayMap
      - CopyOnWriteArraySet
- 线程池

- ThreadLocal

## IO/NIO/AIO

# JVM

# 常用框架

## Spring

## SpringMVC

## SpringBoot

## Mybatis

# 数据库

## Mysql

## Redis

## MongoDB

# 基础

## 计算机网络

## 操作系统

## 算法

# zookeeper

# 分布式框架

## SpringCloud

## Dubbo

# 消息队列

## Kafka

## RocketMQ

# 分布式锁

## 数据库

## redis

## zookeeper

## etcd
