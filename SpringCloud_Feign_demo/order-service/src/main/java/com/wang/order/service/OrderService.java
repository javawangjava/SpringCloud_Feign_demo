package com.wang.order.service;


import com.wang.feign.clients.UserClient;
import com.wang.feign.pojo.User;
import com.wang.order.mapper.OrderMapper;
import com.wang.order.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    // 使用Feign来实现远程调用
    public Order queryOrderById(Long orderId){
        // 1.查询订单
        Order order = orderMapper.findById(orderId);

        //2.远程调用方式2:利用Feign发起http请求，查询用户
        User user = userClient.findById(order.getUserId());

        //3.存入order
        order.setUser(user);
        // 4.返回
        return order;
    }


 /*
    @Autowired
    private RestTemplate restTemplate;
    public Order queryOrderById(Long orderId){
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.远程调用方式1：利用RestTemplate 发起http请求，查询用户
        //// 2.1 方式1：获取url路径
        //String url="http://localhost:8081/user/"+order.getUserId();

        //// 2.1 方式2：修改访问的url路径，用服务名代替ip、端口：
        //String url="http://userservice/user/"+order.getUserId();

        //// 2.2 发送http请求，实现远程调用
        //User user = restTemplate.getForObject(url, User.class);

        //3.存入order
        order.setUser(user);
        // 4.返回
        return order;
    }
    */


}
