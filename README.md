以SPI为基础架构搭建高拓展高可用的RPC。

设计模式：代理、工厂、单例、责任链、观察者
模块有： 注册中心 代理层 路由层 容错层 协议层 拦截器层 SPI 业务线程池

# 快速开始
1. 启动 Zookeeper
首先需要启动 Zookeeper 作为注册中心

2. 定义服务接口
   在 hello-service-api 模块中定义服务接口和数据传输对象：
   ```
   // HelloService.java - 服务接口
   public interface HelloService {
    String hello(Hello hello);
   }
   // Hello.java - 数据传输对象
   @AllArgsConstructor
   @NoArgsConstructor
   @Getter
   @Setter
   @Builder
   @ToString
   public class Hello implements Serializable {
    private String message;
    private String description;
   }
   ```

服务提供端
1. 实现服务接口
在 example-server 模块中实现服务接口，使用 @RpcService 注解标记服务：
```
@Slf4j
@RpcService(group = "test1", version = "version1")
public class HelloServiceImpl implements HelloService {
    static {
        System.out.println("HelloServiceImpl被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
```

2. 启动服务提供者
使用 @RpcScan 注解扫描服务，启动 Netty 服务器：
```
@RpcScan(basePackage = {"github.javaguide"})
public class NettyServerMain {
    public static void main(String[] args) {
        autoRegistry();
    }

    public static void autoRegistry() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        HelloService helloService = applicationContext.getBean(HelloServiceImpl.class);
        helloService.hello(new Hello("你好fzk", "你好服务端"));
        nettyRpcServer.start();
    }
}
```

服务消费端
1. 创建服务消费者
使用 @RpcReference 注解注入远程服务：
```
@Component
public class HelloController {

    @RpcReference(version = "version1", group = "test1")
    private HelloService helloService;

    public void test() throws InterruptedException {
        String hello = this.helloService.hello(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "Hello description is 222".equals(hello);
        Thread.sleep(12000);
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("111", "222")));
        }
    }
}
```
2. 启动服务消费者
```
@RpcScan(basePackage = {"github.javaguide"})
public class NettyClientMain {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
```

运行步骤
启动 Zookeeper：确保 Zookeeper 在 127.0.0.1:2181 运行
启动服务提供者
启动服务消费者

框架支持多种配置方式：

注册中心：默认使用 Zookeeper，地址为 127.0.0.1:2181
序列化方式：支持 Kryo、Protostuff、Hessian 等
负载均衡：支持随机、轮询等策略
