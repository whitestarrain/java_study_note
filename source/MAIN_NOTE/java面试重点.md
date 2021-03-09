# fail-last 机制

[博客](https://juejin.cn/post/6879291161274482695)

# 枚举

[java guide]()

[菜鸟教程](https://www.runoob.com/java/java-enum.html)

[Java 枚举(enum) 详解7种常见的用法](https://blog.csdn.net/qq_27093465/article/details/52180865)

```java
enum Color 
{ 
    RED, GREEN, BLUE; 
} 
```

**等价于**

```java
class Color
{
     public static final Color RED = new Color();
     public static final Color BLUE = new Color();
     public static final Color GREEN = new Color();
}
```

# 静态内部类与非静态内部类

[博客](https://blog.csdn.net/yaomingyang/article/details/79363631)


- 非静态内部类不可以使用static，只有静态内部类才能够定义静态的成员变量与成员方法。

- 在外部类的成员的访问上，有比较大的限制。
  - 一般的非静态内部类，可以随意的访问外部类中的成员变量与成员方法。即使这些成员方法被修饰为private(私有的成员变量或者方法)。
  - 静态内部类只能引用外部类中的静态的成员（变量或方法），而不能够访问非静态的变量。

- 最大区别
  - 通常情况下，在一个类中创建成员内部类的时候，有一个强制性的规定，即内部类的实例一定要绑定在外部类的实例中。非静态内部类在编译完成之后会隐含地保存着一个引用，该引用是指向创建它的外围类，
  - 但是静态内部类没有指向外部类的引用,也就是说
    - 它的创建是不需要依赖外围类的创建。
    - 它不能使用任何外围类的非static成员变量和方法。
  - example
    <details>
    <summary>代码</summary>

    ```java
    public class Singleton {

        //声明为 private 避免调用默认构造方法创建对象
        private Singleton() {
        }

      // 声明为 private 表明静态内部该类只能在该 Singleton 类中被访问
        private static class SingletonHolder {
            private static final Singleton INSTANCE = new Singleton();
        }

        public static Singleton getUniqueInstance() {
            return SingletonHolder.INSTANCE;
        }
    }
    ```
    当 Singleton 类加载时，静态内部类 SingletonHolder 没有被加载进内存。只有当调用 getUniqueInstance() 方法从而触发 SingletonHolder.INSTANCE 时 SingletonHolder 才会被加载，此时初始化 INSTANCE 实例，并且 JVM 能确保 INSTANCE 只被实例化一次。

    这种方式不仅具有延迟初始化的好处，而且由 JVM 提供了对线程安全的支持。
    </details>


```
牢记两个差别：

一、如是否可以创建静态的成员方法与成员变量(静态内部类可以创建静态的成员，而非静态的内部类不可以)

二、对于访问外部类的成员的限制(静态内部类只可以访问外部类中的静态成员变量与成员方法，而非静态的内部类即可以访问所有的外部类成员方法与成员变量)。
```


