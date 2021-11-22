package com.czxy.consumer.controller;

import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    //@Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping
    public String consumer(@RequestParam("id") String id){
        //1. 根据user-service获取user-service的集群的信息
        List<ServiceInstance> instances = discoveryClient.getInstances("service");
        //2. 由于没有集群，只有一个，所以直接去除第一个
        ServiceInstance instance = instances.get(0);
        //3. 拼接url
        String url = "http://"+instance.getHost()+":"+instance.getPort()+"/user/"+id;
        //4. 使用restTemplate发起请求
        ResponseEntity<String> entity = restTemplate.getForEntity(url,String.class);
        //5. 获取返回对象
        String body = entity.getBody();
        return body;

//        String url = "http://localhost:8082/user/"+id;
//        return this.restTemplate.getForObject(url,String.class);
    }

}
