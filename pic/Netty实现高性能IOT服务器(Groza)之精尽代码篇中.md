![blueprint-company-concept-7366](/home/james/Downloads/blueprint-company-concept-7366.jpg)



**运行环境:**

- JDK 8+

- Maven 3.0+
- Redis

**技术栈:**

- SpringBoot 2.0+

- Redis (**Lettuce**客户端,**RedisTemplate**模板方法)

- Netty 4.1+

- MQTT 3.1.1

**IDE:**

- IDEA或者Eclipse

- Lombok插件



# 简介

近年来，物联网高歌猛进，美国有“工业互联网”，德国有“工业4.0”，我国也有“中国制造2025”，这背后都是云计算、大数据。据波士顿咨询报告，单单中国制造业，云计算、大数据、人工智能等新技术就能为其带来高达6万亿的额外附加值。

国内外巨头纷纷驻足工业互联网，国外如亚马逊AWS、微软Azure，国内则是三大电信运营商、百度云、华为、金山云等，其中腾讯云、阿里云最甚，还拉来了传统制造大佬,国内巨头纷纷在物联网上布局。在2018云栖-深圳峰会上，阿里巴巴资深副总裁，阿里云总裁胡晓明宣布阿里巴巴将正式进军IoT。胡晓明表示，IoT是阿里巴巴集团继电商、金融、物流、云计算之后的一条新的主赛道。



# IOT技术窥探

以上这些内容，作者作为一个开发人员，并不是一个投资人员和创业先锋。并不太关系这些具体细节。我所关心的是如何用技术去**实现**或者**模拟**一个支持百万链接的IOT服务器,并不严谨，仅做大家参考。

关于为什么选用下图的中间件或者对MQTT不太了解的话,可以阅读我之前的2篇文章:

1. [IOT高性能服务器实现之路](https://www.cnblogs.com/sanshengshui/p/9797352.html)
2. [Netty实现高性能IOT服务器(Groza)之手撕MQTT协议篇上](https://www.cnblogs.com/sanshengshui/p/9826009.html)

## 技术轮廓图

![轮廓图](/home/james/IdeaProjects/netty-learning-example/pic/netty-iot.png)

## 项目结构

```
netty-iot
      ├── auth -- 认证
        ├── service -- 用户名,密码认证实现类
        ├── util -- 认证工具类
      ├── common -- 公共类
        ├── auth -- 用户名,密码认证接口
        ├── message -- 协议存储实体及接口类
        ├── session -- session存储实体及接口类
        ├── subscribe -- 订阅存储实体及接口类
      ├── config -- Redis配置
      ├── protocol -- MQTT协议实现
      ├── server -- MQTT服务器
      ├── store -- Redis数据存储
      ├── web -- web服务
      ├── NettyIotApplication -- 服务启动类
```

