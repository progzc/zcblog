package com.progzc.blog.configuration;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @Description Swagger相关配置（也可在YML中进行配置），本项目选择在配置类中进行配置。
 * @Author zhaochao
 * @Date 2020/10/26 10:42
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */

@Configuration
@EnableSwagger2 // 启用Swagger
public class SwaggerConfig implements WebMvcConfigurer {

    // 加载Swagger的默认U界面
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    // 配置Swagger的Docket的Bean实例（每一个Docket的Bean实例对应于一个分组，这样可以方便协同开发）
    @Bean
    public Docket createRestApiGroup1(Environment environment){
        // 设置要显示的Swagger环境
        Profiles profiles = Profiles.of("dev", "test");
        // 获取项目的环境
        boolean isDevAndTest = environment.acceptsProfiles(profiles);

        System.out.println("isDevAndTest："+isDevAndTest);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否启动Swagger，若为false，则Swagger不能在浏览器中访问
                .enable(isDevAndTest) // 可以控制Swagger在开发及测试环境中使用，在生产环境不使用
                .select()
                // RequestHandlerSelectors.basePackage("包名")：扫描指定的包
                // RequestHandlerSelectors.any()：扫描全部
                // RequestHandlerSelectors.none()：不扫描
                // RequestHandlerSelectors.withMethodAnnotation(注解.class)：扫描方法上的注解
                // RequestHandlerSelectors.withClassAnnotation(注解.class)：扫描类上的注解
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) // 配置要扫描接口的方式
                // PathSelectors.any()：放行所有路径
                // PathSelectors.ant("/article")：只放行/article路径
                .paths(PathSelectors.any()) // 过滤映射路径
                .build()
                .groupName("Clouds")
                // 可以由使用者设置全局token（一般登录成功后都会设置一个token作为同行证）放置到HTTP请求头中，在跨域访问时作为通行证
                .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        // 作者信息
        Contact contact = new Contact("Clouds", "http://blog.progzc.com", "zcprog@foxmail.com");
        return new ApiInfoBuilder()
                .title("zcblog")
                .description("zcblog的接口文档")
                .termsOfServiceUrl("http://blog.progzc.com")
                .version("v1.0")
                .contact(contact)
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }

    private List<ApiKey> security() {
        // 设置登录的用户名为token，登录的密码为token
        return newArrayList(new ApiKey("token", "token", "header"));
    }

}
