package com.wang.feign.clients;


import com.wang.feign.config.DefaultFeignConfiguration;
import com.wang.feign.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("userservice")
// 配置文件局部生效：局部生效就是当前服务作为服务消费者调用指定服务提供者的时候采用改配置。
//@FeignClient(value = "userservice", configuration = DefaultFeignConfiguration.class)
public interface UserClient {

    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);

}
