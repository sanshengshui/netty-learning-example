[TOC]



# 负载，性能测试工具-Gatling

![Gatling](/home/james/图片/Gatling-dark-logo.png)

## 前言

### Gatling

Gatling是一款功能强大的负载测试工具，它为易于使用，高可维护性和高性能而设计。

开箱即用，Gatling由于对HTTP协议的出色支持，使其成为负载测试任何HTTP服务器的首选工具。由于核心引擎实际上是协议不可知的，因此完全可以实现对其他协议的支持。例如，Gatling目前还提供JMS支持。

**代码自定义**并且**场景资源有效**是Gatling的两个基础。并且拥有富有表现力的[DSL](http://en.wikipedia.org/wiki/Domain-specific_language)，自我解释的场景,易于维护，可以保存在版本控制系统中的优点。

只要底层协议（如HTTP）可以以非阻塞方式实现，Gatling的体系结构就是异步的。这种架构允许我们将虚拟用户实现为消息而不是专用线程，这使得硬件要求不需要很苛刻。因此，运行数千个并发虚拟用户不是问题。

### 使用理由

- **避免崩溃**：Gatling可以帮助您预测缓慢的响应时间和奔溃。

- **缩短产品上市时间**：Gatling 在开发周期的早期检测到性能问题和错误
- **增强用户体验**：Gatling可以准确描述您最慢的用户体验

- **提升您的业务**：加特林可以防止您的业务成为自身成功的牺牲品

### 性能测试

#### 性能测试是什么意思?

**Web应用程序**的性能测试包括:

1. **模拟大量**具有**复杂行为的用户**;
2. **收集**和汇总**所有请求的响应时间**;
3. **创建报告**和**分析数据**;

![性能测试](/home/james/图片/Capture-décran-2017-01-05-16.26.07.png)

#### 编写测试场景，自动化测试

Gatling的**类似代码的脚本**使您可以**轻松维护测试场景**，并在持续交付管道中轻松实现自动化。

我们开发了自己的**领域特定语言（DSL）**，以便让**每个人都能轻松阅读**您的场景。

![自动化测试](/home/james/图片/codelike.png)

#### 分析并调查您的应用程序的瓶颈

Gatling是一个**功能强大的工具**：只需几台计算机，您就可以 在Web应用程序上模拟**每秒数十万个请求，**并获得**高精度指标**。

在测试结束时，Gatling会自动生成**详尽，动态**且**丰富多彩的报告**。

平均值和平均数据是**不够的**：使用Gatling，您可以得到**适当**的**响应时间**百分位数**。**不要让最慢的用户落后！

![report](/home/james/图片/rapport.png)

## 安装

### 准备工作

#### Java版本

Gatling主要针对Oracle提供的JDK8软件包进行测试。Gatling需要**JDK8**（因此不支持JDK9 atm）。Gatling应该适用于任何JDK8更新，但我们建议您使用最新版本。

#### IPv4 vs IPv6

发现IPv6（默认情况下在Java上启用）有时会导致一些性能问题，因此启动脚本会使用以下选项禁用它：

```
-Djava.net.preferIPv4Stack=true
-Djava.net.preferIPv6Addresses=false
```

如果您确实需要优先选择IPv6，请编辑启动脚本。

#### 操作系统调整

> 以下说明摘自伟大的Riak文档。 有关更多详细信息或OS X的说明，请参阅“ [打开文件限制”](https://github.com/basho/basho_docs/blob/master/content/riak/kv/2.2.3/using/performance/open-files-limit.md/) 和“ [内核和网络调整”](https://github.com/basho/basho_docs/blob/master/content/riak/kv/2.2.3/using/performance.md#kernel-and-network-tuning)部分。

在正常操作期间，Gatling会消耗大量的打开文件句柄。通常，操作系统会限制此数量，因此您可能必须在所选操作系统中调整一些选项，以便可以打开*许多*新套接字并实现高负载。

##### 打开文件限制

大多数操作系统都可以使用该命令更改打开文件限制。例：`ulimit -n`

```
$ ulimit -n 65536
```

但是，这只会更改当前shell会话的限制。在系统范围内，永久性地更改限制因系统而异。

要*为系统的所有用户*永久设置软值和硬值*，*以允许最多65536个打开文件; 编辑`/etc/security/limits.conf`并附加以下两行：

```
*       soft    nofile  65535
*       hard    nofile  65535
```

保存文件。启动新会话以使限制生效。您现在可以验证是否正确设置了限制。`ulimit -a`

对于Debian和Ubuntu，您应该启用PAM用户限制。为此，请添加：`session required pam_limits.so`

- `/etc/pam.d/common-session`
- `/etc/pam.d/common-session-noninteractive` 如果文件存在
- `/etc/pam.d/sshd` 如果您通过SSH访问该计算机

另外，如果访问通过SSH的机器，一定要具有在`UseLogin yes``/etc/ssh/sshd_config`

要进行更多调整，您可能需要执行以下操作：

```
# more ports for testing
sudo sysctl -w net.ipv4.ip_local_port_range="1025 65535"

# increase the maximum number of possible open file descriptors:
echo 300000 | sudo tee /proc/sys/fs/nr_open
echo 300000 | sudo tee /proc/sys/fs/file-max
```

##### 内核和网络调优

考虑调整内核和网络，并在/etc/sysctl.conf中添加以下这些设置：

```
net.ipv4.tcp_max_syn_backlog = 40000
net.core.somaxconn = 40000
net.core.wmem_default = 8388608
net.core.rmem_default = 8388608
net.ipv4.tcp_sack = 1
net.ipv4.tcp_window_scaling = 1
net.ipv4.tcp_fin_timeout = 15
net.ipv4.tcp_keepalive_intvl = 30
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_moderate_rcvbuf = 1
net.core.rmem_max = 134217728
net.core.wmem_max = 134217728
net.ipv4.tcp_mem  = 134217728 134217728 134217728
net.ipv4.tcp_rmem = 4096 277750 134217728
net.ipv4.tcp_wmem = 4096 277750 134217728
net.core.netdev_max_backlog = 300000
```



### 离线下载

您可以离线下载Gatling。然后，您只需要一个文本编辑器（Scala语法高亮）来编辑模拟，您就可以从命令行启动Gatling。

查看我们的[下载页面](https://gatling.io/#/resources/download)以获取下载链接。

将下载的包解压缩到您选择的文件夹中。使用`bin`目录中的脚本启动Gatling和Recorder。

Windows用户：我们建议您不要将Gatling放在“ Programs”文件夹中，因为可能存在权限和路径问题。

要运行Gatling，您需要安装JDK。加特林至少需要**JDK8**。然后，我们建议您使用最新版本。

- Gatling启动脚本和Gatling maven插件会在`JAVA_HOME`env var中设置它。

  与OSX一样，操作系统有自己的棘手方法来发现要运行的Java版本，因此最终可能会运行与您告诉您的版本不同的版本。如果您遇到奇怪的错误，并且您希望运行JDK8，则可能需要明确设置`JAVA_HOME`。`java -version``Unsupported major.minor version 51.0`

### 使用构建工具

#### Maven

Gatling提供了一个名为gatling-maven-plugin的官方maven 插件。这个插件允许您编译Scala代码并启动Gatling模拟。

有关更多信息，请查看[maven插件文档](https://gatling.io/docs/current/extensions/maven_plugin#maven-plugin)。



### 使用IDE

您可以使用任何Scala语法高亮的文本编辑器编辑Simulation类。但如果您是开发人员，您很可能希望将自己喜欢的IDE与Gatling一起使用。

Gatling正式支持IntelliJ IDEA和eclipse。



#### IntelliJ IDEA

您需要安装社区版中提供的Scala插件。然后，您就可以使用Scala源直接导入常规maven或sbt项目。



#### Eclipse

您必须安装[ScalaIDE](http://scala-ide.org/index.html)，最好是最新版本，甚至是里程碑。

遗憾的是，截至今天，ScalaIDE仅适用于Eclipse 4.7（Oxygen），并且与更现代的版本不兼容。

请注意，Eclipse 4.7不支持Java 9+，因此如果您的计算机上安装了多个Java，则可能必须强制使用JVM `eclipse.ini`，例如：

```
-vm
/Library/Java/JavaVirtualMachines/jdk1.8.0_162.jdk/Contents/Home/bin/java
```

##### SBT 

如果你正在使用sbt，你可以使用[sbteclipse](https://github.com/sbt/sbteclipse)来生成eclipse项目配置。

##### Maven

如果您正在使用maven，则可以将[scala-maven-plugin](https://github.com/davidB/scala-maven-plugin)与[m2eclipse-scala](https://github.com/sonatype/m2eclipse-scala)结合使用。前者将编译Scala代码，后者将执行ScalaIDE和m2e（maven的eclipse插件）之间的集成。

您必须在pom.xml中添加以下部分：

```
<build>
        <sourceDirectory>src/main/scala</sourceDirectory>
        <testSourceDirectory>src/test/scala</testSourceDirectory>
        <plugins>
                <plugin>
                        <groupId>net.alchim31.maven</groupId>
                        <artifactId>scala-maven-plugin</artifactId>
                        <version>MANUALLY_REPLACE_WITH_LATEST_VERSION</version>
<configuration>
  <args>
    <arg>-target:jvm-1.8</arg>
    <arg>-deprecation</arg>
    <arg>-feature</arg>
    <arg>-unchecked</arg>
    <arg>-language:implicitConversions</arg>
    <arg>-language:postfixOps</arg>
  </args>
</configuration>
                        <executions>
                                <execution>
                                        <goals>
                                                <goal>compile</goal>
                                                <goal>testCompile</goal>
                                        </goals>
                                </execution>
                        </executions>
                </plugin>
        </plugins>
</build>
```

您也可以使用[scalor-maven-plugin](https://github.com/random-maven/scalor-maven-plugin)。

您必须在pom.xml中添加以下部分：

```
<build>
  <plugins>
    <plugin>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.8.0</version>
      <configuration>
        <skip>true</skip>
        <skipMain>true</skipMain>
      </configuration>
    </plugin>
    <plugin>
      <groupId>com.carrotgarden.maven</groupId>
      <artifactId>scalor-maven-plugin_2.12</artifactId>
      <version>MANUALLY_REPLACE_WITH_LATEST_VERSION</version>
      <configuration>
                                      <zincOptionsScala>
                                              -target:jvm-1.8
                                              -deprecation
                                              -feature
                                              -unchecked
                                              -language:implicitConversions
                                              -language:postfixOps
                                      </zincOptionsScala>
                              </configuration>
      <executions>
        <execution>
          <goals>
            <goal>eclipse-config</goal>
            <goal>eclipse-format</goal>
            <goal>eclipse-restart</goal>
            <goal>eclipse-prescomp</goal>
            <!-- <goal>register-main</goal> --> <!-- uncomment if you have some Scala code to compile in src/main/scala -->
            <goal>register-test</goal>
            <!-- <goal>compile-main</goal> -->  <!-- uncomment if you have some Scala code to compile in src/main/scala -->
            <goal>compile-test</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```



### 从IDE启动加特林

在[gatling-highcharts-maven-archetype](https://gatling.io/docs/current/extensions/maven_archetype#maven-archetype)生成，您可以使用从您的IDE手动启动加特林和记录一些辅助类。可以完美复制[这3个类](https://github.com/gatling/gatling-highcharts-maven-archetype/tree/master/src/main/scala)（`Engine`，`Recorder`并`IDEPathHelper`在自己的项目）。