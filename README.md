一个简易的基于服务端内存的MQ
===

### 需要的依赖

基于https://github.com/jianliu/lsf 的netty rpc基础代码
需下载https://github.com/jianliu/lsf ִ后打包 mvn install
得到

```java
        <dependency>
            <groupId>com.liuj</groupId>
            <artifactId>lsf</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
使用了spring命名空间
```java
       消费端启动入口com.liuj.lmq.spring.ConsumerMain
       生产端启动入口com.liuj.lmq.spring.ProduceMain
```

## MQ服务中心端，负责接收和分发消息
server配置中心负责接收生产者推送的数据，然后分发给消费者，server端在内存中记录数据
       server启动无spring配置，启动入口
       com.liuj.lmq.server.MQServer

## 生产者端 producer
生产者配置简单，多个生产者还未分离出一个相同的client负责传输数据到server
```xml
    <!--server端地址-->
    <lmq:server id="server" index="127.0.0.1" port="2222"/>

    <!--生产者对象-->
    <lmq:producer id="producer" topic="b" server="server"/>
```
## 消费端，负责发送消息 consumer
每个消费者都会有一个自己的线程池来处理消息，基于spring的配置
```xml
    <!--server端地址-->
    <lmq:server id="server" index="127.0.0.1" port="2222"/>

	<!--多个consumer共享一个client，它负责和server端交流-->
    <lmq:consumerClient id="consumerClient" server="server"/>

	<!--消息监听器-->
    <bean id="messageListener" class="com.liuj.lmq.client.DefaultMessageListener"/>

	<!--消费者实例-->
	<lmq:consumer id="defaultConsumer" topic="b" listener="messageListener" transport="consumerClient"
                  corePoolSize="1" maxPoolSize="5"/>

```
