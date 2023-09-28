

### solutions
#### 依赖冲突
如果同时引入了 groupId 和 artifactId 相同而 version 不同的包时，Maven会认为发生了依赖冲突，将进行依赖调解，确保classpath中将只会有一个明确版本的包。
* maven会自动通过两个原则决定使用哪个版本的包：
    第一原则，路径最近者优先；
    第二原则，在pom中第一声明者优先。
    依赖调解的两个原则都存在最终引入的包不确定性的隐患。
* 最优的解决方案为手动去排除不需要的那个版本的包，需要的自动引入。
    ```xml
    <dependencies>
        <dependency>
            <groupId>org.rone</groupId>
            <artifactId>spring</artifactId>
            <version>1.0-SNAPSHOT</version>
            <exclusions>
                <exclusion>
                    <groupId>org.apache.commons</groupId>
                    <artifactId>commons-lang3</artifactId>
                    <version>3.9</version>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.10</version>
        </dependency>
    </dependencies>
    ```


************************************************************************************************************************
### 坑
* 两个不同的maven远程仓库，在本地也需要配置不同的本地仓，不然不同的本地仓会冲突，导致项目打包失败(2019.07.29 cbpms项目二期)

* 2020.05.04 - 由于在父项目中配置了如下的配置，导致项目中mybatis的xml文件(/mapper/*.xml)始终没有编译导致项目运行失败
    ```xml
    <resources>
        <resource>
            <includes>
                <include>*.xml</include>
                <include>*.properties</include>
                <include>*.yml</include>
            </includes>
        </resource>
    </resources>
    ```


************************************************************************************************************************
### pom.xml 示例
[参考](https://www.cnblogs.com/qq78292959/p/3711501.html)
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!--POM模型版本,模型本身的版本很少改变-->
    <modelVersion>4.0.0</modelVersion>

    <!--项目的全球唯一标识符-->
    <groupId>org.rone.study</groupId>
    <!--构件的标识符，它和group ID一起唯一标识一个构件-->
    <artifactId>Java</artifactId>
    <!--项目当前版本，格式为:主版本.次版本.增量版本-限定版本号(主要有：snapshot快照版本(简化版本)、alpha内测版、beta公测版、release稳定版、GA正式发布版)-->
    <version>1.0-SNAPSHOT</version>
    <!--打包类型，默认是jar，可以配置成war、zip、pom类型。-->
    <packaging>jar</packaging>

    <!--当前项目名-->
    <name>java_study</name>
    <!--项目描述-->
    <description>java学习</description>

    <!--
        属性值标签，也叫变量标签。
        以值替代名称，Properties可以在整个POM中使用。格式是<name>value</name>。
    -->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
    </properties>

    <!--定义一系列的配置信息，用于区分不同的运行环境-->
    <profiles>
        <!--开发环境-->
        <profile>
            <id>dev</id>
            <properties>
                <env>dev</env>
                <jarname>Java_study_dev</jarname>
            </properties>
            <activation>
                <!--设置默认激活这个配置-->
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <!--测试环境-->
        <profile>
            <id>beat</id>
            <properties>
                <env>beat</env>
                <jarname>Java_study_beat</jarname>
            </properties>
        </profile>
        <!--线上环境-->
        <profile>
            <id>release</id>
            <properties>
                <env>release</env>
                <jarname>Java_study_release</jarname>
            </properties>
        </profile>
    </profiles>

    <!--
        当有其他多个项目依赖该项目时，<dependencyManagement>可用于帮助统一各个jar包的版本。
        不同于<dependencies>，<dependencyManagement>只是声明只在子项目依赖相同的jar包且没有指定版本号时起作用，而父项目的<dependencies>会强制所有子项目依赖所有jar包。
     -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId></groupId>
                <artifactId></artifactId>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <!--该项目的所有依赖,引入jar包-->
    <dependencies>
        <!--单个依赖-->
        <dependency>
            <!--项目的全球唯一标识符-->
            <groupId>redis.clients</groupId>
            <!--构件的标识符-->
            <artifactId>jedis</artifactId>
            <!--可选项：版本，该属性缺失时会使用仓库中最新的版本。实测效果为不指定时本地库里面会生成一个unknown的目录-->
            <version>2.9.0</version>
			<!--可选项：依赖范围。有六个可选值：（https://blog.csdn.net/seasonsbin/article/details/79093647）
				compile（编译范围）（默认依赖范围）对于编译、测试、运行三种classpath都可用，例如项目中有spring-core的依赖，那么spring-core不管是在编译，测试，还是运行都会被用到，因此spring-core必须是编译范围。
				provided（已提供范围）由容器来提供jar包，典型的例子就是servlet-api， 编译和测试该项目的时候需要该依赖，但是在运行时，web容器已经提供的该依赖，所以运行时就不再需要此依赖。
				runtime（运行时范围）只对测试和运行两种classpath可用，对编译无效。典型例子就是JDBC的驱动实现，项目主代码编译的时候只需要JDK提供的JDBC接口，只有在测试和运行的时候才需要实现上述接口的具体JDBC驱动。
				test（测试范围）只对测试classpath有效，最典型的例子就是 Junit, 构件在测试时才需要，所以它的依赖范围是测试。
				system（系统范围）与provided类似，但是你必须显式的提供一个对于本地系统中JAR文件的路径。
					必须同时提供一个systemPath元素，提供本地引用的jar包路径。
				import（导入依赖范围）该依赖范围不会对三种classpath产生影响，该依赖范围只能与dependencyManagement元素配合使用。
			-->
			<!--<scope>一个范围</scope>-->
			<!-- 引用本地jar的地址 -->
			<!--<systemPath>${project.basedir}/src/main/resources/lib/rone-core-1.17.jar</systemPath>-->
			<!--可选项：排除依赖传递：即：当前项目依赖当前配置的依赖包A时，如果这个依赖包又依赖其他包B，这里可以选择排除依赖的传递性，不下载导入B-->
			<!--<exclusions>-->
				<!--<exclusion>-->
					<!--被排除的依赖包坐标-->
					<!--<groupId></groupId>-->
					<!--<artifactId></artifactId>-->
					<!--<version></version>-->
				<!--</exclusion>-->
			<!--</exclusions>-->
        </dependency>

        <dependency>
            <groupId>com.rabbitmq</groupId>
            <artifactId>amqp-client</artifactId>
            <version>4.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.3</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.3</version>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.0</version>
        </dependency>

    </dependencies>

    <!--构建项目-->
    <build>
        <!--用于web项目，在浏览器中的访问路径-->
        <!--<finalName>/java_study</finalName>-->
        <!--构建的插件-->
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.6.1</version>
                <!--设置插件的参数值-->
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>

        <!--资源文件。-->
        <!--默认情况下，如果没有指定resources，目前认为自动会将classpath下的src/main/java下的.class文件
            和src/main/resources下的.xml文件放到target里头的classes文件夹下的package下的文件夹里。
            如果设定了resources，那么默认的就会失效，就会以指定的includes和excludes为准。-->
        <resources>
            <resource>
                <!--指定build资源到哪个目录，目标文件路径，默认是base directory-->
                <!--<targetPath>/</targetPath>-->
                <!--指定是否将filter文件(即build下的filters里定义的*.property文件)的变量值在这个resource文件中，默认false-->
                <filtering>true</filtering>
                <!--资源文件的路径，默认位于/src/main/resources/目录下-->
                <directory>src/main/resources</directory>
                <!--一组文件名的匹配模式，被匹配的资源文件将被构建过程处理-->
                <includes>
                    <include>*.xml</include>
                    <include>*.properties</include>
                </includes>
                <!--一组文件名的匹配模式，被匹配的资源文件将被构建过程忽略。同时被includes和excludes匹配的资源文件，将被忽略。-->
                <excludes>
                    <exclude>*.txt</exclude>
                </excludes>
            </resource>
        </resources>

        <!--给出对资源文件进行过滤的属性文件的路径，默认位于${basedir}/src/main/filters/目录下。属性文件中定义若干键值对。
            在构建过程中，对于资源文件中出现的变量（键），将使用属性文件中该键对应的值替换。-->
        <filters>
            <filter>src/main/resources/customer/db_${env}.properties</filter>
        </filters>
    </build>


<!--说明类信息-->
    <!--开发者信息列表-->
    <developers>
        <!--单个开发者信息-->
        <developer>
            <!--开发者标识-->
            <id>rone</id>
            <name>zouRongHui</name>
            <email>870136444@qq.com</email>
            <url>https://github.com/zouRongHui</url>
            <!--担任的角色-->
            <roles>
                <!--具体职责-->
                <role>All the work</role>
            </roles>
        </developer>
    </developers>

    <!--描述了所有License列表-->
    <licenses>
        <!--项目许可证信息，用来发布时授予别人使用此项目的权利-->
        <license>
            <!--license用于法律上的名称-->
            <name>Rone</name>
            <!--官方的license正文页面的URL-->
            <url>https://github.com/zouRongHui</url>
            <!--项目分发的主要方式:repo,可以从Maven库下载;manual,用户必须手动下载和安装依赖-->
            <distribution>repo</distribution>
            <!--关于license的补充信息-->
            <comments>java study.</comments>
        </license>
    </licenses>

    <!--组织信息-->
    <organization>
        <name>Rone</name>
        <url>https://github.com/zouRongHui</url>
    </organization>

</project>

```
