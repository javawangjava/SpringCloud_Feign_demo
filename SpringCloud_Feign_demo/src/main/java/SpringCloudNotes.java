public class SpringCloudNotes {

    /*
     * 微服务调用方式
     *        1.基于RestTemplate发起的http请求实现远程调用;
     *           1.1注册一个RestTemplate的实例到Spring容器,在需要注入的服务的配置类中使用@Bean注入;
     *               // SpringBoot启动类是一个配置类。在项目的配置类中声明一个Bean对象。
     *               @Bean
     *               public RestTemplate restTemplate(){
     *                   return new RestTemplate();
     *               }
     *           1.2 在需要使用的Service层中注入RestTemplate对象，后续进行调用。
     *
     *        2.http请求做远程调用是与语言无关的调用，只要知道对方的ip、端口、接口路径、请求参数即可。
     *
     * */

    /*
     *
     * Eureka注册中心
     *   SpringCloud中的注册中心来解决，其中最广为人知的注册中心就是Eureka。
     *   在Eureka架构中，微服务角色有两类：
     *
     *   1.EurekaServer：服务端，注册中心；
     *       1.1.记录服务信息；
     *       1.2.心跳监控；
     *   2.EurekaClient：客户端。这个就是每一个服务提供者中要；
     *       2.1.Provider：服务提供者，例如案例中的 user-service；
     *           2.1.1.注册自己的信息到EurekaServer；
     *           2.1.2.每隔30秒向EurekaServer发送心跳；
     *       2.2.consumer：服务消费者，例如案例中的 order-service；
     *           2.2.1.根据服务名称从EurekaServer拉取服务列表；
     *           2.2.2.基于服务列表做负载均衡，选中一个微服务后发起远程调用。
     *
     *   【搭建eureka-server】：
     *       注册中心服务端：eureka-server，这必须是一个独立的微服务；
     *      【搭建eureka-server】创建eureka-server服务；
     *           1-1.在父工程下，创建一个子模块eureka-server；
     *           1-2.在子模块eureka-server的pom文件中引入SpringCloud为eureka提供的starter依赖<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>；
     *           1-3.给eureka-server服务编写一个启动类，一定要添加一个@EnableEurekaServer注解，开启eureka的注册中心功能；
     *              import org.springframework.boot.SpringApplication;
     *              import org.springframework.boot.autoconfigure.SpringBootApplication;
     *              import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
     *              @SpringBootApplication
     *              @EnableEurekaServer
     *              public class EurekaApplication {
     *                  public static void main(String[] args) {
     *                      SpringApplication.run(EurekaApplication.class, args);
     *                  }
     *              }
     *
     *           1-4.编写配置文件application.yml文件；注意yml格式文件的缩进
     *              server:
     *                port: 10086 # 服务端口
     *              spring:
     *                  application:
     *                      name: eurekaserver # eureka的服务名称
     *              eureka:
     *                  client:
     *                      service-url:  # eureka的地址信息
     *                          defaultZone: http://127.0.0.1:10086/eureka
     *              #           defaultZone: http://localhost:10086/eureka
     *
     *
     * 【服务注册】
     *      1.在需要注册的服务中引入依赖：在需要注册的服务的pom文件中引入<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>；
     *      2.修改需要注册的服务的application.yml文件，添加服务名称、eureka地址
     *          spring:
     *              application:
     *                  name: userservice # orderservice的服务名称
     *          eureka:
     *              client:
     *                  service-url:  # eureka的地址信息
     *                      defaultZone: http://127.0.0.1:10086/eureka
     *
     * */

    /*
     *
     * Ribbon负载均衡
     *   负载均衡原理：SpringCloud底层其实是利用了一个名为Ribbon的组件，来实现负载均衡功能的。
     *   SpringCloudRibbon的底层采用了一个拦截器，拦截了RestTemplate发出的请求，对地址做了修改。
     *
     *   负载均衡策略
     *       负载均衡的规则都定义在IRule接口中。
     *       RoundRobinRule:简单轮询服务列表来选择服务器。它是Ribbon默认的负载均衡规则。
     *       ZoneAvoidanceRule:以区域可用的服务器为基础进行服务器的选择。使用Zone对服务器进行分类，这个Zone可以理解为一个机房、一个机架等。而后再对Zone内的多个服务做轮询。
     *       默认的实现就是ZoneAvoidanceRule，是一种轮询方案。
     *
     *   自定义负载均衡策略
     *       通过定义IRule实现可以修改负载均衡规则，有两种方式：注意，一般用默认的负载均衡规则，不做修改。
     *       【自定义负载均衡策略方式一】：在服务消费者的配置类【启动类】中定义一个新的IRule:
     *           这种是修改全局的负载均衡策略，只要是本服务作为消费者，那么所消费的服务都是采用改策略.
     *           @Bean
     *           public IRule randomRule(){
     *               return new RandomRule();
     *           }
     *
     *       【自定义负载均衡策略方式二】：在服务消费者的配置文件application.yml文件中，添加有关服务提供者的新的配置
     *       服务提供者: # 给某个微服务配置负载均衡规则，这里是userservice服务
     *           ribbon:
     *               NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 负载均衡规则
     *
     *
     *
     * */


    /*
    * 饥饿加载
    *   Ribbon默认是采用懒加载，即第一次访问时才会去创建LoadBalanceClient，请求时间会很长。
    *   而饥饿加载则会在项目启动时创建，降低第一次访问的耗时，通过下面配置开启饥饿加载：
    * */

    /*
    *
    * Nacos注册中心:
    *   Nacos需要安装；不需要单独建立一个服务。
    *   Nacos是阿里巴巴的产品，现在是SpringCloud中的一个组件。相比Eureka功能更加丰富.
    *   Nacos是SpringCloudAlibaba的组件，而SpringCloudAlibaba也遵循SpringCloud中定义的服务注册、服务发现规范。
    *   1）引入依赖:父工程的pom文件中的 <dependencyManagement> 中引入SpringCloudAlibaba的依赖：<artifactId>spring-cloud-alibaba-dependencies</artifactId>
    *   2）配置nacos地址: 在服务中的yml文件中添加nacos地址；
    *
    *   1.Nacos服务分级存储模型
    *       1.1一级是服务，例如userservice；
    *       1.2二级是集群，例如杭州或上海；
    *       1.3三级是实例，例如杭州机房的某台部署了userservice的服务器；
    *   2.如何设置实例的集群属性
    *   2.1修改application.yml文件，添加spring.cloud.nacos.discovery.cluster-name属性即可
    *
    *  Nacos提供了namespace来实现环境隔离功能。
    *       nacos中可以有多个namespace;
    *       namespace下可以有group、service等;
    *       不同namespace之间相互隔离，例如不同namespace的服务互相不可见;
    *       默认情况下，所有service、data、group都在同一个namespace，名为public：
    *       给微服务配置namespace:给微服务配置namespace只能通过修改配置来实现【命名空间，填ID】。
    *
    *   Nacos配置共享
    *       1.微服务会从nacos读取的配置文件：
    *           1.1、[服务名]-[spring.profile.active].yaml，环境配置;
    *           1.2、[服务名].yaml，默认配置，多环境共享;

    *       2.不同微服务共享的配置文件：
    *           2.1、通过shared-configs指定;
    *           2.2、通过extension-configs指定;
    *       3.优先级：
    *           3.1、环境配置 >服务名.yaml > extension-config > extension-configs > shared-configs > 本地配置;
    *
    * */

    /*
    *   Feign远程调用
    *       Feign是一个声明式的http客户端，其作用就是帮助我们优雅的实现http请求的发送。
    *       使用Feign的步骤：
    *           1.服务消费者的pom中引入依赖：<artifactId>spring-cloud-starter-openfeign</artifactId>；
    *           2.在服务消费者的启动类上添加@EnableFeignClients注解开启Feign的功能；
    *           3.在服务消费者的模块中编写FeignClient接口；接口中方法主要是基于SpringMVC的注解来声明远程调用的信息。
    *               服务名称：userservice；请求方式：GET；请求路径：/user/{id}；请求参数：Long id；返回值类型：User。
    *               @FeignClient("userservice")
    *               public interface UserClient {
    *                   @GetMapping("/user/{id}")
    *                   User findById(@PathVariable("id") Long id);
    *               }
    *           4.在服务消费者要调用服务提供者的方法中使用FeignClient中定义的方法代替RestTemplate。
    *               4.1.在该类中注入FeignClient的对象，然后使用FeignClient对象来调用FeignClient中定义的方法来对服务消费者进行远程调用。
    *
    *   Feign的日志配置:
    *   1.方式一是配置文件，feign.client.config.xxx.loggerLevel；
    *       1.1如果xxx是default则代表全局；
    *       1.2如果xxx是服务名称，例如userservice则代表某服务；
    *   2.方式二是java代码配置Logger.Level这个Bean；
    *       2.1如果在@EnableFeignClients注解声明则代表全局；
    *       2.2如果在@FeignClient注解中声明则代表某服务；
    *
    *
    *   Feign的优化：Feign底层发起http请求，依赖于其它的框架。
    *       1.日志级别尽量用basic
    *       2.使用HttpClient或OKHttp代替URLConnection
    *           ① 在服务消费者的pom文件中引入Apache的HttpClient依赖：<artifactId>feign-httpclient</artifactId>;
    *           ② 在配置文件开启httpClient功能，设置连接池参数。
    *
    *
    *   Feign的最佳实践：
    *       1.让controller和FeignClient继承同一接口；
    *       2.将FeignClient、POJO、Feign的默认配置都定义到一个项目中，供所有消费者使用。
    *
    *       实现最佳实践方式二的步骤如下：
    *           首先创建一个module，命名为feign-api，然后引入feign的starter依赖
    *           将order-service中编写的UserClient、User、DefaultFeignConfiguration都复制到feign-api项目中
    *           在order-service中引入feign-api的依赖
    *           修改order-service中的所有与上述三个组件有关的import部分，改成导入feign-api中的包
    *           重启测试
    *       不同包的FeignClient的导入有两种方式：
    *           1.在@EnableFeignClients注解中添加basePackages，指定FeignClient所在的包@EnableFeignClients(clients = UserClient.class)；
    *           2.在@EnableFeignClients注解中添加clients，指定具体FeignClient的字节码：@EnableFeignClients(clients = {UserClient.class})；
    * */

    /*
    * Gateway网关是我们服务的守门神，所有微服务的统一入口。
    *   网关的核心功能特性：
    *       权限控制：网关作为微服务入口，需要校验用户是是否有请求资格，如果没有则进行拦截。
    *       路由和负载均衡：一切请求都必须先经过gateway，但网关不处理业务，而是根据某种规则，把请求转发到某个微服务，这个过程叫做路由。当然路由的目标服务有多个时，还需要做负载均衡。
    *       限流：当请求流量过高时，在网关中按照下流的微服务能够接受的速度来放行请求，避免服务压力过大。
    *   而SpringCloudGateway则是基于Spring5中提供的WebFlux，属于响应式编程的实现，具备更好的性能
    *
    *   Gateway基本步骤如下：
    *       1. 创建SpringBoot工程gateway，引入网关依赖:<artifactId>spring-cloud-starter-gateway</artifactId>;<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>;
    *       2. 编写启动类;
    *       3. 编写基础配置和路由规则;
    *       4. 启动网关服务进行测试;
    *
    *   Gateway断言工厂：在配置文件中写的断言规则只是字符串，这些字符串会被Predicate Factory读取并处理，转变为路由判断的条件；使用时查文档。
    *
    *   Gateway过滤器工厂
    *       GatewayFilter是网关中提供的一种过滤器，可以对进入网关的请求和微服务返回的响应做处理；
    *
    *   Gateway全局过滤器：定义方式是实现GlobalFilter接口。
    *       全局过滤器的作用也是处理一切进入网关的请求和微服务响应，与GatewayFilter的作用一样。
    *       区别在于GatewayFilter通过配置定义，处理逻辑是固定的；而GlobalFilter的逻辑需要自己写代码实现。
    *           在filter中编写自定义逻辑，可以实现下列功能：登录状态判断，权限校验，请求限流等。
    *
    *
    *
    *  */


}
