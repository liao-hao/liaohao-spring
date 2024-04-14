# 手写Spring

## 实现功能
1. 实现Spring Application 容器
2. 组件扫描
3. 实现Bean的依赖注入
4. 单例,原型作用域
5. 处理特殊接口
6. AOP


## Bean的创建步骤
1. 得到扫描路径
2. 扫描路径下的所有的class文件
3. 根据class文件得到Class对象
4. 判断是否是一个bean
5. 根据class文件生成beanDefinition
6. 遍历beanDefinition创建bean
7. 依赖注入
8. 初始化
9. 生成bean

# 实现的组件
## 1. ApplicationContext
- `ApplicationContext`是Spring的核心接口之一，它是Spring应用的上下文，负责管理Bean的生命周期。


## 2. BeanDefinition

`BeanDefinition` 表示Bean的定义, 他存在很多属性来描述一个Bean的特点
- class
- scope, 作用域, 单例或者原型
- isLazy, 是否懒加载
- initMethodName: 初始化方法
- destroyMethodName: 销毁的方法



## 3. BeanPostProcessor





# 待实现的功能

1. **Bean的生命周期回调**：除了`InitializingBean`接口，Spring还支持其他的生命周期回调，如`DisposableBean`接口和`@PostConstruct`、`@PreDestroy`注解。

2. **更多的依赖注入方式**：目前你的代码只支持通过字段注入依赖，你可以考虑添加对构造器注入和setter方法注入的支持。

3. **循环依赖处理**：如果两个bean互相依赖，你的代码可能会出现问题。你需要添加对循环依赖的处理。

4. **配置类和@Bean注解**：Spring支持使用配置类（带有`@Configuration`注解的类）和`@Bean`注解来定义bean。这是一种更灵活的定义bean的方式。

5. **AOP（面向切面编程）**：Spring的AOP功能可以让你在不修改业务代码的情况下，添加一些横切关注点，如事务管理、日志记录等。

6. **事件发布和监听**：Spring支持使用事件来进行组件之间的解耦通信。你可以添加对事件发布和监听的支持。

7. **资源管理**：Spring提供了一套资源抽象，可以用统一的方式来访问各种资源，如文件、类路径资源等。

8. **国际化支持**：Spring支持使用`MessageSource`来进行国际化。

9. **数据验证**：Spring支持使用`Validator`接口和`DataBinder`类来进行数据验证。

10. **集成其他技术**：Spring有很多与其他技术的集成，如数据库访问（JDBC、ORM等）、事务管理、缓存、邮件发送、任务调度等。

