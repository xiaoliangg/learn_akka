# 项目介绍

akka集群和akka客户端，用于测试akka集群间通信和replicator

## 模块介绍

* cluster akka服务端
* client akka客户端
* msg 公共组件

# 启动cluster

1. 打包

```shell
cd M_ReplicatorDemo
mvn clean install -DskipTests
```

2. 运行

```shel
cd M_ReplicatorDemo/cluster/target
java -jar cluster-1.0-SNAPSHOT-shaded.jar 15035
另启动一个cmd,再运行一个服务节点:
java -jar cluster-1.0-SNAPSHOT-shaded.jar 15036
```



