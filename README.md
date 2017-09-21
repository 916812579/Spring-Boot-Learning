# Spring-Boot-Learning
spring boot 学习

查看每个目录下的README.md可以了解项目主要涉及到的内容


spring boot配置控制台彩色日志[http://www.cnblogs.com/lixuwu/p/5804793.html](http://www.cnblogs.com/lixuwu/p/5804793.html)

# 目录
- [spring boot快速入门](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo1)
- [Spring Boot开发Web应用](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo2)
- [Spring Boot构建RESTful API与单元测试](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo3)
- [Spring Boot中使用Swagger2构建强大的RESTful API文档](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo4)
- [Spring Boot中使用JdbcTemplate访问数据库](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo5)
- [Spring Boot 使用 Druid 和监控配置](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo6)
- [Spring Boot中Web应用的统一异常处理](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo7)
- [Spring Boot属性配置文件详解](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo8)
- [Spring Boot中使用@Scheduled创建定时任务](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo9)
- [Spring Boot中使用@Async实现异步调用](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo10)
- [Spring Boot中使用AOP统一处理Web请求日志](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo11)

- [Spring Boot中使用Spring Security进行安全控制](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo12)
- [Spring Boot中使用JavaMailSender发送邮件](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo13)
- [Spring Boot中使用Spring-data-jpa让数据访问更简单、更优雅](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo14)
- [Spring Boot中的缓存支持（一）注解配置与EhCache使用](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo15)
- [Spring Boot中的缓存支持（二）使用Redis做集中式缓存](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo16)
- [Spring Boot整合MyBatis](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo17)
- [Spring Boot Actuator监控端点小结](https://github.com/916812579/Spring-Boot-Learning/tree/master/demo18)



# Spring Boot中的事务管理
- 参考：[http://blog.didispace.com/springboottransactional/](http://blog.didispace.com/springboottransactional/)

## 事务
事务的作用就是为了保证用户的每一个操作都是可靠的，事务中的每一步操作都必须成功执行，只要有发生异常就回退到事务开始未进行操作的状态。

## 快速入门

在Spring Boot中，当我们使用了```spring-boot-starter-jdbc```或```spring-boot-starter-data-jpa```依赖的时候，框架会自动默认分别注入```DataSourceTransactionManager```或```JpaTransactionManager```。所以我们不需要任何额外配置就可以用```@Transactional```注解进行事务的使用。

```@Transactional```注解来声明一个函数需要被事务管理,通常在```service```层接口中使用```@Transactional```来对各个业务逻辑进行事务管理的配置

## 事务隔离级别
··
隔离级别是指若干个并发的事务之间的隔离程度，与我们开发时候主要相关的场景包括：```脏读取```、```重复读```、```幻读```。

我们可以看```org.springframework.transaction.annotation.Isolation```枚举类中定义了五个表示隔离级别的值：

```java
public enum Isolation {

	/**
	 * Use the default isolation level of the underlying datastore.
	 * All other levels correspond to the JDBC isolation levels.
	 * @see java.sql.Connection
	 */
	DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),

	/**
	 * A constant indicating that dirty reads, non-repeatable reads and phantom reads
	 * can occur. This level allows a row changed by one transaction to be read by
	 * another transaction before any changes in that row have been committed
	 * (a "dirty read"). If any of the changes are rolled back, the second
	 * transaction will have retrieved an invalid row.
	 * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
	 */
	READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),

	/**
	 * A constant indicating that dirty reads are prevented; non-repeatable reads
	 * and phantom reads can occur. This level only prohibits a transaction
	 * from reading a row with uncommitted changes in it.
	 * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
	 */
	READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),

	/**
	 * A constant indicating that dirty reads and non-repeatable reads are
	 * prevented; phantom reads can occur. This level prohibits a transaction
	 * from reading a row with uncommitted changes in it, and it also prohibits
	 * the situation where one transaction reads a row, a second transaction
	 * alters the row, and the first transaction rereads the row, getting
	 * different values the second time (a "non-repeatable read").
	 * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
	 */
	REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),

	/**
	 * A constant indicating that dirty reads, non-repeatable reads and phantom
	 * reads are prevented. This level includes the prohibitions in
	 * {@code ISOLATION_REPEATABLE_READ} and further prohibits the situation
	 * where one transaction reads all rows that satisfy a {@code WHERE}
	 * condition, a second transaction inserts a row that satisfies that
	 * {@code WHERE} condition, and the first transaction rereads for the
	 * same condition, retrieving the additional "phantom" row in the second read.
	 * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
	 */
	SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE);


	private final int value;


	Isolation(int value) { this.value = value; }

	public int value() { return this.value; }

}
```

- ```DEFAULT```：这是默认值，表示使用底层数据库的默认隔离级别。对大部分数据库而言，通常这值就是：```READ_COMMITTED```。
- ```READ_UNCOMMITTED```：该隔离级别表示一个事务可以读取另一个事务修改但还没有提交的数据。该级别不能防止脏读和不可重复读，因此很少使用该隔离级别。
- ```READ_COMMITTED```：该隔离级别表示一个事务只能读取另一个事务已经提交的数据。该级别可以防止脏读，这也是大多数情况下的推荐值。
- ```REPEATABLE_READ```：该隔离级别表示一个事务在整个过程中可以多次重复执行某个查询，并且每次返回的记录都相同。即使在多次查询之间有新增的数据满足该查询，这些新增的记录也会被忽略。该级别可以防止脏读和不可重复读。
- ```SERIALIZABLE```：所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。

指定方法：通过使用isolation属性设置，例如：

```java
@Transactional(isolation = Isolation.DEFAULT)
```

## 传播行为

所谓事务的传播行为是指，如果在开始当前事务之前，一个事务上下文已经存在，此时有若干选项可以指定一个事务性方法的执行行为。

我们可以看```org.springframework.transaction.annotation.Propagation```枚举类中定义了6个表示传播行为的枚举值：
```java
public enum Propagation {

	/**
	 * Support a current transaction, create a new one if none exists.
	 * Analogous to EJB transaction attribute of the same name.
	 * <p>This is the default setting of a transaction annotation.
	 */
	REQUIRED(TransactionDefinition.PROPAGATION_REQUIRED),

	/**
	 * Support a current transaction, execute non-transactionally if none exists.
	 * Analogous to EJB transaction attribute of the same name.
	 * <p>Note: For transaction managers with transaction synchronization,
	 * PROPAGATION_SUPPORTS is slightly different from no transaction at all,
	 * as it defines a transaction scope that synchronization will apply for.
	 * As a consequence, the same resources (JDBC Connection, Hibernate Session, etc)
	 * will be shared for the entire specified scope. Note that this depends on
	 * the actual synchronization configuration of the transaction manager.
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#setTransactionSynchronization
	 */
	SUPPORTS(TransactionDefinition.PROPAGATION_SUPPORTS),

	/**
	 * Support a current transaction, throw an exception if none exists.
	 * Analogous to EJB transaction attribute of the same name.
	 */
	MANDATORY(TransactionDefinition.PROPAGATION_MANDATORY),

	/**
	 * Create a new transaction, and suspend the current transaction if one exists.
	 * Analogous to the EJB transaction attribute of the same name.
	 * <p><b>NOTE:</b> Actual transaction suspension will not work out-of-the-box
	 * on all transaction managers. This in particular applies to
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 * which requires the {@code javax.transaction.TransactionManager} to be
	 * made available it to it (which is server-specific in standard Java EE).
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	REQUIRES_NEW(TransactionDefinition.PROPAGATION_REQUIRES_NEW),

	/**
	 * Execute non-transactionally, suspend the current transaction if one exists.
	 * Analogous to EJB transaction attribute of the same name.
	 * <p><b>NOTE:</b> Actual transaction suspension will not work out-of-the-box
	 * on all transaction managers. This in particular applies to
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 * which requires the {@code javax.transaction.TransactionManager} to be
	 * made available it to it (which is server-specific in standard Java EE).
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	NOT_SUPPORTED(TransactionDefinition.PROPAGATION_NOT_SUPPORTED),

	/**
	 * Execute non-transactionally, throw an exception if a transaction exists.
	 * Analogous to EJB transaction attribute of the same name.
	 */
	NEVER(TransactionDefinition.PROPAGATION_NEVER),

	/**
	 * Execute within a nested transaction if a current transaction exists,
	 * behave like PROPAGATION_REQUIRED else. There is no analogous feature in EJB.
	 * <p>Note: Actual creation of a nested transaction will only work on specific
	 * transaction managers. Out of the box, this only applies to the JDBC
	 * DataSourceTransactionManager when working on a JDBC 3.0 driver.
	 * Some JTA providers might support nested transactions as well.
	 * @see org.springframework.jdbc.datasource.DataSourceTransactionManager
	 */
	NESTED(TransactionDefinition.PROPAGATION_NESTED);


	private final int value;


	Propagation(int value) { this.value = value; }

	public int value() { return this.value; }

}
```
- `REQUIRED`：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
- `SUPPORTS`：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
- `MANDATORY`：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
- `REQUIRES_NEW`：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
- `NOT_SUPPORTED`：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
- `NEVER`：以非事务方式运行，如果当前存在事务，则抛出异常。
- `NESTED`：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于`REQUIRED`。

指定方法：通过使用propagation属性设置，例如：
```java
@Transactional(propagation = Propagation.REQUIRED)
```

```@Transactional```默认的事务传播行为是```Propagation.REQUIRED```

