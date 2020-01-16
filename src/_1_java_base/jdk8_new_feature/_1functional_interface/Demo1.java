package _1_java_base.jdk8_new_feature._1functional_interface;

/*
函数式接口：有且只有一个抽象方法的接口，称之为函数式接口        lambda表达式就是重写抽象方法的。
当然接口中可以包含其他的方法（默认，静态，私有等非抽象接口） !!!!!!!!!!!!!!!!!!!!记住
 */
@FunctionalInterface // 当不是抽象方法时编译失败
interface MyFunctionalInterface {
    public abstract void method();
}

/*
 * 函数式接口的使用：一般可以作为方法的参数和返回值类型
 */
public class Demo1 {
    public static void show(MyFunctionalInterface myinterface) {
        myinterface.method();
    }

    public static void main(String[] args) {
    // 调用show方法，方法的参数是一个接口，所以可以传递接口的实现类的对象（此处直接通过匿名内部类来写了）
        show(new MyFunctionalInterface() {
            @Override
            public void method() {
                System.out.println("使用匿名内部类重写函数式接口中的方法");
            }
        });
        // 调用show方法，参数是一个函数式接口，所以可以传递lambda表达式
        show(() -> {
            System.out.println("使用lambda表达式，重写method方法");
        });

        // 简化lambda表达式
        show(() -> System.out.println("使用lambda表达式，重写method方法"));// 有且只有一条语句的话，大括号，分号省略

        /*
         * 匿名内部类和lambda表达式区别： 在这里匿名内部类会生成一个FunctionalInterfaceDemo1$1.class文件
         * 而lambda表达式不会。效率更高些。
         * 
         * 
         * 只要有函数式接口作为参数，就可以使用lambda表达式
         * lambda表达式其实就是在重写接口中唯一的方法
         */
    }
}