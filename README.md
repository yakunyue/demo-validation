# demo-validation
A Applacation for Test Spring-Validation


## 前言

JSR 303 是专门针对 Bean Validation 制定的 Java 规范，Hibernate Validator 又对JSR 303 进行了扩展，合理运用这两个框架提供的校验功能可以为项目省去很多重复的校验代码。Spring 已经为数据校验提供了很好的支持，基于 spring 和 JSR 303 规范中的一些注解，再加上一些自定义内容，能极大简化项目中参数校验代码。

[JSR 303 文档下载地址](https://jcp.org/aboutJava/communityprocess/final/jsr303/index.html)
[Hibernate Validator 文档地址](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/)

JSR 303 的文档太硬核了，我英文一般，读起来太生涩了。相比较而言 Hivernate Validator 的文档要容易理解很多。本文只讨论 Bean Validation 的常用姿势，足够解决项目中90%的校验需求，如需彻底了解  Bean Validation ，还需认真阅读上面的两个文档。

## Bean Validation

实现校验最主要的两个组件是约束注解（Constraint annotation）和约束校验器（Constraint validator）。校验注解加在需要校验的参数或者其内部属性上，校验器负责判断该参数或其内部属性是否符合检验规则。

*本文所有的测试代码已上传gitHub，项目地址：[demo-validation](https://github.com/yueyakun2017/demo-validation)*

### 校验注解（Constraint annotation）

> 如果注解的保持策略包含 RUNTIME，并且注释本身是用 javax.validation.Constraint 注释，则该注释被认为是约束注解。-- JSR 303 文档

#### 约束注解的默认字段
约束注解有三个保留名称字段，这三个字段有特殊意义，每个约束注解都必须有

* message
    * 每一个约束注解都必须有一个 String 类型的 message 字段。
    * 这个属性的值是用来构建校验失败时的报错信息的
    * 值可以直接是错误信息本身，也可以是一个占位符，真正的错误信息放在 ValidationMessages.properties 文件中

* group
    * 每一个约束注解都必须有一个 Class 数组类型的 group 字段
    * group 字段的默认值必须是空数组
    * 作用是控制校验顺序或者执行部分校验

* payload
    * 每一个约束注解都必须有一个 Payload 数组类型的 payload 字段
    * payload 字段的默认值必须是空数组
    * 通常用它控制校验失败时的日志等级

* 其他自定义属性
    * 根据约束的不同，约束注解还会定义其他类型的字段，如 @Size 注解中的 min 和 max 字段

#### 校验组

group 属性相同的字段属于同一校验组。group 属性可以是数组类型，所以一个字段有可能属于多个校验组。校验组有以下两个功能：

* 实现部分校验
约束注解是可以通过指定 group 来实现分组校验的，比如在项目中，一条数据“添加”和“修改”的校验规则肯定是不一样的，这时我们可以指定对象中字段约束注解的校验组来实现部分校验

* 控制检验顺序
控制校验顺序需要配合 @GroupSequence 注解使用，定义如下一个校验组接口，就可以实现 先校验 New.class 组的约束条件后校验 Custom.class 组的约束条件。（**注意：如果前面组的校验不通过，后面组的约束校验是不会进行的**）

```java
import javax.validation.GroupSequence;

@GroupSequence({New.class,Update.class})
public interface NewAndUpdate {

}
```
*用group实现部分校验的测试在 [demo-validation](https://github.com/yueyakun2017/demo-validation) 项目的 TestCustomer 类中*

#### 校验的继承

父类中的校验注解在子类中是一样生效的

#### 级联校验
Bean Validation 支持级联校验（文档没说最多关联几级）
有 A 和 B 两个 JavaBean，如果 A 中有一个 B 类型的字段 b，校验 A 的时候想延伸校验段 b，只需在 b 字段上加 @Valid 注解。

级联校验也适用于集合类型的字段，如：
* 集合
* 实现了java.lang.Iterable接口( 例如Collection, List 和 Set)
* 实现了java.util.Map接口

*关于级联校验的简单测试在 [demo-validation](https://github.com/yueyakun2017/demo-validation) 项目的 TestCustomer 类中*

#### 约束条件组合

约束注解是可以组合使用的。比如可以在自定义注解上添加一个或多个定义好约束注解，这样这个自定义注解就同时有了多个约束条件，比如下面的这个自定义的 @KeyId 注解。它上面标注了 @Min 和 @NotNull 注解，这样 @KeyId 就同时拥有了
@Min 和 @NotNull 的约束条件，当然你还可以用 @Constraint 注解再指定一个校验器。其中 @ReportAsSingleViolation 注解的作用是不管当违反哪个约束，都报本注解的 message 信息，不加这个注解的话会报各自注解的 message 信息。
```java
//校验是否符合主键id要求，不为null，大于等1
@Min(1)
@NotNull
@ReportAsSingleViolation
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})
public @interface KeyId {
    String message() default "{custom.constraints.KeyId.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```
*关于约束条件组合的简单测试在 [demo-validation](https://github.com/yueyakun2017/demo-validation) 项目的 TestCustomer 类中*

## Spring 对 Bean Validation 的支持

如果每当校验约束的时候都要创建一个 Validator，然后调用 Validator 的 validate 方法，之后再出来返回的校验结果也挺烦的。还好 Spring 为 Bean Validation 提供了很好的支持。

Spring 的参数校验功能有两部分组成：
1. 首先是 argument resolver 内部进行的 data-binding 时的 参数校验（validation），这个阶段的 validation 是由spring-mvc实现并决定的。

2. 第二部分validation是由spring-framework，基于aop实现的。

下面对这两部分校验功能进行详细说明

### data-binding 时的参数校验

这一层的参数校验可以看做是由 Spring MVC 在 argument resolver 阶段进行 data-binding 时顺便进行的，这个阶段的参数校验有以下几个特点：
* 只在 Controller 层生效
* 必须在 Controller 上添加 @Validated 注解
* RequestParam 参数校验异常会直接返回 500 错误
* RequestBody 对象参数必须在对象参数前加@Valid 或 @Validated 注解才会触发内部属性的校验规则
* RequestBody 参数校验异常结果可以用 BindingResult 对象接收，然后自主处理，也可以不接收。不接收会直接 400 Bad Request

*这部分的测试代码主要在[demo-validation](https://github.com/yueyakun2017/demo-validation)项目的 AddressController 类中*

### Spring 基于 AOP 实现的参数校验
这部分的参数校验由 spring-framework 基于 AOP 实现，特点如下：
* 只要一个类被注册为bean并且加了@Validated注解，则spring为此类的所有方法提供参数校验
* 对象参数必须在对象参数前加@Valid 注解才会触发内部属性的校验规则
* 校验不通过会报异常，异常类型为 javax.validation.ConstraintViolationException
* 由于基于AOP实现，所以类内部调用是不会触发validation的

*这部分的测试代码主要在[demo-validation](https://github.com/yueyakun2017/demo-validation)项目的 TestAOPValidationController 和 AddressService 类中*
