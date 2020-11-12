# JVM 与 java 体系结构

## 基础知识

- java与jvm
  - java：跨平台的语言
    > ![jvm-1.1-1](./image/jvm-1.1-1.png)
    - 一次编译，到处运行。
  - jvm：跨语言的平台
    > ![jvm-1.1-2](./image/jvm-1.1-2.png)
    - jvm 只关心字节码文件
    - 不局限于编程语言
    - 只要提供编译器，可以编译为字节码文件，便可在虚拟机上运行

- 多语言混合编程趋势：
  - java 平台上多语言混合编程成为趋势
  - 通过特定领域的语言解决特定领域的问题
    - 并行处理；Cloure
    - 展示层；JRuby/Rails
    - 中间层：java
  - 各语言间因为运行在同一个虚拟机上，交互十分方便


- 字节码：
  - 不同语言的不同编译器，可能编译出相同的字节码文件
  - jvm 和 java 语言没有必然联系，它只与特定二进制文件 Class 文件格式所关联
  - Class 文件中包含了 java 虚拟机指令集(或称为字节码，Bytecodes)和符号表，还有一些其他辅助信息

- java 历史事件

  > ![java-histroy-1](./image/java-histroy-1.png)
  > ![java-history-2](./image/ java-history-2.png)

## 架构相关

- 虚拟机：
  - 系统虚拟机，比如 vmware。是对物理计算机的仿真，提供了一个可运行完整操作系统的平台
  - 程序虚拟机：比如 jvm。专门为执行单个计算机程序而设计。java 虚拟机中的指令为 java 字节码指令

- java 虚拟机
  - 说明：
    - 运行 java 字节码的虚拟机，拥有独立的运行机制。java 字节码不一定由 java 语言编译而成
    - jvm 平台各种语言都可以共享 java 虚拟机带来的跨平台性，垃圾回收机制以及各种可靠的即时编译器
    - java 技术的核心就是 java 虚拟机。所有 java 程序都运行在 java 虚拟机内部
  - 作用
    - java 虚拟机就是二进制字节码的运行环境。负责装在字节码到虚拟机内部，再解释/编译为 对应平台上的机器指令执行
  - 特点：
    - 一次编译，到处运行
    - 自动内存管理
    - 自动垃圾回收
  - 位置：
    > ![jvm-position](./image/jvm-position.png)
  - 整体结构:
    > ![jvm-sturction](./image/jvm-sturction.png)
    - 上层：
      - 将字节码文件通过 Class Loader 加载到 jvm 内存中
      - 生成一个 class 对象
    - 中层：
      > Runtime 类。（单例模式）
      - 运行时数据区。
      - 方法区和堆多线程共享
      - 其他的各自有一份
    - 下层：执行引擎
      > 解释执行(编译器)和即时编译(JIT编译器)并存
      - 解释器:保证响应时间，逐行对字节码指令进行解释执行
      - JIT 编译器：针对人点代码，将热点代码编译为机器代码，并缓存起来
        - java-->class file:编译器前端
        - class 字节码-->机器指令:编译器后端
      - 垃圾回收器

- java执行流程
  > ![java-flow](./image/java-flow.png)  

- jvm架构模型
  > Java编译器输入的指令流基本上是一种基于栈的指令集架构，另外一种指令集架构则 是基于寄存器的指令集架构。
  - 基于栈式架构的特点
    > HotSpot就是基于栈
    - 占用资源少。设计和实现更简单，适用于资源受限的系统；
    - 避开了寄存器的分配难题：使用零地址指令方式分配。
    - 指令流中的指令大部分是零地址指令，其执行过程依赖于操作栈。指令集更小，编译器容易实现。
    - **不需要硬件支持，可移植性更好，更好实现跨平台**
    - 性能差一些
  - 基于寄存器架构的特点
    > 比如传统的PC以及Android的Davlik虚拟机。
    - 典型的应用是x86的二进制指令集
    - 指令集架构则**完全依赖硬件，可移植性差**
    - **性能优秀和执行更高效**；
    - 花费更少的指令去完成一项操作。
    - 在大部分情况下，基于寄存器架构的指令集往往都以一地址指令、二地址指令 和三地址指令为主，而基于栈式架构的指令集却是以零地址指令为主。为难学的

## jvm生命周期

- 启动：
  - 通过引导类加载器(bootstrap class loader)创建一个初始类(initial class)来完成。
  - 不同的虚拟机该类有不同的实现
- 执行
  - 程序执行的时候jvm才执行，程序结束jvm也结束
  - 执行一个java程序的时候，真正执行的是 java虚拟机 这个进程
- 退出
  - 程序正常结束
  - 遇到异常或错误
  - 操作系统错误
  - System.exit。Runtime.halt
  - JNI(java Native Interface)加载和卸载java虚拟机

## 历史版本虚拟机

> 详细看pdf课本

> 具体JVM的内存结构，其实取决于其实现，不同厂商的JVM,或
> 者同一厂商发布的不同版本，都有可能存在一定差异。
> 此处主要以Oracle HotSpot VM为默认虚拟机。

- Sun Classic VM
  - 早在1996年Java1.0版本的时候，Sun公司发布了一款名为Sun Classic VM的Java虚拟机，它同时也是世界上第一款商用Java虚拟机，JDK1.4时 完全被淘汰。
  - 这款虚拟机内部只提供解释器。
  - 如果使用JIT编译器，就需要进行外挂。但是一旦使用了JIT编译器，JIT就 会接管虚拟机的执行系统。解释器就不再工作。解释器和编译器不能配合工 作。
  - 现在hotspot内置了此虚拟机。

- Exact VM
  - 为了解决上一个虚拟机问题，jdk1.2时，sun提供了此虚拟机。
  - Exact Memory Management:准确式内存管理
    - 也可以叫Non-Conservative/Accurate Memory Management
    - 虚拟机可以知道内存中某个位置的数据具体是什么类型。
  - 具备现代高性能虚拟机的维形
    - 热点探测
    - 编译器与解释器混合工作模式
  - 只在Solaris平台短暂用，其他平台上还是classic vm
    - 但也没用在其他平台，终被Hotspot虚拟机替换


- HotSpot VM
  > 商用三大虚拟机之一
  - 历史
    - 最初由一家名为“Longview Technologies"的小公司设计
    - 1997年，此公司被Sun收购；2009年，Sun公司被甲骨文收购。
    - JDK1.3时，HotSpot VM成为默认虚拟机
  - 目前Hotspot占有绝对的市场地位，称霸武林。
    - 不管是现在仍在广泛使用的JDK6,还是使用比例较多的JDK8中，默认的虚拟机都是 HotSpot
    - Sun/Oracle JDK和OpenJDK的默认虚拟机
    - 因此本课程中默认介绍的虚拟机都是HotSpot,相关机制也主要是指HotSpot的GC机 制。（比如其他两个商用虚拟机都没有方法区的概念）
  - 从服务器、桌面到移动端、嵌入式都有应用。
  - 名称中的HotSpot指的就是它的热点代码探测技术。
    - 通过计数器找到最具编译价值代码，触发即时编译或栈上替换
    - 通过编译器与解释器协同工作，在最优化的程序响应时间与最佳执行性能中取得平衡

- BEA的JRockit
  > 商用三大虚拟机之一
- 专注于服务器端应用
  - 它可以**不太关注程序启动速度**，因此JRockit内部不包含解析器实现，全部代码 **都靠即时编译器**编译后执行。
- 大量的行业基准测试显示，JRockit JVM是世界上最快的JVM。
  - 使用JRockit产品，客户已经体验到了显著的性能提高（一些超过了70号）和
  - 硬件成本的减少（达50%)。
- 优势：全面的Java运行时解决方案组合
  - JRockit面向延迟敏感型应用的解决方案JRockit Real Time提供以毫秒或 微秒级的JVM响应时间，适合财务、军事指挥、电信网络的需要
  - MissionControl服务套件，它是一组以极低的开销来监控、管理和分析生产环境中的应用程序的工具。
- 2008年，BEA被Oracle收购。
- Oracle表达了整合两大优秀虚拟机的工作，大致在JDK8中完成。
  - 整合的方式是在 HotSpot的基础上，移植JRockit的优秀特性。
  - 两者架构差别很大，整合有限
  - 但JRockit占优势地位

- IBM的J9
  > 商用三大虚拟机之一
  - 全称：IBM Technology for Java Virtual Machine,简称IT4J,内 部代号：J9
  - 市场定位与HotSpot接近，服务器端、桌面应用、嵌入式等多用途VM
  - 广泛用于IBM的各种Java产品。
  - 目前，有影响力的三大商用虚拟机之一，也号称是世界上最快的Java虚拟机(主要是针对在IBM自己产品和平台上)
  - 2017年左右，IBM发布了开源J9 VM,命名为OpenJ9,交给Eclipse基金 会管理，也称为Eclipse OpenJ9

- KVM和CDC/CLDC HotSpot
  - Oracle在Java ME产品线上的两款虚拟机为：CDC/CLDC HotSpot Implementation VM
  - KVM(Kilobyte)是CLDC-HI早期产品
  - 目前移动领域地位尴尬，智能手机被Android和ios二分天下。
  - KVM简单、轻量、高度可移植，面向更低端的设备上还维持自己的一片 市场
    - 智能控制器、传感器
    - 老人手机、经济欠发达地区的功能手机

- Azul VM
  > 高性能Java虚拟机
  - 前面三大“高性能Java虚拟机”使用在通用硬件平台上
  - 这里Azul VM和BEA Liquid VM是与**特定硬件平台绑定、软硬件配合的专有虚拟机**
  - Azul VM是Azul systems公司在HotSpot基础上进行大量改进，运行于 Azul systems公司的专有硬件Vega系统上的Java虚拟机。
  - **每个Azu1 VM实例都可以管理至少数十个CPU和数百GB内存的硬件资源，并 提供在巨大内存范围内实现可控的GC时间的垃圾收集器、专有硬件优化的线 程调度等优秀特性。**
  - 2010年，Azul Systems公司开始从硬件转向软件，发布了自己的Zing JVM,可以在通用x86平台上提供接近于Vega系统的特性。

- Liquid VM
  > 高性能Java虚拟机中的战斗机。
  - BEA公司开发的，直接运行在自家Hypervisor系统上
  - Liquid VM即是现在的JRockit VE(Virtual Edition),Liquid VM不需要操作系统的支持，或者说它自己本身实现了一个专用操作系统 的必要功能，如线程调度、文件系统、网络支持等。
    > 避免了用户态和内核态的切换，大大提高了执行速度 
  - 随着JRockit虚拟机终止开发，Liquid VM项目也停止了。

- Apache Harmony
  - Apache也曾经推出过与JDK1.5和JDK1.6兼容的Java运行平台 Apache Harmony。
  - 它是IBM和Inte1联合开发的开源JVM,受到同样开源的OpenJDK的压制， Sun坚决不让Harmony获得JCP认证，最终于2011年退役，IBM转而参与 OpenJDK
  - 虽然目前并没有Apache Harmony被大规模商用的案例，但是它的Java 类库代码吸纳进了Android SDK。

- Microsoft VM
  - 微软为了在IE3浏览器中支持Java Applets,开发了Microsoft JVM。
  - 只能在window平台下运行。但确是当时Windows下性能最好的Java VM。
  - 1997年，Sun以侵犯商标、不正当竞争罪名指控微软成功，赔了sun很多 钱。微软在WindowsXP SP3中抹掉了其VM。现在windows上安装的jdk 都是HotSpot。


- Taobao JVM
  - 由AliJVM团队发布。阿里，国内使用Java最强大的公司，覆盖云计算、金融、物流、 电商等众多领域，需要解决高并发、高可用、分布式的复合问题。有大量的开源产品。
  - 基于OpenJDK开发了自己的定制版本AlibabaJDK,简称AJDK。是整个阿里Java体 系的基石。
  - 基于OpenJDK HotSpot VM发布的国内第一个优化、深度定制且开源的高性能服务器 版Java虚拟机。
    - 创新的GCIH(GC invisible heap)技术实现了off-heap,即将生命周期 较长的Java对象从heap中移到heap之外，并且GC不能管理GCIH内部的Java对 象，以此达到降低GC的回收频率和提升GC的回收效率的目的。
    - GCIH中的对象还能够在多个Java虚拟机进程中实现共享
    - 使用crc32指令实现JVMintrinsic降低JNI的调用开销
    - PMU hardware 的Java profiling tool 和诊断协助功能
    - 针对大数据场景的ZenGC
  - taobao vm应用在阿里产品上性能高，硬件严重依赖intel的cpu,损失了兼容性，但提高了性能
    - 目前已经在淘宝、天猫上线，把oracle官方JVM版本全部替换了。

- Dalvik VM
  - 谷歌开发的，应用于Android系统，并在Android2.2中提供了JIT,发展迅猛。
  - Dalvik VM只能称作虚拟机，而**不能称作Java虚拟机**，它没有遵循Java 虚拟机规范
  - 不能直接执行 Java 的 Class 文件
  - 基于**寄存器架构**，不是jvm的栈架构。
  - 执行的是编译以后的dex(Dalvik Executable)文件。执行效率比较高。
    - 它执行的dex(Dalvik Executable)文件可以通过class文件转化而来， 使用Java语法编写应用程序，可以直接使用大部分的Java API等。
  - Android 5.0 使用支持提前编译（Ahead Of Time Compilation,AOT)的 ART VM替换Dalvik VM。

- Graal VM
  > 最有可能替代HotSpot的虚拟机，现在依旧处于实验性质

# 内存与垃圾回收

## 上层 类加载子系统

> 注意，详细内容会放在字节码与类的加载篇，现在首先有个初步认识

### 架构回顾

- 架构简图：
  > ![jvm-structure-simple](./image/jvm-structure-simple.png)
  - 第一部就是通过Class Loader 将类加载到内存中
- 架构详细图：
  > ![jvm-structure-pro](./image/jvm-structure-pro.png) 
  - 类加载环节：
    >  注意，上面加载是指整个宏观过程。只是下面三步中，第一步也叫加载
    - 加载。三个类加载器
      - 引导类加载器
      - 扩展类加载器
      - 系统类加载器
    - 链接
      - 验证
      - 准备
      - 解析
    - 初始化
  - 加载到内存后的运行时数据区
    - 每个线程一个pc寄存器
    - 每个线程一个栈。栈由多个栈帧组成
    - 堆：共享，最大的一块空间
    - 方法区：存放类的信息，常量，方法信息等
      > 只有hotspot有
  - 执行引擎
    - 解释器:保证响应时间，逐行对字节码指令进行解释执行
    - JIT 即时编译器：针对人点代码，将热点代码编译为机器代码，并缓存起来
    - 垃圾回收器

### 作用

![class-loader-1](./image/class-loader-1.png)

- 加载源：
  - 本地文件系统
  - 网络

- 类加载器子系统负责从文件系统或者网络中加载Class文件。**class文件在文件开头有特定的文件标识。**
  - java规范中有规定文件标识
  - 在验证阶段进行验证
- ClassLoader只负责class文件的加载，至于它是否可以运行，则由Execution Engine决定。
- 加载的类信息存放于一块称为方法区的内存空间。
  - 除了类的信息外，方法区中还会存放运行时常量池信息
    - 运行时常量池对应class中的常量池
    - javap 反编译 class可以发现常量池:
      >![pool](./image/pool.png) 
    - 运行时会把常量池加载到内存，成为运行时常量池
  - 可能还包括字符串字面量和数字常量（这部分常量信息是Class文件中常量池部分的内存映射）

### 类加载器充当的角色

![class-loader-2](./image/class-loader-2.png)

- Car.class 字节码文件保存在硬盘上
- 通过类加载器可以加载到内存中，存放在方法区中。
  - 其中加载包括三部分（上面架构图中的），之后会具体讲
- 此时加载到内存的为 **DNA元数据模版**,即内存中的Car Class
  - Car Class 调用 getClassLoader方法可以获取类加载器
- 调用 Car Class 的构造方法可以在堆空间中创建对象
  - 对象调用 getClass 方法，可以获取 DNA元数据模版，即类本身

### 类的加载过程※

#### 大致图解

![classloader-load](./image/classloader-load.png)

#### loading

- 说明：
  - 通过一个类的全限定类名获取定义此类的二进制字节流
  - 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构
  - **在内存中生成一个代表这个类的java.lang.Class对象**，作为方法区这个类的各种数据的访问入口

- 补充：加载.class文件的方式
  - 从本地系统中直接加载
  - 通过网络获取，典型场景：Web Applet
  - 从zip压缩包中读取，成为日后jar、war格式的基础
  - 运行时计算生成，使用最多的是：动态代理技术
  - 由其他文件生成，典型场景：JSP应用
  - 从专有数据库中提取.class文件，比较少见
  - 从加密文件中获取，典型的防class文件被反编译的保护措施

#### linking

- 验证（Verify):
  - 目的在于确保class文件的字节流中包含信息符合当前虚拟机要求，保证被加载类的正确性， 不会危害虚拟机自身安全。
    - 使用 BinaryViewer打开可以发现，
    - 给java虚拟机使用的class文件开头都为 **CA FE BA BE**
      > ![linking-1](./image/linking-1.png) 
  - 主要包括四种验证，文件格式验证，元数据验证，字节码验证，符号引用验证。
- 准备（Prepare):
  - 为**类变量**分配内存并且设置该类变量的默认初始值，即零值。
  - 这里**不包含用final修饰的static**,因为**final在编译的时候就会分配了**，**准备阶段会显式初始化**；
  - **这里不会为实例变量分配初始化**，
    - 类变量会分配在方法区中
    - 而实例变量是会随着对象一起分配到Java堆中。
  - 例子：
    ```
    private static int a = 1

    准备阶段会为 a 开辟内存，然后并赋值为0
    在initial阶段才会赋值为1
    ```
- 解析（Resolve):
  > 该部分内容放到 字节码与类的加载 一章进行讲解
  - 事实上，**解析操作往往会伴随着JVM在执行完初始化之后再执行**。
  - 将常量池内的符号引用转换为直接引用的过程。
    - 符号引用就是一组符号来描述所引用的目标。符号引用的字面量形式明确定义在《java虚拟机 规范》的class文件格式中。
    - 直接引用就是直接指向目标的指针、相对偏移量或一个间接定位 到目标的句柄。
  - 解析动作主要针对类或接口、字段、类方法、接口方法、方法类型等。对应常量池中的 CONSTANT_Class_info、CONSTANT_Fieldref_info、CONSTANT_Methodref_info等。

#### initial

- 过程
  - 初始化阶段就是执行类构造器方法<clinit>()的过程。
    - 此方法不需定义，
    - 是javac编译器自动收集
      - **类中的所有类变量的赋值动作**
      - **类中的所有静态代码块中的语句**
    - 然后合并
    - 如果没有静态变量和静态代码块，该方法就不会存在
  - 构造器方法中指令按语句在源文件中出现的顺序执行。
  - <clinit>()不同于类的构造器。
    - 构造器是<init>
  - 若该类具有父类，JVM会保证子类的<clinit>()执行前，父类的<clinit>() 已经执行完毕。
  - 虚拟机必须保证一个类的<clinit>()方法在多线程下被同步加锁。
    - 一个类的<clinit>()只会被加载一次
    - 在加载过程中会加锁
    - 应该注意，避免导致死锁

- 测试
  - 使用 jclasslib 或者idea中的该插件 打开字节码文件
    ```java
    public class ClassInitTest{
      public static int a = 1;
      static{
        a = 2
        b = 20
        // System.out.println(b)  //报错，注意，可以赋值但不可以调用
      }
      public static int b = 10;// linking中已经给 b 赋值为0，已经被加载到内存
                                // 然后initial阶段，按代码上下顺序执行代码
                                // 因为上面的static，会赋值为20
                                // 再因为该条语句，会赋值为10
                                // 可以想成：
                                  // linking阶段:  int b = 0
                                  // initial阶段: b = 20; b=10
                                  // 具体用jclasslib看更清晰
      public static void main(){
        System.out.println(Hello.a);
      }
    }
    ```
  - 左侧 Methods 下有 <clinit>，可以自行查看
    > ![classloader-init-1](./image/classloader-init-1.png) 

### 类加载器

#### 分类

- 通常会分成三个类加载器：
  - BootStrap ClassLoader
  - Extension  ClassLoader
  - AppClassLoader  ClassLoader
- 但从java虚拟机规范上讲。JVM支持两种类加载器：
  - 引导类加载器（Bootstrap ClassLoader）
    - c/c++编写
  - 自定义类加载器（User-Defined ClassLoader）
    > java编写
    - 从概念上将，自定义类加载器为开发人员自定义的一类类加载器
    - 但是java规范上定义为：所有派生于抽象类ClassLoader的类加载器都为自定义类加载器
      > ![classloader-kind](./image/classloader-kind.png) 


- 一些类加载器的继承关系
  > ![classloader-kind-2](./image/classloader-kind-2.png) 
  > 自定义类加载器是无法获取到核心类加载器（Bootstrap ClassLoader）的
  > ![classloader-kind-3](./image/classloader-kind-3.png) 
  > ![classloader-kind-4](./image/classloader-kind-4.png) 

#### 常见的三个

##### BootStrap ClassLoader

- 启动类加载器（引导类加载器，Bootstrap ClassLoader)
  - 编写：这个类加载使用**C/C++语言**实现的，嵌套在JVM内部。
  - 父类：并不继承自java.lang.ClassLoader,没有父加载器。
  - 子类：加载扩展类和应用程序类加载器，并指定为他们的父类加载器。
  - 加载：它用来加载Java的核心库（`JAVA_HOME/jre/lib/rt.jar、 resources.jar或sun.boot.class.path`路径下的内容）,用于提供 JVM自身需要的类
    - 出于安全考虑，Bootstrap启动类加载器只加载包名为java、javax、 sun等开头的类
    - 获取能够加载api的路径：
      ```java
      sun.misc.Launcher.getBootstrapClassPath().getURLs();

      // 随便找一个输出目录中的类，查看类的加载器
      System.out.println(provider.class.getClassLoader());// null 。引导类加载器用c/c++编写，获取不到
      // java.lang.String 获取的也是null
      ```

##### Extension ClassLoader


- 扩展类加载器
  - 编写：**Java语言编写**，由`sun.misc.Launcher$ExtClassLoader`实现
  - 父类：**派生于ClassLoader类**。 父类加载器为启动类加载器
  - 加载：从java.ext.dirs系统属性所指定的目录中加载类库，
    - 或从JDK的安装目录的`jre/lib/ext`子目录（扩展目录）下加载类库。
    - **如果用户创建的JAR放在此目录下，也会自动由扩展类加载器加载**。
    - 获取扩展类加载器加载的api
      ```java
      System.getProperty("java.ext.dirs").split(";");

      // 随便找一个输出目录中的类，查看类的加载器
      System.out.println(CurveDB.class.getClassLoader()); // 获得扩展类加载器
      ```

##### AppClassLoader

- 应用程序类加载器（系统类加载器，AppClassLoader)
  - 编写：java语言编写，由`sun.misc.Launcher$AppClassLoader`实现
  - 父类：派生于ClassLoader类。 父类加载器为扩展类加载器
  - 加载：它负责加载环境变量classpath或系统属性java.class.path指 定路径下的类库
  - 其他：
    - 该类加载是程序中默认的类加载器，一般来说，Java应用的类都是由它来完成加载
    - 通过ClassLoader.getSystemClassLoader()方法可以获取到该类加载器

#### 自定义类加载器

- 为什么要自定义类加载器
  - 隔离加载类
    > 主流框架都自定义了类加载器
    - 比如在使用某些框架时,需要使用中间件
    - 为了避免jar包冲突(比如全类名相同)
    - 所以要将中间件和应用模块隔离
  - 修改类的加载方式
    - BootStrap ClassLoader 必须要加载
    - 但其他类加载器加载的api并不一定必须
    - 使用自定义类加载器可以在需要的时候实现动态加载
  - 扩展加载源
    - 比如从数据库中加载
  - 防止源码泄漏
    - 加密字节码文件
    - 为了解密，就需要自定义类加载器

- 步骤简介
  > 具体过程在第二篇
  1. 开发人员可以通过继承抽象类java.lang.ClassLoader类的方式，实现 自己的类加载器，以满足一些特殊的需求
  2. 在JDK1.2之前，在自定义类加载器时，总会去继承ClassLoader类并重 写1oadClass()方法，从而实现自定义的类加载类，但是在JDK1.2之后 已不再建议用户去覆盖1oadClass()方法，而是建议把自定义的类加载逻 辑写在findClass()方法中
  3. 在编写自定义类加载器时，如果没有太过于复杂的需求，可以直接继承 URLClassLoader类，这样就可以避免自己去编写findClass()方法及 其获取字节码流的方式，使自定义类加载器编写更加简洁。


#### 抽象类ClassLoader

- 简介：除了Bootstrap ClassLoader，所有的类加载器都继承该类
- 方法：
  > ![classloader-methods](./image/classloader-methods.png) 
  - 这些方法都不是抽象方法

- 继承结构
  > ![classloader-inherit](./image/classloader-inherit.png) 
  
- sun.misc.Launcher 是一个虚拟机的入口应用 
  - 如：
  - `sun.misc.Launcher$ExtClassLoader`
  - `sun.misc.Launcher$AppClassLoader`

- 获取classloader的方式
  - class.getClassLoader()
    > 获取指定类的类加载器
  - Thread.currentThread().getContextClassLoader()
    > 获取当前线程执行的main方法所在类的类加载器
  - ClassLoader.getSystemClassLoader()
    > 获取系统的类加载器
  - DriverManager.getCallerClassLoader()
    > 获取调用者的类加载器 

### 双亲委派机制※

#### 原理

> 面试经常被问

![parent-class-loader](./image/parent-class-loader.png)


- java虚拟机对class文件加载方式：
  - 按需加载
    - 当需要时才会将class文件加载到内存生成class对象
  - 加载某个类时，使用的是双亲委派模式
    - 即把请求交由父类处理
    - 是一种任务委派模式

- 问题引入：
  - 问题：在工程下创建`java.lang.String`类。 那么加载的是自定义的String，还是java自带的String。
  - 结果：java自带的String

- 解释：
  - 如果一个类加载器收到了类加载请求，
  - 它不会自己先去加载
  - 而是请求向上委托给父类的加载器去执行
  - 如果父类加载器也存在父类加载器
  - 就会一直向上委派
  - 直到到达最顶层的启动类加载器
  - 判断
    - 如果父类加载器可以完成类的加载任务
      - 就成功返回
    - 如果不能正常完成
      - 子加载器才会向下依次尝试自己加载

- 示例1：
  - 情景：
    - 创建一个工程，新建java.lang.String类
    - 在里面写main方法，执行的话会报错。
      > 在类java.Lang.String中找不到main方法
  - 解释：
    - 加载自定义的java.lang.String类时，根据双亲委派机制，会把java.lang.String类交给BootStrap ClassLoader
    - 因为是以java开头，所有BootStrap ClassLoader 可以加载
    - 所以加载的是java自带的java.lang.String
    - 因为只有自定义的java.lang.String中有main方法，而java自带的java.lang.String中没有，所以就会报错


- 示例2:
  > ![parent-class-loader-2](./image/parent-class-loader-2.png)
  - 假设要使用spi核心类，接口和第三方接口实现类
  - 首先通过双亲委派机制到达引导类加载器
  - 引导类加载器会加载SPI核心类和接口
  - 接口的实现类会由引导类加载器反向委派到系统类加载器（getContextClassLoader()获取，一般就是系统类加载器）
  - 系统类加载器就会加载接口的实现类
    
#### 优势

- 避免类的重复加载
  > 一旦有父加载器加载了，就不会委托给子加载器加载
- 保护程序安全，防止核心api被随意篡改
  - 尝试：自定义类java.lang.String
    > 看上方举例
  - 尝试：自定义类java.lang.TestTest
    > 报错：Prohibited package name，禁止使用该包名

#### 沙箱安全机制

> 自定义java.lang.String等出现报错就是沙箱安全机制的一种体现。
> 具体可以自行查阅资料

- 字节码校验器（bytecode verifier）:
  - 确保Java类文件遵循Java语言规范。
  - 这样可以帮助Java程序实现内存保护。但并不是所有的类文件都会经过字节码校验，比如核心类。
- 类装载器（class loader）:其中类装载器在3个方面堆Java沙箱起作用：
  - 防止恶意代码去干涉善意的代码；//双亲委派机制
  - 守护了被新人的类库边界；
  - 将代码归入保护域，确定了代码可以进行哪些操作。
- 存取控制器（access controller）：存取控制器可以控制核心API堆操作系统的存取权限，而这个控制的策略设定，可以由用户指定。
- 安全管理器（security manager）: 是核心API和操作系统之间的主要接口。实现权限控制，比存取控制器优先级高。
- 安全软件包（security package）：java.security下的类和扩展包下的类。允许用户为自己的应用增加新的安全特性，包括：
  - 安全提供者
  - 消息摘要
  - 数字签名 keytools
  - 加密

### 其他

- jvm 中判断两个class对象是否为同一个类判断条件：
  - 类的完整类名必须一致，包括包名
  - 加载这个类的ClassLoader（指ClassLoader实例对象）必须相同
  - 也就是说即使两个Class对象来自于同一个class文件，被一个虚拟机所加载，如果两个类的ClassLoader实例对象不同，那么这两个类对象也是不相等的。
 
- JVM必须知道一个类型是由启动加载器加载的还是由自定义类加载器加载的。
  - 如果一个类型是由自定义类加载器加载的，那么**JVM会将这个类加载器的一个引用作为类型信息的一部分保存在方法区中**。
  - 当解析一个类型到另一个类型的引用的时候，JVM需要保证这两个类型的类加载器是相同的。
    > 之后动态链接那里涉及


- jvm对类的使用方式
  > 字节码和类的加载篇会详细说
  - 主动使用
    - 创建类的实例
    - 访问某个类或接口的静态变量，或者对该静态变量赋值
    - 调用类的静态方法
    - 反射（比如：Class.forName("com.atguigu.Test"))
    - 初始化一个类的子类
    - Java虚拟机启动时被标明为启动类的类
    - JDK 7 开始提供的动态语言支持：
      ```
      java.lang.invoke.MethodHandle实例的解析结果
      REF_getStatic、REF_putStatic、REF_invokeStatic句柄又
      应的类没有初始化，则初始化
      ```
  - 被动使用
    - 除了上面的7种方式的其他方式
    - 都**不会导致类的初始化**（即：类加载过程中的 初始化 阶段）

## 中层 运行时数据区

### 运行时数据区概述及线程

### 程序计数器

### 虚拟机栈

### 本地方法接口

### 本地方法栈

### 堆

### 方法区

### 直接内存

## 下层 执行引擎


### String Table

### 解释器和即时编译器

> 面试很爱问

### 垃圾回收

#### 垃圾回收概述

#### 垃圾回收相关算法

#### 垃圾回收相关概念

#### 垃圾回收器

# 字节码与类的加载

# 性能监控与调优
