# 1. synchronized详解

## 1.1. 简介

- 作用：能够保证在同一时刻最多只有一个线程执行该段代码，以达到保证并发安全的效果。
- 地位：
  - Synchronized是Java的关键字，被Java语言原生支持
  - 是最基本的互斥同步手段
  - 是并发编程中的元老级角色，是并发编程的必学内容

## 1.2. 使用方式

## 1.3. 常见情况

- 两个线程同时访问一个对象的同步方法。同一个锁。
- 两个线程访问的是两个对象的同步方法。不是一个锁。
- 两个线程访问的是synchronized的静态方法。同一个锁。
- 同时访问同步方法与非同步方法。互不影响。
- 访问同一个对象的不同的普通同步方法。同一个锁。
- 同时访问静态synchronized和非静态synchronized方法。不是同一个锁。
- 方法抛异常后，会释放锁
  > lock的话，不会释放锁。

## 1.4. 性质：可重入，不可中断


- 可重入：指的是同一线程的外层函数获得锁之后，内层函数可以直接再次获取该锁
  - 好处：避免死锁，提升封装度。
- 不可中断：一旦这个锁已经被别人获得了，如果我还想获得，我只能选择等待或者阻塞，直到别的线程释放这个锁。如果别人永远不释放锁，那么我只能永远地等下去。
  - 相比之下，未来会介绍的Lock类，可以拥有中断的能力
  - 第一点，如果我觉得我等的时间太长了，有权中断现在已经获取到锁的线程的执行；
  - 第二点，如果我觉得我等待的时间太长了不想再等了，也可以退出。

---

- 粒度(scope)：加锁范围
  - 线程：如synchronized
    - 也就是说尽管一个线程持有锁
    - 但因为粒度是线程
    - 所以可以再次获得锁（可重入）
  - 调用：pthread
    - 一个线程获得了锁
    - 但因为粒度是调用，
    - 把锁给了该线程的该次调用。
    - 因此在不释放锁的情况下，是无法再次获得锁的(不可重入)

## 1.5. 原理

### 1.5.1. 加锁和释放锁

#### 1.5.1.1. 概述

JVM基于进入和退出Monitor对象来实现方法的同步和代码块同步。每个对象都有一个Monitor与之关联,当其被占用就会处于锁定的状态。

**Monitor并不是一个对象**，只是习惯了这样一个称呼，他被保存在**对象头的Mark Word**中。

在Java虚拟机（HotSpot）中，Monitor是由ObjectMonitor实现的。

#### 1.5.1.2. 代码块的同步

测试代码如下：

```java
public class SynchronizedTest {
    private void test2(){
        synchronized (this){
            System.out.println(Thread.currentThread().getName()+"获取锁"+this.toString());
        }
    }
}
```

查看编译后的字节码文件如下(省略部分内容)：

```
...
 2 astore_1
 3 monitorenter  
 4 getstatic #2 <java/lang/System.out>
 ....
38 invokevirtual #11 <java/io/PrintStream.println>
41 aload_1
42 monitorexit
43 goto 51 (+8)
46 astore_2
47 aload_1
48 monitorexit
...
```

在编译后的字节码文件中出现了monitorenter和monitorexit两个指令，作用如下：

- monitorenter指令会尝试获取monitor的所有权，即会尝试获取对象的锁(保存在对象头中)。过程如下：
  - 如果monitor的进入数位0，则该线程进入monitor，然后将进入数设置为1，该线程即为monitor的所有者。
  - 如果线程已经占有了该monitor，则是重新进入，将monitor的进入数加1.
  - 如果其他线程已经占有了monitor则该线程进入阻塞状态，直到monitor的进入数为0，再尝试获取monitor所有权

- monitorexit指令的执行线程必须是monitor的持有者。指令执行时monitor的进入数减1，如果减1后计数器为0，则该线程将不再持有这个monitor，其他被这个monitor阻塞的线程可以尝试去获取这个 monitor 的所有权。
  - monitorexit指令出现了两次，第1次为同步正常退出释放锁；第2次为**发生异步退出释放锁**；

Synchronized的底层是通过一个monitor的对象来完成，其实wait/notify等方法也依赖于monitor对象，
这就是为什么只有在同步的块或者方法中才能调用wait/notify等方法，否则会抛出java.lang.IllegalMonitorStateException的异常的原因。

#### 1.5.1.3. 同步方法的同步

源代码如下

```java
public class SynchronizedTest {
    public   synchronized  void test() {
        System.out.println(Thread.currentThread().getName()+"获取锁"+this.toString());
    }
}
```

编译后字节码文件如下(省略部分内容)：

```
public synchronized void test();
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_SYNCHRONIZED
    Code:
      stack=3, locals=1, args_size=1
...
```

对于同步方法，在字节码文件中没有使用monitorenter和monitorexit来完成同步(理论上可以)，但是多了一个ACC_SYNCHRONIZED的标记，
对于静态方法还会多出ACC_STATIC标记。JVM就是根据该标记来实现方法同步的。

当方法调用时，调用指令会检查方法的ACC_SYNCHRONIZED访问标记是否被设置，如果设置了执行线程将先法获取monitor，获取成功才能执行方法体，
方法体执行完成后释放monitor，在方法执行期间，任何一个其他的线程都无法再获取同一个monitor对象。

#### 1.5.1.4. 总结

两种同步方式本质上没有区别，只是方法的同步是一种隐式的方式来实现，无需通过字节码来完成。

两个指令的执行是JVM通过调用操作系统的互斥原语mutex来实现，

被阻塞的线程会被挂起、等待重新调度，会导致“用户态和内核态”两个态之间来回切换，对性能有较大影响。

### 1.5.2. 可重入

- JVM负责跟踪对象被加锁的次数
- 线程第一次给对象加锁的时候，计数变为1。每当这个相同的线程在此对象上再次获得锁时，计数会递增
- 每当任务离开时，计数递减，当计数为0的时候，锁被完全释放

### 1.5.3. 可见性保障

> 先了解JMM

一个代码块或者方法被synchronized修饰后，在执行完毕之后，被锁住的对象所做的任何修改都会在释放之前写会到主内存中。

## 1.6. 缺陷

> lock对下面的问题都有一定的处理措施。

- 效率低：锁的释放情况少、试图获得锁时不能设定超时、不能中断一个正在试图获得锁的线程
- 不够灵活（读写锁更灵活）:加锁和释放的时机单一，每个锁仅有单一的条件（某个对象）,可能是不够的
- 无法知道是否成功获取到锁

## 1.7. 常见问题

- 使用注意点：锁对象不能为空、作用域不宜过大、避免死锁

---

如何选择Lock和synchronized关键字？

> 尽量避免出错。

- 尽量不要使用lock和synchronized，使用java.util.concurrent包下的工具类。
- 相对于lock，优先使用synchronized，使用起来更简单。
- 当需要使用lock特性时，再使用lock

---

多线程访问同步方法的各种情况。

## 1.8. 思考题

竞争锁时的 锁调度机制。(不同JVM实现不同)

---

Synchronized使得同时只有一个线程可以执行，性能较差，有什么办法可以提升性能？

- 优化使用范围
- 使用读写锁

---

如何自己实现一个lock锁

---

锁的升级

# 2. 并发基础

## 2.1. 创建线程方式

### 2.1.1. 方式

> 准确地讲，创建线程只有一种方式，就是构造Thread类。儿实现线程的执行单元有两种方式
- 继承Thread类，并重写run方法；
- 实现Runnable接口的run方法；

### 2.1.2. 两者区别

> 通常优先使用“实现Runnable接口”这种方式来自定义线程类。
- 如果使用线程时不需要使用Thread类的诸多方法，显然使用Runnable接口更为轻量。
  > 使用Thread会建立一个独立的线程，而Runnable可以放入线程池，节约资源
- Runnable接口出现，降低了线程对象和线程任务的耦合性。
- 由于Java“单继承，多实现”的特性，Runnable接口使用起来比Thread更灵活。
- Runnable接口出现更符合面向对象，将线程单独进行对象的封装。

## 2.2. 启动线程

### 2.2.1. 启动方式：

- 错误方式：直接调用run()方法，会由main线程执行
- 正确方式：调用strat方法：会启动新线程

### 2.2.2. 源码解析

### 2.2.3. 面试题

```
一个线程两次调用start()方法会出现什么情况？为什么？

既然start()方法会调用run()方法，为什么我们选择调用
start()方法，而不是直接调用run()方法呢？
```



## 2.3. 停止线程

### 2.3.1. 原理介绍

- **使用Interrupt来通知，而不是强制**
  - 是一种合作机制，
  - 用一个线程通知一个线程停止工作
  - Java没有能力强行停止线程，是否停止交给线程本身判断。（通过isinterrupted方法判断是否有中断通知）
    > 原因：
    > 线程的调用者本身可能并不知道线程到底在干什么，
    > 因此把决定权交给线程本身。
    > 线程停止的时候可能会做一些扫尾工作

### 2.3.2. 最佳实践

- 代码执行过程中接受到interrupt通知
  - 代码执行过程中接受到interrupt通知，
  - 使用isinterrupted方法，判断是否有通知，跳转到return
  - 线程如果进行迭代运算的话，可以使用isinterrupted方法进行判断
- 线程在sleep等阻塞状态中接收到interrupt通知
  - 收到通知后，sleep方法会抛出异常
- 循环+阻塞的两种表现
    <details>
    <summary style="color:red;">情况1</summary>

    ```java
  /**
  * 描述：     try/catch里面放while，可以中断成功
  * 这种情况下，sleep会抛出异常，try/catch针对的是整个while，导致线程退出
  */
  public class RightWayStopThreadWithSleep {
      public static void main(String[] args) throws InterruptedException {
          Runnable runnable = () -> {
              int num = 0;
              try {
                  while (num <= 300 && !Thread.currentThread().isInterrupted()) {
                      if (num % 100 == 0) {
                          System.out.println(num + "是100的倍数");
                      }
                      num++;
                  }
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          };
          Thread thread = new Thread(runnable);
          thread.start();
          Thread.sleep(500);
          thread.interrupt();
      }
  }
    ```
    </details>

  <details>
  <summary style="color:red;">情况2</summary>

  ```java
  /**
  * 描述：     如果while里面放try/catch，会导致中断失效
  * try/catch针对的是循环循环的单个执行，之后依旧有循环要执行
  * **而抛出异常时，会把interrupt通知的标记位清除。**
  */
  public class CantInterrupt {
      public static void main(String[] args) throws InterruptedException {
          Runnable runnable = () -> {
              int num = 0;
              while (num <= 10000 && !Thread.currentThread().isInterrupted()) {
                  if (num % 100 == 0) {
                      System.out.println(num + "是100的倍数");
                  }
                  num++;
                  try {
                      Thread.sleep(10);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          };
          Thread thread = new Thread(runnable);
          thread.start();
          Thread.sleep(5000);
          thread.interrupt();
      }
  }
  ```
  </details>

#### 2.3.2.1. 通常的停止方式

#### 2.3.2.2. 正确方式interrupt方式

- 优先选择：传递中断。指编写函数调用接口时，要把Exception抛出，而调用代码中进行try/catch
  <details>
  <summary style="color:red;">代码</summary>

  ```java
  import threadcoreknowledge.createthreads.ThreadStyle;

  /**
  * 描述：     最佳实践：catch了InterruptedExcetion之后的优先选择：在方法签名中抛出异常 那么在run()就会强制try/catch
  */
  public class RightWayStopThreadInProd implements Runnable {

      @Override
      public void run() {
          while (true && !Thread.currentThread().isInterrupted()) {
              System.out.println("go");
              try {
                  throwInMethod();
              } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  //保存日志、停止程序
                  System.out.println("保存日志");
                  e.printStackTrace();
              }
          }
      }

      /**
      * 这里不能try/catch，
      * 否则在上面的while中，只会在一次执行时打印出异常信息， 剩下的照常执行
      *
      * 调用者也无法认知到异常的出现
      */
      private void throwInMethod() throws InterruptedException {
              Thread.sleep(2000);
      }

      public static void main(String[] args) throws InterruptedException {
          Thread thread = new Thread(new RightWayStopThreadInProd());
          thread.start();
          Thread.sleep(1000);
          thread.interrupt();
      }
  }
  ```
  </details>

- 不想或无法传递：恢复中断

- 不应屏蔽中断

### 2.3.3. 错误的停止方法

- 被弃用的stop,suspend和resume方法
- 用volatile设置标记位

### 2.3.4. 停止线程相关函数解析

- interupt方法原理
- 判断是否已经中断
  - static boolean interrupted()
  - boolean isinterrupted()

### 2.3.5. 常见概念

- JRE与JDK
- Java 7 和 Java SE 7
- JDK1.8和Java8

### 2.3.6. 面试题

```
如何停止一个线程
```

<br /><br />

```
如何处理不可中断的堵塞，（如抢锁时ReentrantLock.lock()或者SocketI/O时无法响应中断，那应该怎么让该线程）
```


## 2.4. 线程生命周期

### 2.4.1. 状态说明

- New:处于NEW状态的线程此时尚未启动
- Runnable:可以是等待cpu分配资源的状态，也可以是已经分配了资源正在运行中的状态
- Blocked:等待锁的释放以进入同步区域
- Waiting:没有设置
- Timed Waiting
- Terminated


### 2.4.2. 状态转化图示

![concorrence-1](./image/concorrence-1.png)

---

<details>
<summary style="color:red;">操作系统线程状态对比</summary>

![concorrence-5](./image/concorrence-5.png)
</details>

### 2.4.3. Java线程阻塞的代价

java的线程是映射到操作系统原生线程之上的，如果要阻塞或唤醒一个线程就需要操作系统介入，需要在户态与核心态之间切换，这种切换会消耗大量的系统资源，因为用户态与内核态都有各自专用的内存空间，专用的寄存器等，用户态切换至内核态需要传递给许多变量、参数给内核，内核也需要保护好用户态在切换时的一些寄存器值、变量等，以便内核态调用结束后切换回用户态继续工作。

如果线程状态切换是一个高频操作时，这将会消耗很多CPU处理时间；
如果对于那些需要同步的简单的代码块，获取锁挂起操作消耗的时间比用户代码执行的时间还要长，这种同步策略显然非常糟糕的。

synchronized会导致争用不到锁的线程进入阻塞状态，所以说它是java语言中一个重量级的同步操纵，被称为重量级锁，为了缓解上述性能问题，JVM从1.5开始，引入了轻量锁与偏向锁，默认启用了自旋锁，他们都属于乐观锁。

### 2.4.4. 阻塞状态

一般习惯而言，Blocked，Waiting，Timed Waiting三种情况都被称为阻塞状态

### 2.4.5. 面试题

```
Java线程有几种状态，生命周期是什么
```

## 2.5. Thread和Object相关重要方法

### 2.5.1. 方法概述

![concorrence-2](./image/concorrence-2.png)

### 2.5.2. wait,notify,notifyAll方法

- 三个方法都是必须获得monitor锁后才能调用(也就是说在synchronized内)
- notify只能唤醒一个线程
- 三个方法都属于object类
- 类似于Condition的功能

### 2.5.3. sleep方法

- 作用：
  - 进入TIMED_WAITING状态
  - 只让线程在预期的时间执行，其他时候不要占用CPU资源

- 特点：不释放锁,synchronized和lock锁都不会释放

- sleep响应中断：
  > 前面也有提
  - 抛出InterruptedException
  - 清除中断状态

- 推荐写法：
  > 底层依旧是sleep方法
  ```java
  TimeUnit.SECONDS.sleep(1);
  ```
  - 支持秒，小时等单位。更容易阅读
  - 参数小于0也不会抛出异常
    > sleep参数小于0会抛异常

### 2.5.4. join 方法

- 作用：
  - 因为新的线程加入了我们，所以我们要等他执行完再出发
  - main等待thread1执行完毕，注意谁等谁
- 遇见中断
  <details>
  <summary style="color:red;">测试代码</summary>

  ```java
  /**
  * 描述：     演示join期间被中断的效果
  */
  public class JoinInterrupt {
      public static void main(String[] args) {
          Thread mainThread = Thread.currentThread();
          Thread thread1 = new Thread(new Runnable() {
              @Override
              public void run() {
                  try {
                      mainThread.interrupt();
                      Thread.sleep(5000);
                      System.out.println("Thread1 finished.");
                  } catch (InterruptedException e) {
                      System.out.println("子线程中断");
                  }
              }
          });
          thread1.start();
          System.out.println("等待子线程运行完毕");
          try {
              thread1.join();
          } catch (InterruptedException e) {
              System.out.println(Thread.currentThread().getName()+"主线程中断了");
              thread1.interrupt();
          }
          System.out.println("子线程已运行完毕");
      }
  }
  ```

  ![concorrence-3](./image/concorrence-3.png)
  </details>

  - main线程等待thread-0运行完毕
  - thread-0的run方法中调用mian线程引用的interrupt方法
  - 主线程抛出异常，中断。作为解决方式，也将thread-0中断
  - 此时两个catch块中的代码是并行执行的

- join期间main方法的状态：**Waiting**

---

- join原理以及等价java代码

  !! 带补 !!

### 2.5.5. yield方法

- 作用：
  - 释放CPU时间片
  - 但不会释放自己的锁
- 定位；
  - JVM不保证遵循。
  - 实际开发中较少使用
  - （但是AQS等中使用较多）
- yield和sleep区别：
  - sleep后，线程会进入阻塞状态TIMED_WAITING
  - 但是调用yield方法后，依旧是Runnable状态

### 2.5.6. start和run

### 2.5.7. stop,suspend,resume方法

### 2.5.8. 面试题

```
为什么线程通信的方法wait(),notify()和notifyAll()被定义在
Object类里？而sleep定义在Thread类里？

如果调用Thread.wait会怎样
```

wait(),notify(),notifyAll()是锁级别的操作，而锁是属于对象的。而一个线程可以持有多把锁。

如果把这些方法定义在Thread类中，就无法实现以上逻辑

<br /><br />

线程退出时，会自动执行notify，会影响整体流程，因此不要使用Thread类作为锁对象。

---

```
用3种方式实现生产者模式
```

---

```
Java SE 8和Java 1.8和JDK 8是什么关系，是同一个东西吗？
```

---

```
Join和sleep和wait期间线程的状态分别是什么？为什么？
```

---

```
为什么wait()需要在同步代码块中使用，而sleep不需要
```
为了让通信变得可靠，防止死锁以及永久等待的发生。因为wait后，必须通过notify唤醒。线程间必须配合执行。

sleep基本就是作用于本身线程，和其他线程基本没有什么关系。

---

```
wait/notify,sleep异同
```

- 相同：
  - 阻塞
  - 响应中断
- 不同
  - wait/notify要在同步方法中
    > 原因说一下
  - wait/notify会释放锁
  - sleep必须指定时间，wait可以不指定时间
  - 所属类
    > 原因说一下

---

```
手写生产者，消费者模式
```

<details>
<summary style="color:red;">代码</summary>

```java
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：     用wait/notify来实现生产者消费者模式
 */
public class ProducerConsumerModel {
    public static void main(String[] args) {
        EventStorage eventStorage = new EventStorage();
        Producer producer = new Producer(eventStorage);
        Consumer consumer = new Consumer(eventStorage);
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}

class Producer implements Runnable {

    private EventStorage storage;

    public Producer(
            EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            storage.put();
        }
    }
}

class Consumer implements Runnable {

    private EventStorage storage;

    public Consumer(
            EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            storage.take();
        }
    }
}

class EventStorage {

    private int maxSize;
    private LinkedList<Date> storage;

    public EventStorage() {
        maxSize = 10;
        storage = new LinkedList<>();
    }

    public synchronized void put() {
        while (storage.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(new Date());
        System.out.println("仓库里有了" + storage.size() + "个产品。");
        notify();
    }

    public synchronized void take() {
        while (storage.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("拿到了" + storage.poll() + "，现在仓库还剩下" + storage.size());
        notify();
    }
}
```
</details>

---

```
一个线程只打印奇数，一个线程只打印偶数，交替打印
```

 <details>
 <summary style="color:red;">代码</summary>

 ```java
/**
 * 描述：     两个线程交替打印0~100的奇偶数，用wait和notify
 */
public class WaitNotifyPrintOddEveWait {

    private static int count = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(new TurningRunner(), "偶数").start();
        new Thread(new TurningRunner(), "奇数").start();
    }

    //1. 拿到锁，我们就打印
    //2. 打印完，唤醒其他线程，自己就休眠
    static class TurningRunner implements Runnable {

        @Override
        public void run() {
            while (count <= 100) {
                synchronized (lock) {
                    //拿到锁就打印
                    System.out.println(Thread.currentThread().getName() + ":" + count++);
                    lock.notify();
                    if (count <= 100) {
                        try {
                            //如果任务还没结束，就让出当前的锁，并休眠
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
 ```
 </details>

## 2.6. 线程属性

### 2.6.1. 总览

![concorrence-4](./image/concorrence-4.png)

### 2.6.2. id

### 2.6.3. 名称

### 2.6.4. 守护线程

### 2.6.5. 线程优先级

### 2.6.6. 各属性总结

### 2.6.7. 面试题

```
什么时候我们需要设置守护线程？
```

---

```
我们应该如何应用线程优先级来帮助程序运行？有哪些禁忌？
```

---

```
不同的操作系统如何处理优先级问题？
```


## 2.7. 线程的未捕获异常

## 2.8. 多线程导致的问题

### 2.8.1. 线程安全

### 2.8.2. 性能问题

# 3. Java 内存模型

## 3.1. 三大结构模型

## 3.2. JMM 是什么

## 3.3. 重排序

## 3.4. 可见性

## 3.5. JMM 的抽象：本地内存和主内存

## 3.6. happens-before 原则

## 3.7. volatile 关键字

## 3.8. 并发编程三大特性

### 3.8.1. 可见性

### 3.8.2. 原子性

### 3.8.3. 有序性

# 4. 死锁

# 5. 常见问题


