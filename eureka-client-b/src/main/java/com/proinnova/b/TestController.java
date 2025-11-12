package com.proinnova.b;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test1")
    public String test1() {
        return "test1";
    }

    @GetMapping("/test")
    public String test() {
        return "eureka-client-a的test接口返回：" + restTemplate.getForObject("https://eureka-client-a/test", String.class);
    }
}
