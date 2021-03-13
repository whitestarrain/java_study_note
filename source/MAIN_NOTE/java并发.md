# 线程 8 大核心基础

## 实现多线程

- 方式
  > 准确地讲，创建线程只有一种方式，就是构造Thread类。儿实现线程的执行单元有两种方式
  - 继承Thread类，并重写run方法；
  - 实现Runnable接口的run方法；

- 两者区别
  > 通常优先使用“实现Runnable接口”这种方式来自定义线程类。
  - 如果使用线程时不需要使用Thread类的诸多方法，显然使用Runnable接口更为轻量。
    > 使用Thread会建立一个独立的线程，而Runnable可以放入线程池，节约资源
  - Runnable接口出现，降低了线程对象和线程任务的耦合性。
  - 由于Java“单继承，多实现”的特性，Runnable接口使用起来比Thread更灵活。
  - Runnable接口出现更符合面向对象，将线程单独进行对象的封装。

## 启动线程

### 启动方式：

- 错误方式：直接调用run()方法，会由main线程执行
- 正确方式：调用strat方法：会启动新线程

### 源码解析

### 面试题

```
一个线程两次调用start()方法会出现什么情况？为什么？

既然start()方法会调用run()方法，为什么我们选择调用
start()方法，而不是直接调用run()方法呢？
```



## 停止线程

### 原理介绍

- **使用Interrupt来通知，而不是强制**
  - 是一种合作机制，
  - 用一个线程通知一个线程停止工作
  - Java没有能力强行停止线程，是否停止交给线程本身判断。（通过isinterrupted方法判断是否有中断通知）
    > 原因：
    > 线程的调用者本身可能并不知道线程到底在干什么，
    > 因此把决定权交给线程本身。
    > 线程停止的时候可能会做一些扫尾工作

### 最佳实践

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

#### 通常的停止方式

#### 正确方式interrupt方式

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

### 错误的停止方法

- 被弃用的stop,suspend和resume方法
- 用volatile设置标记位

### 停止线程相关函数解析

- interupt方法原理
- 判断是否已经中断
  - static boolean interrupted()
  - boolean isinterrupted()

### 常见概念

- JRE与JDK
- Java 7 和 Java SE 7
- JDK1.8和Java8

### 面试题

```
如何停止一个线程
```

<br /><br />

```
如何处理不可中断的堵塞，（如抢锁时ReentrantLock.lock()或者SocketI/O时无法响应中断，那应该怎么让该线程）
```


## 线程生命周期

## Thread和Object相关重要方法

## 线程属性

## 线程的未捕获异常

## 多线程导致的问题

### 线程安全

### 性能问题

# Java 内存模型

## 三大结构模型

## JMM 是什么

## 重排序

## 可见性

## JMM 的抽象：本地内存和主内存

## happens-before 原则

## volatile 关键字

## 并发编程三大特性

### 可见性

### 原子性

### 有序性

# 死锁

# 常见问题


