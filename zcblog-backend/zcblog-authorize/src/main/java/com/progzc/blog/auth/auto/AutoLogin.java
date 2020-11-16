package com.progzc.blog.auth.auto;

import com.progzc.blog.common.Result;
import com.progzc.blog.common.constants.KaptchaConstants;
import com.progzc.blog.common.utils.EncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 自动登录获取token并放入Swagger的请求头（目前无法做到放入Swagger的请求头）
 * @Author zhaochao
 * @Date 2020/11/14 9:55
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
@Service
@Slf4j
public class AutoLogin implements ApplicationListener<ContextRefreshedEvent> {

    private static final String protocol = "http";
    private static String host = "localhost";

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
            Thread thread = new Thread(() ->{
                // 请求路径
                String baseUrl = protocol + "://" + host + ":" + port + contextPath;
                // 设置请求头
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
                HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

                // 生成uuid
                String uuid = UUID.randomUUID().toString();
                HashMap<String, String> hashMap = new HashMap<>(16);
                hashMap.put("uuid", uuid);

                // 获取验证码
                ResponseEntity<Result> captchaResponse = restTemplate.exchange(baseUrl + "/captcha.jpg?uuid={uuid}", HttpMethod.GET, entity, Result.class, hashMap);

                // 进行登录
                if (captchaResponse.getBody().get("captchaPath") != null) {
                    HashMap<String, String> formMap = new HashMap<>();
                    String encryptUsername = EncryptUtils.encrypt("admin123");
                    String encryptPassword = EncryptUtils.encrypt("admin123");
                    formMap.put("username", encryptUsername);
                    formMap.put("password", encryptPassword);
                    formMap.put("uuid", uuid);
                    formMap.put("captcha", KaptchaConstants.captcha);
                    HttpEntity<HashMap<String, String>> dataEntity = new HttpEntity<>(formMap, httpHeaders);
                    ResponseEntity<Result> loginResponse = restTemplate.postForEntity(baseUrl + "/admin/sys/login", dataEntity, Result.class);
                    String token = (String) loginResponse.getBody().get("token");
                    log.debug("-----自动登录的token-----:" + token);
                    // 利用反射设置swagger的全局token
                    ParameterBuilder tokenParam = new ParameterBuilder();
                    Parameter parameter = tokenParam.name("token").description("登录令牌").defaultValue(token)
                            .modelRef(new ModelRef("string")).parameterType("header").required(false).build();
                    Class<?> restApiGroup1 = applicationContext.getType("restApiGroup1");
                    if(Docket.class.equals(restApiGroup1)){
                        Field[] declaredFields = restApiGroup1.getDeclaredFields();
                        Object docket = applicationContext.getBean("restApiGroup1");
                        for (Field declaredField : declaredFields) {
                            if(declaredField.getName() == "globalOperationParameters"){
                                declaredField.setAccessible(true);
                                try {
                                    declaredField.set(docket, Arrays.asList(parameter));
                                } catch (IllegalAccessException e) {
                                    log.error("-----restApiGroup1属性修改失败-----");
                                }
                            }
                        }
                    }
                }
            });
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
            Future<?> future = executorService.submit(thread);
            executorService.schedule(()->{
                log.debug("-----关闭自动登录线程-----");
                future.cancel(true);
            }, 6, TimeUnit.SECONDS);
            executorService.shutdown();
        }
    }
}
