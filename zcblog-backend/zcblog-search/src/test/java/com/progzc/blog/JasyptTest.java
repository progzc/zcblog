package com.progzc.blog;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description TODO
 * @Author zhaochao
 * @Date 2020/10/23 19:19
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class JasyptTest {

    @Autowired
    private StringEncryptor encryptor;

    @Value("${oss.qiniu.accessKey}")
    private String accessKey;

    @Value("${oss.qiniu.secretKey}")
    private String secretKey;

    /**
     * 解密测试
     */
    @Test
    public void testJasypt(){
        System.out.println("accessKey:" + accessKey);
        System.out.println("secretKey:" + secretKey);
    }
}
