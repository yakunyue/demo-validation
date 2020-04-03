# demo-validation
A Applacation for Test Spring-Validation


## 前言

每个项目都会有数据校验相关的代码，而且代码量还都不少。大部分时候冗长的、重复的数据校验代码并不一定是写代码的人的锅，因为不是每个项目都有时间让程序员精雕细琢。所以一个统一的架构级别的数据校验方案是每一个架构师应该注意的方面。Spring 已经为数据校验提供了很好的支持。


## Spring 提供的参数校验

Spring 的参数校验功能有两部分组成：
1. 首先是 argument resolver 内部进行的 data-binding 时的 参数校验（validation），这个阶段的 validation 是由spring-mvc实现并决定的。

2. 第二部分validation是由spring-framework，基于aop实现的。

下面对这两部分校验功能进行详细说明

### data-binding 时的参数校验

这一层的参数校验可以看做是由 Spring MVC 在 argument resolver 阶段进行 data-binding 时顺便进行的，这个阶段的参数校验有以下几个特点：

* 只在 Controller 层生效
* 必须在 Controller 上添加 @Validated 注解
* 校验结果
