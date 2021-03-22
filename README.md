# 笔记总览(source目录)

> 跳转：<br />
> [python与机器学习笔记](https://github.com/whitestarrain/python_learn)
> [大数据与深度学习笔记](https://github.com/whitestarrain/big_data) <br />

- Java 笔记
  - [Java 基础笔记](./source/OLD_JAVA_NOTE/oldnote.md)
    > 基础 java 知识点，基本全是代码+注释
  - [Java 笔记](./source/MAIN_NOTE/Note.md)
    > 反射，注解，jdbc,javaweb,redis基本使用
  - [Java 并发](./source/MAIN_NOTE/java并发.md)
    > java并发基础，完善中
  - [Java 常见问题](./source/MAIN_NOTE/java重点.md)
    > 杂项，日常学习中遇见的一些问题，进行简单记录。未来会详细整理到[博客](https://whitestarrain.github.io/blog/)
  - [Java NIO](./source/MAIN_NOTE/javaNIO.md)
    > javaNIO基础
- 前端笔记
  - [HTML](./source/HTML_NOTE/01-HTML.md)
    > 网上搜集。
  - [CSS](./source/CSS_NOTE/CSS.md)
    > 网上搜集
  - [BFC](./source/BFC_JD/BFC.md)
    > 网上搜集
  - [Bootstrap](./source/CSS_NOTE/Bootstrap.md)
    > 停止更新，还是官方文档好用
  - [JavaScript 基础](./source/JS_NOTE/00/01-JavaScript基础.md)
    > 网上搜集基础上进行补充整理
  - [WebAPI](./source/JS_NOTE/02/02-Web-API.md)
    > 网上搜集基础上进行补充整理
  - [JavaScript 高级](./source/JS_NOTE/03/03-JavaScript高级.md)
    > 网上搜集基础上进行补充整理
  - [jQuery](./source/JS_NOTE/04_jquery/jQuery.pdf)
    > 网上搜集
  - [Vue](#)
    > 待整理
  - [Node.js](./source/JS_NOTE/05_Node.js/Node.js.md)
    > 进行中
- 数据库笔记
  - [Mysql](./source/DATABASE/DATABASE.md)
    > msyql笔记。mysql相关基本概念以及基本使用。
  - [oracle](./source/DATABASE/oracle.md)
    > 进行中
- 设计模式
  - [设计模式](./source/DESIGN_PATTERNS/main.md)
    > 进行中
- JVM
  - [JVM](./source/MAIN_NOTE/JVM.md)
    > 根据宋红康老师视频完成。正在学习中篇
  - [JVM 指令手册](./source/MAIN_NOTE/JVM指令手册.md)
- Java 框架
  - [JavaFrame(SSM)](./source/MAIN_NOTE/JavaFrame.md)
    > SSM笔记。SpringMVC未整理完成
  - [Spring Boot](./source/MAIN_NOTE/SpringBoot基础.md)
    > springBoot基础使用
- Maven
  - [多模块构建笔记](./source/MAIN_NOTE/Maven.md)
    > 并不是maven的详细笔记。
- 常用工具
  - [git](./source/MAIN_NOTE/git.md)
    > git常用命令
  - [docker](#)
    > 待整理
- [操作系统常见问题](./source/OS/os.md)
  > 根据操作系统概念第9版整理<br />
  > 待进一步完善

# 结构总览

- 工程目录
  - src:包含各种学习笔记（.java 文件）
    > 注意：JDBCDemo9_2 JDBCDemo5 把 Static{} 代码块 去了 Static 防止不开 mysql 时报错，影响做笔记
    - \_1_java_base(java 基础)
      - \_1_java_base.base_strengthen:基础增强，包括注解与反射（一开始做，笔记有些乱）
      - jdk8_new_feature:jdk8 新特性
    - \_2_MySQL:无文件，笔记都在 note.md
    - \_3_jdbc:jdbc 笔记
    - 其他文件：配置文件以及 java 文件所生成或者所依赖等，看到指定文件时可以找找看
  - source:各种笔记文件
  - src_java_web:java_web 示例
  - lib 目录需要 jar 包列表：
    - c3p0-0.9.5.2.jar
    - commons-logging-1.2.jar
    - druid-1.0.9.jar
    - jsoup-1.12.2.jar
    - JsoupXpath-0.3.2.jar
    - junit-platform-console-standalone-1.6.0.jar
    - mchange-commons-java-0.2.12.jar
    - mysql-connector-java-5.1.48-bin.jar
    - spring-beans-5.1.10.RELEASE.jar
    - spring-core-5.1.10.RELEASE.jar
    - spring-jdbc-5.1.10.RELEASE.jar
    - spring-tx-5.1.10.RELEASE.jar
      > 为减小项目大小，此处并没有提供 jar 包

