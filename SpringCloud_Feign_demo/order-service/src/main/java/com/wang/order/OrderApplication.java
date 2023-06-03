package com.wang.order;

import com.wang.feign.clients.UserClient;
import com.wang.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.wang.order.mapper")
@SpringBootApplication
//@EnableFeignClients
// 配置文件全局生效。全局生效就是当前服务作为服务消费者调用其他服务提供者的时候都采用改配置。
@EnableFeignClients(clients = UserClient.class,defaultConfiguration = DefaultFeignConfiguration.class)
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    /**
     * 创建RestTemplate并注入Spring容器
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    //一般用默认的负载均衡规则，不做修改。也就是不用声明这个Bean
    //@Bean
    //public IRule randomRule(){
    //    return new RandomRule();
    //}

}
