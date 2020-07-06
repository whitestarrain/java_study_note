# 1. mybatis

## 1.1. 基础

### 1.1.1. mubatis入门

- 框架：
	- 概念：
		- 半成品软件
		- 封装了很多细节，使开发者可以通过极简的方式实现功能，提高效率
	- 目的：对三层架构的封装
		- 持久层解决方案（非框架）
			- jdbc -- 规范
			- Spring的JDBCTemplate -- 简单封装
			- Apache的DBUtils
- mybatis概述
	- 持久层框架，java编写
	- 封装了很多jdbc细节，使开发者只用关注sql语句本身，而不用关注注册驱动等繁杂过程
	- 它使用了ORM思想，实现了结果集的封装
		- Object Relation Mapping对象关系映射
		- 把数据库表和实体类及实体类的属性对应起来，使操作实体类就能操作数据库表
			> 实体类中的属性和数据库表中的字段名保持一致

### 1.1.2. mybatis环境搭建

> 此处主要是使用xml，注解会在后面演示
- mybatis环境搭建
	- maven导入包
		- mybatis
		- mysql-connect-java
		- log4j
		- junit
	- 创建实体类（实现Serializable接口？）
	- 创建dao接口
		```java
		package com.itheima.dao;

		import com.itheima.domain.User;

		import java.util.List;

		/**
		* @author 黑马程序员
		* @Company http://www.ithiema.com
		*
		* 用户的持久层接口
		*/
		public interface IUserDao {

				/**
				* 查询所有操作
				* @return
				*/
				List<User> findAll();
		}
		```
	- 创建一个xml文件（习惯名称：SqlMapConfig.xml）
	- 添加xml文件的mybatis约束
		```xml
		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE configuration  
			PUBLIC "-//mybatis.org//DTD Config 3.0//EN"  
			"http://mybatis.org/dtd/mybatis-3-config.dtd">

		```
	- 配置xml文件
		```xml
		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE configuration
						PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
						"http://mybatis.org/dtd/mybatis-3-config.dtd">
		
		<!-- mybatis的主配置文件 -->
		<configuration>
				<!-- 配置环境 -->
				<environments default="mysql"> <!-- 默认使用配置名称 -->

						<!-- 配置mysql的环境-->
						<environment id="mysql">
								<!-- 配置事务的类型-->
								<transactionManager type="JDBC"></transactionManager>

								<!-- 配置数据源（连接池） -->
								<!-- 一共有三种类型，后面再讲 -->
								<dataSource type="POOLED">

										<!-- 配置连接数据库的4个基本信息 -->
										<property name="driver" value="com.mysql.jdbc.Driver"/>
										<property name="url" value="jdbc:mysql://localhost:3306/eesy_mybatis"/>
										<property name="username" value="root"/>
										<property name="password" value="1234"/>
								</dataSource>
						</environment>
				</environments>

				<!-- 指定映射配置文件的位置，映射配置文件指的是每个dao独立的配置文件 -->
				<mappers>
						<mapper resource="com/itheima/dao/IUserDao.xml"/><!-- mapper路径 -->
				</mappers>
		</configuration>

		```
	- 创建mapper文件，添加mapper约束
		```xml
		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE mapper
						PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
						"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
		```
	- 添加配置
		```xml
		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE mapper
						PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
						"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

		<mapper namespace="com.itheima.dao.IUserDao">

				<!--配置查询所有-->
				<select id="findAll" resultType="com.itheima.domain.User"> <!-- 这里一定要与方法名称相对应，以及一定要告知封装到的实体类 -->
						select * from user
				</select>
		</mapper>
		```
	- 注意事项
		- mybatis中将持久层（dao层）的操作接口名称叫做mapper，比如`UserDao`和`UserMapper`是相同意思
		- 在映射配置文件中要指明封装到的实体类的全限定类名
		- mybatis映射文件位置必须和dao接口的包结构相同。
			```
			├─src
			│  ├─main
			│  │  ├─java
			│  │  │  └─com
			│  │  │      └─itheima
			│  │  │          ├─dao
			│  │  │          │      IUserDao.java
			│  │  │          │
			│  │  │          └─domain
			│  │  │                  User.java
			│  │  │
			│  │  └─resources
			│  │      │  log4j.properties
			│  │      │  SqlMapConfig.xml
			│  │      │
			│  │      └─com
			│  │          └─itheima
			│  │              └─dao
			│  │                      IUserDao.xml
			│  │
			│  └─test
			│      └─java
			│          └─com
			│              └─itheima
			│                  └─test
			│                          MybatisTest.java
			```
		- 映射配置文件的mapper标签，nampspace属性取值，必须是dao接口的全限定类名
		- 映射配置文件的操作配置，id属性的取值必须是dao接口的方法名
		- **遵从后三个条件后，就不用再写dao的实现类，而只通过mybatis实现**



### 1.1.3. mybatis入门案例

```java
/**
 * @author 黑马程序员
 * mybatis的入门案例
 */
public class MybatisTest {

    /**
     * 入门案例
     * @param args
     */
    public static void main(String[] args)throws Exception {
        //1.读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        //3.使用工厂生产SqlSession对象
        SqlSession session = factory.openSession();
        //4.使用SqlSession创建Dao接口的代理对象
        IUserDao userDao = session.getMapper(IUserDao.class);
        //5.使用代理对象执行方法
        List<User> users = userDao.findAll();
        for(User user : users){
            System.out.println(user);
        }
        //6.释放资源
        session.close();
        in.close();
    }
}

```
- 图解
	> ![](./image/入门案例的分析.png)

- 基于注解的入门案例
	- 把IUserDao.xml移除，在dao接口方法上使用@Select注解并且指定sql语句
	- 在SqlMapConfig.xml中配置mapper标签时，修改使用class属性指定dao接口的权限定类名
		```xml
		<mapper resource="com/itheima/dao/IUserDao.xml"/>
		<!-- 改为 -->
		<mapper class="com.ithema.IUserDao.class">
		```

- 明确：
	- 在实际开发中，越简便越好
	- 尽管使用mybatis框架，也支持自己写实现类
	- 但通常不采用使用dao实现类的方式，直接使用框架
	- 使用自定义实现类演示（看看就行）：
		```java
		// test.java
		public static void main(String[] args)throws Exception {
						//1.读取配置文件
						InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
						//2.创建SqlSessionFactory工厂
						SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
						SqlSessionFactory factory = builder.build(in);

						// 3将工厂传进去
						userdao = new UserDaoImpl(factory);

						//6.使用对象执行方法
						List<User> users = userDao.findAll();
						for(User user : users){
								System.out.println(user);
						}
						//7.释放资源
						session.close();
						in.close();
		}
		```
		```java
		//实现类
		/**
		* @author 黑马程序员
		*/
		public class UserDaoImpl implements IUserDao {

				private SqlSessionFactory factory;

				public UserDaoImpl(SqlSessionFactory  factory){
						this.factory = factory;
				}

				public List<User> findAll(){
						//4.使用工厂创建SqlSession对象
						SqlSession session = factory.openSession();
						//5.使用session执行查询所有方法
						// * 注意：这里使用的是namespage.id 只有id时不行的。指定这个是为了找到配置的sql语句
						List<User> users = session.selectList("com.itheima.dao.IUserDao.findAll");
						session.close();
						//返回查询结果
						return users;
				}
		}
		
		```

### 1.1.4. 自定义mybatis框架

> 了解执行细节

## 1.2. 基本使用

### 1.2.1. 单表crud操作

### 1.2.2. 参数和返回值

### 1.2.3. dao编写

### 1.2.4. 配置细节

## 1.3. 深入

### 1.3.1. 多表查询

### 1.3.2. 事务

### 1.3.3. 多表查询

## 1.4. 缓存和注解开发

### 1.4.1. 缓存和注解开发

### 1.4.2. mybatis加载时机

### 1.4.3. 一级缓存和二级缓存

### 1.4.4. 注解开发
