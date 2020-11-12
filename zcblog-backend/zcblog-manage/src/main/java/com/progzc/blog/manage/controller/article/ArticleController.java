package com.progzc.blog.manage.controller.article;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 文章
 * @Author zhaochao
 * @Date 2020-10-25
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @GetMapping("/test")
    public void test() {
        System.out.println("66666正在进行热部署...");
    }

    @GetMapping("/test2")
    public void test2() {
        System.out.println("2222正在进行热部署...");
    }

    @GetMapping("/test3")
    public void test3() {
        System.out.println("44444444正在进行热部署...");
    }

    @GetMapping("/test5")
    public void test5() {
        System.out.println("bbbb正在进行热部署...");
    }

    @GetMapping("/test4")
    public void test4() {
        System.out.println("333");
    }
}
