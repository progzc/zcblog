package com.progzc.blog;

import com.progzc.blog.common.Result;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.UUID;

/**
 * @Description Restful请求测试
 * @Author zhaochao
 * @Date 2020/11/14 10:44
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class RestTemplateTest {
    @Test
    public void test(){
        String baseUrl = "http://localhost:8082/blog";
        RestTemplate restTemplate = new RestTemplateBuilder().build();

        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        // 生成uuid
        String uuid = UUID.randomUUID().toString();
        HashMap<String, String> hashMap = new HashMap<>(16);
        hashMap.put("uuid", uuid);

//        ResponseEntity<Result> exchange = restTemplate.exchange(baseUrl + "/captcha.jpg?uuid={1}", HttpMethod.GET, entity, Result.class, uuid);
        ResponseEntity<Result> exchange = restTemplate.exchange(baseUrl + "/captcha.jpg?uuid={uuid}", HttpMethod.GET, entity, Result.class, hashMap);

        System.out.println(exchange.getBody());

    }
}
